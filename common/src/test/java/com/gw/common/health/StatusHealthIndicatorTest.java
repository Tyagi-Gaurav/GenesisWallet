package com.gw.common.health;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import reactor.test.StepVerifier;

class StatusHealthIndicatorTest {
    private final StatusHealthIndicator statusHealthIndicator = new StatusHealthIndicator();

    @Test
    void health() {
        StepVerifier.create(statusHealthIndicator.health())
                .expectNext(Health.up().build())
                .verifyComplete();
    }
}