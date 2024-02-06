package com.gw.security.health;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultHealth;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VaultHealthIndicatorTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private VaultTemplate vaultTemplate;

    @Mock
    private VaultHealth vaultHealth;

    @InjectMocks
    private VaultHealthIndicator vaultHealthIndicator;

    @Test
    void healthy() {
        when(vaultTemplate.opsForSys().health()).thenReturn(vaultHealth);
        when(vaultHealth.isInitialized()).thenReturn(true);

        StepVerifier.create(vaultHealthIndicator.health())
                .expectNext(Health.up().build())
                .verifyComplete();
    }

    @Test
    void unhealthy() {
        when(vaultTemplate.opsForSys().health()).thenReturn(vaultHealth);
        when(vaultHealth.isInitialized()).thenReturn(false);

        StepVerifier.create(vaultHealthIndicator.health())
                .expectNext(Health.down().outOfService().build())
                .verifyComplete();
    }
}