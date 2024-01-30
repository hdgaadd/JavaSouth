package org.codeman;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hdgaadd
 * created on 2022/09/14
 *
 * from: https://github.com/zhugezifang/hdfs-to-redis
 *
 * question: 针对大量数据写入Redis, 造成的: 1.Redis有写入限制, 数据写入可能失败 2.大量写入造成Redis瞬时流量高峰
 *
 * solution: 1. qps过高则进行写入休眠 2.每1000条数据才提交一次Redis, 而不是一条一次
 */
public class Client {

    private static final Properties PROPERTIES = CommonParaUtil.paraUtil();

    private static final int TOTAL_QPS = Integer.parseInt(PROPERTIES.getProperty("qps"));

    private static final String FILE_PATH = PROPERTIES.getProperty("hdfs.input.path");

    private static int requiredQps;

    public static void main(String[] args) throws IOException {
        SparkSession spark = SparkSession.builder()
                .appName("Spark-to-redis")
                .master("local")
                .getOrCreate();
        JavaRDD<String> lines = spark.read().textFile(FILE_PATH).javaRDD();

        JavaRDD<Row> rows = lines.map((Function<String, Row>) line -> {
            String[] str = line.split(" ");
            return RowFactory.create(str[0], str[1]);
        });
        int partition = rows.getNumPartitions();
        requiredQps = TOTAL_QPS / partition;

        rows.foreachPartition(row -> {
            Jedis jedis = RedisUtil.getInstance(PROPERTIES.getProperty("redis.ip"), Integer.parseInt(PROPERTIES.getProperty("redis.port")), PROPERTIES.getProperty("redis.pwd"));
            Pipeline pipeline = jedis.pipelined();
            AtomicLong atomicLong = new AtomicLong();
            long start = System.currentTimeMillis();

            row.forEachRemaining(v -> {
                        atomicLong.incrementAndGet();
                        qpsControll(start, atomicLong);

                        System.out.println(v.getString(0) + " , " +  v.getString(1));
                        pipeline.sadd(v.getString(0), v.getString(1));
                        // 每1000条提交一次
                        if (atomicLong.get() % 1000 == 0) {
                            pipeline.sync();
                        }
                    }
            );
            pipeline.close();
            jedis.close();
        });
        spark.stop();
    }

    /**
     * 写入控制
     *
     * @param start
     * @param count
     */
    private static void qpsControll(long start, AtomicLong count) {
        // 当前qps
        long actualQps = 1000 * count.get() / (System.currentTimeMillis() - start);
        System.out.println("current qps is " + actualQps);

        if (actualQps > (long) requiredQps) {
            System.out.println("============STOP============");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

}

