package com.gw.user.config;

import com.gw.common.util.TokenManager;
import com.gw.user.cache.CacheManager;
import com.gw.user.resource.domain.UserProfile;
import com.gw.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

public class AuthenticationManager implements ReactiveAuthenticationManager {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationManager.class);

    private final UserService userService;
    private final TokenManager tokenManager;
    private final CacheManager cacheManager;

    public AuthenticationManager(UserService userService,
                                 TokenManager tokenManager,
                                 CacheManager cacheManager) {
        this.userService = userService;
        this.tokenManager = tokenManager;
        this.cacheManager = cacheManager;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String tokenAsString = authentication.getPrincipal().toString();
        var token = tokenManager.parse(tokenAsString);
        String userId = token.getUserId();
        LOG.debug("Token: {}", token);

        if (userId != null) {
            LOG.info("Fetch user details for: {}", userId);

            return userService.findUserBy(UUID.fromString(userId))
                    .filter(user -> token.isTokenValid())
                    .filter(user -> cacheManager.isValidToken(tokenAsString))
                    .switchIfEmpty(Mono.error(() -> new IllegalCallerException(userId)))
                    .flatMap(ud -> {
                        // After setting the Authentication in the context, we specify
                        // that the current user is authenticated. So it passes the
                        // Spring Security Configurations successfully.

                        String authority = token.getRole();

                        LOG.info("Authorities Object {}", authority);

                        var userprofile = new UserProfile(UUID.fromString(ud.id()), authority, token.toString());

                        LOG.info("User {} authenticated with role: {}", userId, authority);
                        UsernamePasswordAuthenticationToken userTokenData =
                                new UsernamePasswordAuthenticationToken(userprofile, "",
                                        Collections.singletonList(new SimpleGrantedAuthority(ud.role())));
                        return Mono.just(userTokenData);
                    });
        }

        return Mono.empty();
    }
}
