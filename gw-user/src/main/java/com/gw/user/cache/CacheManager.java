package com.gw.user.cache;

import com.gw.user.config.AuthConfig;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

@Component
public class CacheManager {
    private final JedisPool jedisPool;
    private final long invalidationKeyExpiry;
    private static final String INVALIDATE_KEY = "invalidated:";
    private static final String LOGIN_KEY = "login:";

    public CacheManager(JedisPool jedisPool, AuthConfig authConfig) {
        this.jedisPool = jedisPool;
        this.invalidationKeyExpiry = authConfig.tokenDuration().getSeconds();
    }

    public long updateLoginCache(String newToken, String value) {
        try(var jedis = jedisPool.getResource()) {
            String oldToken = jedis.hget(LOGIN_KEY, value);
            if (oldToken != null) {
                jedis.setex(INVALIDATE_KEY + oldToken, invalidationKeyExpiry, value);
            }
            return jedis.hset(LOGIN_KEY, value, newToken);
        }
    }

    public boolean isValidToken(String token) {
        try(var jedis = jedisPool.getResource()) {
            return jedis.get(INVALIDATE_KEY +token) == null;
        }
    }

    public String invalidate(String token, String value) {
        try(var jedis = jedisPool.getResource()) {
            return jedis.setex(INVALIDATE_KEY + token, 1, value);
        }
    }
}
