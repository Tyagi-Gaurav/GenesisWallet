package com.gw.user.cache;

import com.gw.user.config.AuthConfig;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

@Component
public class CacheManager {
    private final JedisPool jedisPool;
    private final long invalidationKeyExpiry;
    private static final int EXTRA_MINUTE_FOR_INVALIDATION = 60;

    public CacheManager(JedisPool jedisPool, AuthConfig authConfig) {
        this.jedisPool = jedisPool;
        this.invalidationKeyExpiry = authConfig.tokenDuration().getSeconds() + EXTRA_MINUTE_FOR_INVALIDATION;
    }

    public void updateLoginCache(String userName, String newToken) {
        try(var jedis = jedisPool.getResource()) {
            String oldToken = jedis.hget("login:", userName);
            if (oldToken != null) {
                jedis.setex("invalidated:" + oldToken, invalidationKeyExpiry, userName);
            }
            jedis.hset("login:", userName, newToken);
        }
    }

    public boolean isValidToken(String token) {
        try(var jedis = jedisPool.getResource()) {
            return jedis.get("invalidated:"+token) == null;
        }
    }
}
