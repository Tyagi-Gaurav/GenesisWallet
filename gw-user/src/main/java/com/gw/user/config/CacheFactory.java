package com.gw.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class CacheFactory {
    @Bean(destroyMethod = "destroy")
    public JedisPool jedisPool(CacheConfig cacheConfig) {
        return new JedisPool(cacheConfig.host(), cacheConfig.port());
    }
}
