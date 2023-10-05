package com.gw.user.config;

import com.gw.common.util.TokenManager;
import com.gw.user.cache.CacheManager;
import com.gw.user.domain.User;
import com.gw.user.service.UserService;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.UUID;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@ExtendWith(MockitoExtension.class)
class AuthenticationManagerTest {
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CacheManager cacheManager;
    private AuthenticationManager authenticationManager;
    private final User user = new User(UUID.randomUUID(),
            "TestFirstName",
            "TestLastName",
            "TestUserName",
            "",
            "",
            "01/01/1989",
            "ADMIN");
    private final static String KEY_256_BIT = "8A6872AD13BEC411DAC9746C7FEDB8A6872AD13BEC411DAC9746C7FEDB";

    private String token;
    private TokenManager tokenManager;

    @BeforeEach
    void setUp() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(KEY_256_BIT);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        tokenManager = new TokenManager(signingKey);
        authenticationManager = new AuthenticationManager(userService, tokenManager, cacheManager);
        token = tokenManager.generateToken(user, Duration.ofMinutes(1L));
    }

    @Test
    void shouldReturnEmptyWhenNoUserFound() {
        when(userService.findUserBy(user.userId())).thenReturn(Mono.empty());

        //when
        Mono<Authentication> authenticate =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token, ""));

        StepVerifier.create(authenticate)
                .expectError(IllegalCallerException.class)
                .verify();
    }

    @Test
    void shouldReturnEmptyWhenTokenIsExpired() {
        token = tokenManager.generateToken(user, Duration.ofMillis(10L));
        await("Wait for token to Expire").atLeast(Duration.ofMillis(50))
                .until(() -> true);

        //when
        Mono<Authentication> authenticate =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token, ""));

        StepVerifier.create(authenticate)
                .expectNextCount(0)
                .verifyComplete();

        verifyNoInteractions(userService);
    }

    @Test
    void shouldReturnEmptyWhenTokenIsValidButInvalidated() {
        when(userService.findUserBy(user.userId())).thenReturn(Mono.just(user));
        when(cacheManager.isValidToken(token)).thenReturn(false);

        //when
        Mono<Authentication> authenticate =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token, ""));

        StepVerifier.create(authenticate)
                .expectError(IllegalCallerException.class)
                .verify();
    }

    @Test
    void shouldReturnResponseWhenUserFound() {
        when(cacheManager.isValidToken(token)).thenReturn(true);
        when(userService.findUserBy(user.userId())).thenReturn(Mono.just(user));

        //when
        Mono<Authentication> authenticate =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token, ""));

        StepVerifier.create(authenticate)
                .expectNextCount(1)
                .verifyComplete();
    }
}