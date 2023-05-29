package com.gw.security.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultHealth;
import reactor.core.publisher.Mono;

@Component
public class VaultHealthIndicator implements ReactiveHealthIndicator {
    private final VaultTemplate vaultTemplate;

    public VaultHealthIndicator(VaultTemplate vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
    }

    @Override
    public Mono<Health> health() {
        return Mono.just(vaultTemplate.opsForSys().health())
                .filter(VaultHealth::isInitialized)
                .map(health -> Health.up().build())
                .switchIfEmpty(Mono.just(Health.down().outOfService().build()));
    }
}
