package com.gw.common.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

@ConstructorBinding
@ConfigurationProperties("resilience")
public record ResilienceConfig(Map<String, CircuitBreakerConfig> circuitBreaker) {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return Optional.ofNullable(circuitBreaker)
                .map(cb ->
                        CircuitBreakerRegistry.of(cb.entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().enabled())
                                .collect(Collectors.toMap(Map.Entry::getKey,
                                        entry -> entry.getValue().build())))
                ).orElseGet(CircuitBreakerRegistry::ofDefaults);
    }

    @Bean
    @Qualifier("resilienceExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(10);
    }
}
