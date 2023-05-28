package com.gw.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * This health indicator can be accessed using
 * <code>
 *     /actuator/health/status
 * </code>
 *
 * because classname is Status, so it gets picked up as "/status"
 */
@Component
public class StatusHealthIndicator implements ReactiveHealthIndicator {
    @Override
    public Mono<Health> health() {
        return Mono.just(Health.up().build());
    }
}
