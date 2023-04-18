package com.gw.user.resource;

import com.gw.common.exception.ApplicationAuthenticationException;
import com.gw.common.util.TokenManager;
import com.gw.user.resource.domain.LoginRequestDTO;
import com.gw.user.resource.domain.LoginResponseDTO;
import com.gw.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class UserLoginResource {
    private final UserService userService;
    private final TokenManager tokenManager;

    public UserLoginResource(UserService userService, TokenManager tokenManager) {
        this.userService = userService;
        this.tokenManager = tokenManager;
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
