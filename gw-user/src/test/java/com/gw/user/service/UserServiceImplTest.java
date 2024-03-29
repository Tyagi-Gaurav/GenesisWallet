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
import java.util.UUID;

import static com.gw.user.testutils.TestUserBuilder.aUser;
import static com.gw.user.testutils.TestUserBuilder.copyOf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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
    void findUserById_shouldReturnUser() {
        UUID userId = UUID.randomUUID();
        User user = aUser().build();

        when(userRepository.findUserById(userId)).thenReturn(Mono.just(user));

        StepVerifier.create(userService.findUserBy(userId))
                .expectNext(user)
                .verifyComplete();
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
    void addUser() {
        User user = aUser().build();
        when(secureRandom.nextLong()).thenReturn(NEXT_SECURE_RANDOM);
        given(passwordEncryptor.encrypt(eq(user.password()), anyString())).willReturn(user.password());

        User userWithSaltAndPassword = copyOf(user)
                .withSalt(user.lastName() + NEXT_SECURE_RANDOM + user.firstName())
                .build();

        when(userRepository.addUser(userWithSaltAndPassword)).thenReturn(Mono.empty());

        StepVerifier.create(userService.addUser(userWithSaltAndPassword))
                .verifyComplete();
    }

    @Test
    void incrementRegistrationMetricOnAddUser() {
        User user = aUser().build();

        when(secureRandom.nextLong()).thenReturn(NEXT_SECURE_RANDOM);
        given(passwordEncryptor.encrypt(eq(user.password()), anyString())).willReturn(user.password());

        User userWithSaltAndPassword = copyOf(user)
                .withSalt(user.lastName() + NEXT_SECURE_RANDOM + user.firstName())
                .build();

        when(userRepository.addUser(userWithSaltAndPassword)).thenReturn(Mono.empty());

        StepVerifier.create(userService.addUser(userWithSaltAndPassword))
                .verifyComplete();

        verify(userRegistrationCounter).increment("WEB", "HOMEPAGE");
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

    @Test
    void authenticateUser() {
        String password = "encryptedPassword";
        User user = aUser().withPassword(password).build();

        when(userRepository.findUserByUserName(user.userName())).thenReturn(Mono.just(user));
        when(passwordEncryptor.encrypt(eq(user.password()), anyString())).thenReturn("encryptedPassword");

        StepVerifier.create(userService.authenticateUser(user.userName(), password))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void authenticateUser_returnFalseWhenPasswordIsDifferent() {
        User user = aUser().build();
        User otherUser = copyOf(user).withPassword("testPassword").build();

        when(passwordEncryptor.encrypt(user.password(), user.salt())).thenReturn("encryptedUserPassword");
        when(userRepository.findUserByUserName(user.userName())).thenReturn(Mono.just(otherUser));

        StepVerifier.create(userService.authenticateUser(user.userName(), user.password()))
                .verifyComplete();
    }

    @Test
    void authenticateUser_returnFalseWhenUserNotFound() {
        User user = aUser().build();

        when(userRepository.findUserByUserName(user.userName())).thenReturn(Mono.empty());

        StepVerifier.create(userService.authenticateUser(user.userName(), user.password()))
                .verifyComplete();
    }
}