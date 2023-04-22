package com.gw.user.resource;

import com.gw.common.util.TokenManager;
import com.gw.user.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserLogoutResource {
    private final CacheManager cacheManager;
    private final TokenManager tokenManager;

    public UserLogoutResource(CacheManager cacheManager, TokenManager tokenManager) {
        this.cacheManager = cacheManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping(consumes = "application/vnd.logout.v1+json",
            produces = "application/vnd.logout.v1+json",
            path = "/user/logout")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<Void> logout(@RequestHeader("Authorization") String authTokenHeader) {
        return Mono.fromSupplier(() -> {
            String authToken = authTokenHeader.substring(7);
            TokenManager.Token token = tokenManager.parse(authToken);
            cacheManager.invalidate(authToken, token.getUserId());
            return null;
        });
    }
}
