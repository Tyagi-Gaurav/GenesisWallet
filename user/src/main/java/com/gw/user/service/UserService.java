package com.gw.user.service;

import com.gw.common.domain.User;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService extends ReactiveUserDetailsService {
    Mono<User> findUserBy(UUID userId);

    Mono<Void> addUser(User user);

    Mono<User> authenticateUser(String userName, String password);
}
