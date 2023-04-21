package com.gw.user.cache;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import redis.clients.jedis.JedisPool;

@Component
public class CacheHealthIndicator implements ReactiveHealthIndicator  {
    private final JedisPool jedisPool;

    public CacheHealthIndicator(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Mono<Health> health() {
        try (var jedis = jedisPool.getResource()) {
            jedis.ping();
            return Mono.just(Health.up().build());
        } catch(Exception e) {
            return Mono.just(Health.down(e).build());
        }
    }
}
