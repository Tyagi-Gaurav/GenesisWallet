package com.gw.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class StatusHealthIndicator implements ReactiveHealthIndicator {
    @Override
    public Mono<Health> health() {
        return Mono.just(Health.up().build());
    }
}
