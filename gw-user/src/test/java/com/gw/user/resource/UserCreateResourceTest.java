package com.gw.user.resource;

import com.gw.user.domain.Role;
import com.gw.user.domain.User;
import com.gw.user.resource.domain.UserCreateRequestDTO;
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

import static java.util.UUID.fromString;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCreateResourceTest {

    @Mock
    private UserService userService;
    private UserCreateResource userCreateResource;

    @BeforeEach
    void setUp() {
        userCreateResource = new UserCreateResource(userService);
    }

    @Test
    void createUser() {
        UserCreateRequestDTO userCreateRequestDTO = DtoBuilder.testAccountCreateRequestDTO();
        UUID userId = UUID.randomUUID();
        User user = new User(
                userId,
                userCreateRequestDTO.firstName(),
                userCreateRequestDTO.lastName(),
                userCreateRequestDTO.userName(),
                userCreateRequestDTO.password(),
                userCreateRequestDTO.dateOfBirth(),
                userCreateRequestDTO.gender(),
                userCreateRequestDTO.homeCountry(),
                Role.REGISTERED_USER.name());

        when(userService.addUser(refEq(user, "userId")))
                .thenReturn(Mono.empty());

        StepVerifier.create(userCreateResource.createUser(userCreateRequestDTO))
                .consumeNextWith(responseDto -> {
                    fromString(responseDto.userId());
                })
                .verifyComplete();
    }
}