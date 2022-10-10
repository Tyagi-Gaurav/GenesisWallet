package com.gw.user.service;

import com.gw.common.domain.User;
import com.gw.user.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static com.gw.user.utils.TestUserBuilder.aUser;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

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
}