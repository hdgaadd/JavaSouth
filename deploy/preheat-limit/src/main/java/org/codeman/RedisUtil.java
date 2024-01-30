package org.codeman;

import redis.clients.jedis.Jedis;

public class RedisUtil {

    private static volatile Jedis jedis = null;

    private RedisUtil() {

    }

    public static Jedis getInstance(String ip, int port, String pwd) {
        if (jedis == null) {
            synchronized (RedisUtil.class) {
                if (jedis == null) {
                    jedis = new Jedis(ip,  port);
                    return jedis;
                }
            }
        }
        return jedis;
    }
}
