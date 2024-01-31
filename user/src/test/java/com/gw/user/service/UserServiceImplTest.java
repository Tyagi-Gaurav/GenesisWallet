package com.gw.user.service;

import com.gw.common.metrics.UserRegistrationCounter;
import com.gw.security.util.PasswordEncryptor;
import com.gw.user.domain.ExternalUser;
import com.gw.user.domain.ExternalUserBuilder;
import com.gw.user.domain.User;
import com.gw.user.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.security.SecureRandom;

import static com.gw.user.testutils.TestUserBuilder.aUser;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncryptor passwordEncryptor;

    @Mock
    private UserRegistrationCounter userRegistrationCounter;

    @Mock
    private SecureRandom secureRandom = new SecureRandom();

    private UserServiceImpl userService;

    private static final long NEXT_SECURE_RANDOM = 2023L;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncryptor, secureRandom, userRegistrationCounter);
    }

    @Test
    void findUserByUsername_shouldReturnUser() {
        User user = aUser().build();

        when(userRepository.findUserByUserName(user.userName())).thenReturn(Mono.just(user));

        StepVerifier.create(userService.findUserBy(user.userName()))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void addExternalUser() {
        ExternalUser externalUser = ExternalUserBuilder.newBuilder()
                .withUserName("some-user-name")
                .withExternalSystem("google")
                .build();

        when(userRepository.findOrCreateExternalUser(externalUser)).thenReturn(Mono.just(externalUser));

        StepVerifier.create(userService.addExternalUser(externalUser))
                .expectNext(externalUser)
                .verifyComplete();
    }

    @Test
    void incrementRegistrationCounterOnAddExternalUser() {
        ExternalUser externalUser = ExternalUserBuilder.newBuilder()
                .withUserName("some-user-name")
                .withExternalSystem("google")
                .build();

        when(userRepository.findOrCreateExternalUser(externalUser)).thenReturn(Mono.just(externalUser));

        StepVerifier.create(userService.addExternalUser(externalUser))
                .expectNext(externalUser)
                .verifyComplete();

        verify(userRegistrationCounter).increment("WEB", externalUser.externalSystem());
    }
}