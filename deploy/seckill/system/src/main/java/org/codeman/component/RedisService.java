package org.codeman.component;

import org.codeman.entity.SeckillOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Collections;

import static org.codeman.constant.RedisKey.LIMITUSER;
import static org.codeman.constant.RedisKey.STOCK;

/**
 * @author hdgaadd
 * created on 2021/12/10
 */
@Service
@Slf4j
public class RedisService {
    @Resource
    private JedisPool jedisPool;

    private static int accessCount = 0;

    public void setKey(String key, Integer totalStock) {
        Jedis resource = jedisPool.getResource();
        resource.set(key, totalStock.toString());
        resource.close();
    }

    /**
     * 通过Jedis连接池锁定库存
     *
     * @param activityId
     * @return
     */
    public Boolean stockDeductValidator(Long activityId) {
        log.info("redis脚本执行");
        String key = STOCK.toString() + activityId;
        try (Jedis jedisClient = jedisPool.getResource()) {
            String script = "if redis.call('exists', KEYS[1]) == 1 then\n" +
                    "	local stock = tonumber(redis.call('get', KEYS[1]))\n" +
                    "	if (stock <= 0) then\n" +
                    "		return -1\n" +
                    "	end;\n" +
                    "	redis.call('decr', KEYS[1]);\n" +
                    "	return stock - 1;\n" +
                    "end;\n" +
                    "return -1;";
            Long stock = (Long) jedisClient.eval(script, Collections.singletonList(key), Collections.emptyList());
            if (stock < 0) {
                log.error("库存不足!");
                return false;
            }
            log.info(String.format("秒杀请求为第%s次: purchase succeed", accessCount++));
            return true;
        } catch (Throwable throwable) {
            log.error(throwable.toString());
            return false;
        }
    }

    /**
     * 把该用户id作为key，加入到Jedis连接池里，作为限选用户
     *
     * @param seckillActivityId
     * @param userId
     * @return
     */
    public void addLimitUser(Long seckillActivityId, Long userId) {
        Jedis resource = jedisPool.getResource();
        resource.sadd(LIMITUSER.toString()  + userId,  String.valueOf(userId));
        resource.close();
        log.info("添加限选用户成功");
    }

    /**
     * 恢复Redis库存
     *
     * @param order
     */
    public void revertStock(SeckillOrder order) {
        Jedis resource = jedisPool.getResource();
        resource.incr(STOCK.toString()  + order.getSeckillActivityId());
        log.info("恢复Redis库存成功");
        resource.close();
    }

    /**
     * 将用户从锁定状态解除
     *
     * @param order
     */
    public void removeLimitMember(SeckillOrder order) {
        Jedis resource = jedisPool.getResource();
        resource.srem(LIMITUSER.toString() + order.getUserId(), String.valueOf(order.getUserId()));
        log.info("解除用户限选状态成功");
        resource.close();
    }

    /**
     * 检查该用户是否为限选用户
     *
     * @param memberId
     * @return
     */
    public Boolean isLimitMember(Long activityId, Long memberId) {
        Jedis resource = jedisPool.getResource();
        Boolean isLimit = resource.sismember(LIMITUSER.toString() + memberId, String.valueOf(memberId));
        log.info("检查用户是否限选成功");
        return isLimit;
    }
}
