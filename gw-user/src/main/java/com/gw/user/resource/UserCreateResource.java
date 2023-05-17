package com.gw.user.resource;

import com.gw.common.domain.User;
import com.gw.user.resource.domain.UserCreateRequestDTO;
import com.gw.user.resource.domain.UserCreateResponseDTO;
import com.gw.user.service.UserService;
import com.gw.user.service.domain.Role;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class UserCreateResource {
    private final UserService userService;

    public UserCreateResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = "application/vnd+user.create.v1+json",
            produces = "application/vnd+user.create.v1+json",
            path = "/user/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<UserCreateResponseDTO> createUser(@Validated @RequestBody UserCreateRequestDTO userCreateRequestDTO) {
        UUID userId = UUID.randomUUID();
        return userService.addUser(new User(
                userId,
                userCreateRequestDTO.firstName(),
                userCreateRequestDTO.lastName(),
                userCreateRequestDTO.userName(),
                userCreateRequestDTO.password(),
                userCreateRequestDTO.dateOfBirth(),
                userCreateRequestDTO.gender(),
                userCreateRequestDTO.homeCountry(),
                Role.REGISTERED_USER.name()))
                .thenReturn(new UserCreateResponseDTO(userId.toString()));
    }
}
