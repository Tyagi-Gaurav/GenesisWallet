package com.gw.user.resource;

import com.gw.common.domain.User;
import com.gw.common.exception.ApplicationAuthenticationException;
import com.gw.common.util.TokenManager;
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

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.gw.user.testutils.UserBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserLoginResourceTest {

    @Mock
    private TokenManager tokenManager;
    @Mock
    private UserService userService;
    private UserLoginResource userLoginResource;

    @BeforeEach
    void setUp() {
        userLoginResource = new UserLoginResource(userService, tokenManager);
    }

    @Test
    void login() {
        User user = aUser().build();
        String expectedToken = "dummyToken";
        LoginRequestDTO loginRequestDTO = DtoBuilder.testLoginRequestDTO();

        when(userService.authenticateUser(loginRequestDTO.userName(), loginRequestDTO.password()))
                .thenReturn(Mono.just(user));

        when(tokenManager.generateToken(user, Duration.of(10, ChronoUnit.MINUTES))).thenReturn(expectedToken);

        StepVerifier.create(userLoginResource.login(loginRequestDTO))
                .consumeNextWith(loginResponse -> {
                    assertThat(loginResponse.token()).isEqualTo(expectedToken);
                })
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