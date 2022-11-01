package com.gw.user.resource;

import com.gw.common.domain.User;
import com.gw.common.exception.ApplicationAuthenticationException;
import com.gw.common.util.TokenManager;
import com.gw.security.util.PasswordEncryptor;
import com.gw.user.resource.domain.LoginRequestDTO;
import com.gw.user.resource.domain.LoginResponseDTO;
import com.gw.user.resource.domain.UserCreateRequestDTO;
import com.gw.user.service.UserService;
import com.gw.user.service.domain.Role;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;

@RestController
public class UserCreateResource {
    private final UserService userService;
    private final TokenManager tokenManager;
    private final PasswordEncryptor passwordEncryptor;
    private final SecureRandom secureRandom;

    public UserCreateResource(UserService userService,
                              TokenManager tokenManager,
                              PasswordEncryptor passwordEncryptor,
                              SecureRandom secureRandom) {
        this.userService = userService;
        this.tokenManager = tokenManager;
        this.passwordEncryptor = passwordEncryptor;
        this.secureRandom = secureRandom;
    }

    @PostMapping(consumes = "application/vnd+user.create.v1+json",
            produces = "application/vnd+user.create.v1+json",
            path = "/user/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<Void> createUser(@Valid @RequestBody UserCreateRequestDTO userCreateRequestDTO) {
        var salt = userCreateRequestDTO.lastName() + secureRandom.nextLong() + userCreateRequestDTO.firstName();
        return userService.addUser(new User(
                UUID.randomUUID(),
                userCreateRequestDTO.firstName(),
                userCreateRequestDTO.lastName(),
                userCreateRequestDTO.userName(),
                passwordEncryptor.encrypt(userCreateRequestDTO.password(), salt),
                salt,
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
