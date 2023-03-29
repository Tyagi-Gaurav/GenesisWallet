package com.gw.user.resource;

import com.gw.common.domain.User;
import com.gw.common.exception.ApplicationAuthenticationException;
import com.gw.common.util.TokenManager;
import com.gw.user.resource.domain.LoginRequestDTO;
import com.gw.user.resource.domain.LoginResponseDTO;
import com.gw.user.resource.domain.UserCreateRequestDTO;
import com.gw.user.service.UserService;
import com.gw.user.service.domain.Role;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@RestController
public class UserCreateResource {
    private final UserService userService;
    private final TokenManager tokenManager;

    public UserCreateResource(UserService userService,
                              TokenManager tokenManager) {
        this.userService = userService;
        this.tokenManager = tokenManager;
    }

    @PostMapping(consumes = "application/vnd+user.create.v1+json",
            produces = "application/vnd+user.create.v1+json",
            path = "/user/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<Void> createUser(@Valid @RequestBody UserCreateRequestDTO userCreateRequestDTO) {
        return userService.addUser(new User(
                UUID.randomUUID(),
                userCreateRequestDTO.firstName(),
                userCreateRequestDTO.lastName(),
                userCreateRequestDTO.userName(),
                userCreateRequestDTO.password(),
                userCreateRequestDTO.dateOfBirth(),
                userCreateRequestDTO.gender(),
                userCreateRequestDTO.homeCountry(),
                Role.REGISTERED_USER.name()));
    }

    @PostMapping(consumes = "application/vnd.login.v1+json",
            produces = "application/vnd.login.v1+json",
            path = "/user/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.authenticateUser(loginRequestDTO.userName(), loginRequestDTO.password())
                .map(value -> new LoginResponseDTO(tokenManager.generateToken(value, Duration.ofMinutes(10))))
                .switchIfEmpty(Mono.error(() -> new ApplicationAuthenticationException("No user found [UserName]: " +
                        loginRequestDTO.userName())));
    }
}
