package com.gw.user.resource;

import com.gw.common.domain.User;
import com.gw.common.exception.ApplicationAuthenticationException;
import com.gw.common.util.TokenManager;
import com.gw.user.cache.CacheManager;
import com.gw.user.config.AuthConfig;
import com.gw.user.resource.domain.LoginRequestDTO;
import com.gw.user.service.UserService;
import com.gw.user.testutils.DtoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.Duration;

import static com.gw.user.testutils.UserBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserLoginResourceTest {

    @Mock
    private TokenManager tokenManager;
    @Mock
    private UserService userService;
    @Mock
    private JedisPool jedisPool;
    @Mock
    private Jedis jedis;
    @Mock
    private AuthConfig authConfig;
    @Mock
    private CacheManager cacheManager;
    private UserLoginResource userLoginResource;
    private final Duration TOKEN_DURATION = Duration.ofSeconds(10);

    @BeforeEach
    void setUp() {
        userLoginResource = new UserLoginResource(userService, tokenManager, cacheManager, authConfig);
    }

    @Test
    void login() {
        when(authConfig.tokenDuration()).thenReturn(TOKEN_DURATION);

        User user = aUser().build();
        String expectedToken = "dummyToken";
        LoginRequestDTO loginRequestDTO = DtoBuilder.testLoginRequestDTO();

        when(userService.authenticateUser(loginRequestDTO.userName(), loginRequestDTO.password()))
                .thenReturn(Mono.just(user));

        when(tokenManager.generateToken(user, TOKEN_DURATION)).thenReturn(expectedToken);

        when(cacheManager.updateLoginCache(eq(loginRequestDTO.userName()), anyString())).thenReturn(1L);

        StepVerifier.create(userLoginResource.login(loginRequestDTO))
                .consumeNextWith(loginResponse ->
                    assertThat(loginResponse.token()).isEqualTo(expectedToken)
                )
                .verifyComplete();
    }

    @Test
    void login_whenNoUserFound() {
        LoginRequestDTO loginRequestDTO = DtoBuilder.testLoginRequestDTO();

        when(userService.authenticateUser(loginRequestDTO.userName(), loginRequestDTO.password()))
                .thenReturn(Mono.empty());

        StepVerifier.create(userLoginResource.login(loginRequestDTO))
                .expectError(ApplicationAuthenticationException.class)
                .verify();
    }
}