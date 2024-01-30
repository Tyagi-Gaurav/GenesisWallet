package com.gw.user.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import reactor.test.StepVerifier;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheHealthIndicatorTest {
    @Mock
    private JedisPool jedisPool;
    @Mock
    private Jedis jedis;
    private CacheHealthIndicator cacheHealthIndicator;

    @BeforeEach
    void setUp() {
        cacheHealthIndicator = new CacheHealthIndicator(jedisPool);
    }

    @Test
    void healthy() {
        when(jedisPool.getResource()).thenReturn(jedis);
        when(jedis.ping()).thenReturn("");

        StepVerifier.create(cacheHealthIndicator.health())
                .expectNext(Health.up().build())
                .verifyComplete();
    }

    @Test
    void UnHealthy() {
        when(jedisPool.getResource()).thenReturn(jedis);
        RuntimeException runtimeException = new RuntimeException();
        when(jedis.ping()).thenThrow(runtimeException);

        StepVerifier.create(cacheHealthIndicator.health())
                .expectNext(Health.down(runtimeException).build())
                .verifyComplete();
    }
}