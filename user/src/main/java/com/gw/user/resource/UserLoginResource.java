package com.gw.user.resource;

import com.gw.common.exception.ApplicationAuthenticationException;
import com.gw.common.util.TokenManager;
import com.gw.user.cache.CacheManager;
import com.gw.user.config.AuthConfig;
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

@RestController
public class UserLoginResource {
    private final UserService userService;
    private final TokenManager tokenManager;
    private final CacheManager cacheManager;
    private final AuthConfig authConfig;

    public UserLoginResource(UserService userService,
                             TokenManager tokenManager,
                             CacheManager cacheManager,
                             AuthConfig authConfig) {
        this.userService = userService;
        this.tokenManager = tokenManager;
        this.cacheManager = cacheManager;
        this.authConfig = authConfig;
    }

    @PostMapping(consumes = "application/vnd.login.v1+json",
            produces = "application/vnd.login.v1+json",
            path = "/user/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.authenticateUser(loginRequestDTO.userName(), loginRequestDTO.password())
                .map(user -> {
                    String token = tokenManager.generateToken(user, authConfig.tokenDuration());
                    cacheManager.updateLoginCache(token, user.id());
                    return new LoginResponseDTO(token);
                })
                .switchIfEmpty(Mono.error(() -> new ApplicationAuthenticationException("No user found [UserName]: " +
                        loginRequestDTO.userName())));
    }
}
