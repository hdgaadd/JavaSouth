package com.codeman.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author hdgaadd
 * Created on 2021/12/10/00:13
 */
@Configuration
public class JedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.host}")
    private String HOST;
    @Value("${spring.redis.port}")
    private Integer PORT;
    @Value("${spring.redis.timeout}")
    private Integer TIMEOUT;
    @Value("${spring.redis.pool.max-active}")
    private Integer MAXACTIVE;
    @Value("${spring.redis.pool.max-wait}")
    private Long MAXWAIT;
    @Value("${spring.redis.pool.max-idle}")
    private Integer MAXIDLE;
    @Value("${spring.redis.pool.min-idle}")
    private Integer MINIDLE;


    @Bean
    public JedisPool jedisFactor() {
        JedisPoolConfig jedisPollConfig = new JedisPoolConfig();
        jedisPollConfig.setMaxTotal(MAXACTIVE);
        jedisPollConfig.setMaxWaitMillis(MAXWAIT);
        jedisPollConfig.setMaxIdle(MAXIDLE);
        jedisPollConfig.setMinIdle(MINIDLE);
        JedisPool jedisPool = new JedisPool(jedisPollConfig, HOST, PORT, TIMEOUT, null);
        return jedisPool;
    }
}
