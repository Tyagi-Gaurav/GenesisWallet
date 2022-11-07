package com.gw.user.resource;

import com.gw.common.domain.User;
import com.gw.common.exception.ApplicationAuthenticationException;
import com.gw.common.util.TokenManager;
import com.gw.security.util.PasswordEncryptor;
import com.gw.user.resource.domain.LoginRequestDTO;
import com.gw.user.resource.domain.UserCreateRequestDTO;
import com.gw.user.service.UserService;
import com.gw.user.testutils.DtoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.gw.user.testutils.UserBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCreateResourceTest {

    @Mock
    private TokenManager tokenManager;
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncryptor passwordEncryptor;
    private final SecureRandom secureRandom = new SecureRandom();

    private UserCreateResource userCreateResource;

    @BeforeEach
    void setUp() {
        userCreateResource = new UserCreateResource(userService, tokenManager, passwordEncryptor, secureRandom);
    }

    @Test
    void createUser() {
        when(passwordEncryptor.encrypt(any(String.class), any(String.class)))
                .thenAnswer((Answer<String>) invocation -> invocation.getArgument(0));

        UserCreateRequestDTO userCreateRequestDTO = DtoBuilder.testAccountCreateRequestDTO();
        when(userService.addUser(any(User.class))).thenReturn(Mono.empty());

        StepVerifier.create(userCreateResource.createUser(userCreateRequestDTO))
                .verifyComplete();
    }

    @Test
    void login() {
        User user = aUser().build();
        String expectedToken = "dummyToken";
        LoginRequestDTO loginRequestDTO = DtoBuilder.testLoginRequestDTO();

        when(userService.authenticateUser(loginRequestDTO.userName(), loginRequestDTO.password()))
                .thenReturn(Mono.just(user));

        when(tokenManager.generateToken(user, Duration.of(10, ChronoUnit.MINUTES))).thenReturn(expectedToken);

        StepVerifier.create(userCreateResource.login(loginRequestDTO))
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

        StepVerifier.create(userCreateResource.login(loginRequestDTO))
                .expectError(ApplicationAuthenticationException.class)
                .verify();
    }
}