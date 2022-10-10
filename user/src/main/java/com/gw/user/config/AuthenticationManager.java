package com.gw.user.config;

import com.gw.user.resource.domain.UserProfile;
import com.gw.user.service.UserService;
import com.gw.user.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AuthenticationManager implements ReactiveAuthenticationManager {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationManager.class);

    private final UserService userService;
    private final Key signingKey;

    public AuthenticationManager(UserService userService, Key signingKey) {
        this.userService = userService;
        this.signingKey = signingKey;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getPrincipal().toString();
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(token, signingKey);
        String userId = getUserIdFromToken(jwtTokenUtil);
        LOG.debug("Token: {}", token);

        if (userId != null) {
            LOG.info("Fetch user details for: {}", userId);

            return userService.findUserBy(UUID.fromString(userId))
                    .filter(jwtTokenUtil::validateToken)
                    .switchIfEmpty(Mono.error(() -> new IllegalCallerException(userId)))
                    .flatMap(ud -> {
                        // After setting the Authentication in the context, we specify
                        // that the current user is authenticated. So it passes the
                        // Spring Security Configurations successfully.

                        String authority = (String) jwtTokenUtil.getClaimFromToken(claims -> claims.get("Authorities"));

                        LOG.info("Authorities Object {}", authority);

                        var userprofile = new UserProfile(ud.id(), authority, token);

                        LOG.info("User {} authenticated with role: {}", userId, authority);
                        UsernamePasswordAuthenticationToken userTokenData =
                                new UsernamePasswordAuthenticationToken(userprofile, "",
                                        Collections.singletonList(new SimpleGrantedAuthority(ud.role())));
                        return Mono.just(userTokenData);
                    });
        }

        return Mono.empty();
    }

    private String getUserIdFromToken(JwtTokenUtil jwtTokenUtil) {
        try {
            return jwtTokenUtil.getUserIdFromToken();
        } catch(ExpiredJwtException e) {
            LOG.warn("Expired user token found");
            return null;
        }
    }
}