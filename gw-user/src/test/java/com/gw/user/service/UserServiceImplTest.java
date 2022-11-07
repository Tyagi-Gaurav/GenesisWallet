package com.gw.user.service;

import com.gw.common.domain.User;
import com.gw.security.util.PasswordEncryptor;
import com.gw.user.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static com.gw.user.testutils.UserBuilder.aUser;
import static com.gw.user.testutils.UserBuilder.copyOf;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncryptor passwordEncryptor;

    @InjectMocks
    private UserServiceImpl userService;

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

        when(userRepository.addUser(user)).thenReturn(Mono.empty());

        StepVerifier.create(userService.addUser(user))
                .verifyComplete();
    }

    @Test
    void authenticateUser() {
        String password = "password";
        User user = aUser().withPassword(password).build();

        when(userRepository.findUserByName(user.username())).thenReturn(Mono.just(user));
        when(passwordEncryptor.encrypt(user.password(), user.salt())).thenReturn(user.password());

        StepVerifier.create(userService.authenticateUser(user.username(), password))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void authenticateUser_returnFalseWhenPasswordIsDifferent() {
        User user = aUser().build();
        User otherUser = copyOf(user).withPassword("testPassword").build();

        when(passwordEncryptor.encrypt(user.password(), user.salt())).thenReturn("encryptedUserPassword");
        when(userRepository.findUserByName(user.username())).thenReturn(Mono.just(otherUser));

        StepVerifier.create(userService.authenticateUser(user.username(), user.password()))
                .verifyComplete();
    }

    @Test
    void authenticateUser_returnFalseWhenUserNotFound() {
        User user = aUser().build();

        when(userRepository.findUserByName(user.username())).thenReturn(Mono.empty());

        StepVerifier.create(userService.authenticateUser(user.username(), user.password()))
                .verifyComplete();
    }
}