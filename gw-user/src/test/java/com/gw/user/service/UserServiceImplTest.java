package com.gw.user.service;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.User;
import com.gw.common.metrics.UserRegistrationCounter;
import com.gw.security.util.PasswordEncryptor;
import com.gw.user.repo.UserRepository;
import com.gw.user.testutils.ExternalUserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.security.SecureRandom;
import java.util.UUID;

import static com.gw.user.testutils.UserBuilder.aUser;
import static com.gw.user.testutils.UserBuilder.copyOf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
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

    private final SecureRandom secureRandom = new SecureRandom();

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncryptor, secureRandom, userRegistrationCounter);
    }

    @Test
    void findUserBy_shouldReturnUser() {
        UUID userId = UUID.randomUUID();
        User user = aUser().build();

        when(userRepository.findUserById(userId)).thenReturn(Mono.just(user));

        StepVerifier.create(userService.findUserBy(userId))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void addUser() {
        User user = aUser().build();

        when(passwordEncryptor.encrypt(eq(user.password()), anyString())).thenReturn(user.password());
        when(userRepository.addUser(eq(user), eq(user.password()), anyString())).thenReturn(Mono.empty());

        StepVerifier.create(userService.addUser(user))
                .verifyComplete();
    }

    @Test
    void incrementRegistrationMetricOnAddUser() {
        User user = aUser().build();

        when(passwordEncryptor.encrypt(eq(user.password()), anyString())).thenReturn(user.password());
        when(userRepository.addUser(eq(user), eq(user.password()), anyString())).thenReturn(Mono.empty());

        StepVerifier.create(userService.addUser(user))
                .verifyComplete();

        verify(userRegistrationCounter).increment("WEB", "HOMEPAGE");
    }

    @Test
    void addExternalUser() {
        ExternalUser externalUser = ExternalUserBuilder.aExternalUser().build();

        when(userRepository.findExternalUserByEmail(externalUser.email())).thenReturn(Mono.empty());
        when(userRepository.addExternalUser(externalUser)).thenReturn(Mono.empty());

        StepVerifier.create(userService.addExternalUser(externalUser))
                .expectNext(externalUser)
                .verifyComplete();
    }

    @Test
    void incremenRegistrationCounterOnAddExternalUser() {
        ExternalUser externalUser = ExternalUserBuilder.aExternalUser().build();

        when(userRepository.findExternalUserByEmail(externalUser.email())).thenReturn(Mono.empty());
        when(userRepository.addExternalUser(externalUser)).thenReturn(Mono.empty());

        StepVerifier.create(userService.addExternalUser(externalUser))
                .expectNext(externalUser)
                .verifyComplete();

        verify(userRegistrationCounter).increment("WEB", externalUser.externalSystem());
    }

    @Test
    void addExternalUser_whenUserPresentThenReturnExisting() {
        ExternalUser externalUser = ExternalUserBuilder.aExternalUser().build();

        when(userRepository.findExternalUserByEmail(externalUser.email())).thenReturn(Mono.just(externalUser));

        StepVerifier.create(userService.addExternalUser(externalUser))
                .expectNext(externalUser)
                .verifyComplete();

        verify(userRepository, times(0)).addExternalUser(externalUser);
    }

    @Test
    void authenticateUser() {
        String password = "encryptedPassword";
        User user = aUser().withPassword(password).build();

        when(userRepository.findUserByEmail(user.email())).thenReturn(Mono.just(user));
        when(passwordEncryptor.encrypt(eq(user.password()), anyString())).thenReturn("encryptedPassword");

        StepVerifier.create(userService.authenticateUser(user.email(), password))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void authenticateUser_returnFalseWhenPasswordIsDifferent() {
        User user = aUser().build();
        User otherUser = copyOf(user).withPassword("testPassword").build();

        when(passwordEncryptor.encrypt(user.password(), user.salt())).thenReturn("encryptedUserPassword");
        when(userRepository.findUserByEmail(user.email())).thenReturn(Mono.just(otherUser));

        StepVerifier.create(userService.authenticateUser(user.email(), user.password()))
                .verifyComplete();
    }

    @Test
    void authenticateUser_returnFalseWhenUserNotFound() {
        User user = aUser().build();

        when(userRepository.findUserByEmail(user.email())).thenReturn(Mono.empty());

        StepVerifier.create(userService.authenticateUser(user.email(), user.password()))
                .verifyComplete();
    }
}