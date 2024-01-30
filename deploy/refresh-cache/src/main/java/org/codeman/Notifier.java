package org.codeman;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author hdgaadd
 * created on 2022/11/27
 */
public class Notifier {

    private final static int SESSION_TIMEOUT =  5000;

    private final static String ZOOKEEPER_HOST = "127.0.0.1:2181";

    private final static String ZOOKEEPER_PATH = "/zkPath";

    public static void main(String[] args) throws Exception {
        // initialize
        ZooKeeper zooKeeper = new ZooKeeper(ZOOKEEPER_HOST, SESSION_TIMEOUT, null);
        ZKPaths.mkdirs(zooKeeper, ZOOKEEPER_PATH);

        CuratorFramework notifier = CuratorFrameworkFactory
                .builder()
                .connectString(ZOOKEEPER_HOST)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10, 5000))
                .build();
         notifier.start();

        // notify
        HashMap<String, Object> msg = Maps.newHashMap();
        msg.put("msg", "删除集群节点缓存");
        byte[] msgBytes = JSON.toJSONString(msg).getBytes(StandardCharsets.UTF_8);

        Stat stat = new Stat();
        // 读取旧区数据
        notifier.getData().storingStatIn(stat).forPath(ZOOKEEPER_PATH);

        Stat setStat = notifier.setData().withVersion(stat.getVersion()).forPath(ZOOKEEPER_PATH, msgBytes);
        System.out.println(String.format("notifier send msg : %s", JSON.toJSONString(setStat)));
    }
}
