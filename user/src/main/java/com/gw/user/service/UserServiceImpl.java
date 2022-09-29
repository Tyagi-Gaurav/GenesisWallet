package com.gw.user.service;

import com.gw.common.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

//TODO Tests
@Service
public class UserServiceImpl implements UserService {
    @Override
    public Mono<User> findUserBy(UUID userId) {
        return Mono.empty();
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.empty();
    }
}
