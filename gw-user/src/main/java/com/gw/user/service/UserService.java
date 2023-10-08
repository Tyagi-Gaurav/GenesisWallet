package com.gw.user.service;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.UserIdentity;
import com.gw.user.domain.User;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService extends ReactiveUserDetailsService {
    Mono<User> findUserBy(UUID userId);
    Mono<User> findUserBy(String userName);

    Mono<Void> addUser(User user);

    Mono<UserIdentity> authenticateUser(String userName, String password);

    Mono<ExternalUser> addExternalUser(ExternalUser externalUser);
}
