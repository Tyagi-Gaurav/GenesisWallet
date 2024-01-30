package com.gw.user.resource;

import com.gw.user.domain.Role;
import com.gw.user.domain.User;
import com.gw.user.resource.domain.UserCreateRequestDTO;
import com.gw.user.resource.domain.UserDetailsFetchResponseDTO;
import com.gw.user.service.UserService;
import com.gw.user.testutils.DtoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsFetchResourceTest {
    @Mock
    private UserService userService;
    private UserDetailsFetchResource userDetailsFetchResource;

    @BeforeEach
    void setUp() {
        userDetailsFetchResource = new UserDetailsFetchResource(userService);
    }

    @Test
    void fetchUser() {
        UserCreateRequestDTO userCreateRequestDTO = DtoBuilder.testAccountCreateRequestDTO();
        UUID userId = UUID.randomUUID();
        User user = new User(
                userId,
                userCreateRequestDTO.firstName(),
                userCreateRequestDTO.lastName(),
                userCreateRequestDTO.userName(),
                userCreateRequestDTO.password(),
                userCreateRequestDTO.dateOfBirth(),
                Role.REGISTERED_USER.name());

        when(userService.findUserBy(userId))
                .thenReturn(Mono.just(user));

        StepVerifier.create(userDetailsFetchResource.fetchUser(userId))
                .expectNext(new UserDetailsFetchResponseDTO(
                        userCreateRequestDTO.firstName(),
                        userCreateRequestDTO.lastName(),
                        userCreateRequestDTO.dateOfBirth()
                ))
                .verifyComplete();
    }

    @Test
    void empty_whenNoUserFound() {
        UUID userId = UUID.randomUUID();
        when(userService.findUserBy(userId))
                .thenReturn(Mono.empty());

        StepVerifier.create(userDetailsFetchResource.fetchUser(userId))
                .verifyComplete();
    }
}