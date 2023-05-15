package com.gw.user.cache;

import com.gw.user.config.AuthConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheManagerTest {
    @Mock
    private JedisPool jedisPool;
    @Mock
    private Jedis jedis;
    @Mock
    private AuthConfig authConfig;

    private CacheManager cacheManager;
    private final Duration TOKEN_DURATION = Duration.ofSeconds(10);

    @BeforeEach
    void setUp() {
        when(authConfig.tokenDuration()).thenReturn(TOKEN_DURATION);
        cacheManager = new CacheManager(jedisPool, authConfig);
        when(jedisPool.getResource()).thenReturn(jedis);
    }

    @Test
    void updateLoginCacheWhenNoPreviousEntry() {
        when(jedis.hget("login:", "user")).thenReturn(null);
        when(jedis.hset("login:", "user", "token")).thenReturn(1L);

        assertThat(cacheManager.updateLoginCache("token", "user")).isEqualTo(1L);

    }

    @Test
    void updateLoginCacheWhenPreviousEntryPresent() {
        when(jedis.hget("login:", "user")).thenReturn("oldToken");
        when(jedis.hset("login:", "user", "newToken")).thenReturn(1L);
        when(jedis.setex("invalidated:oldToken", TOKEN_DURATION.getSeconds(), "user")).thenReturn("1");

        assertThat(cacheManager.updateLoginCache("newToken", "user")).isEqualTo(1L);;
    }

    @Test
    void isTokenInvalidated_TokenIsValid() {
        when(jedis.get("invalidated:token")).thenReturn(null);

        assertThat(cacheManager.isValidToken("token")).isTrue();
    }

    @Test
    void isTokenInvalidated_TokenIsInvalid() {
        when(jedis.get("invalidated:token")).thenReturn("user");

        assertThat(cacheManager.isValidToken("token")).isFalse();
    }

    @Test
    void invalidateToken() {
        when(jedis.setex("invalidated:token", 1, "user")).thenReturn("1");
        assertThat(cacheManager.invalidate("token", "user")).isNotNull();
    }
}