package com.gw.user.service;

import com.gw.common.domain.User;
import com.gw.user.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> findUserBy(UUID userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public Mono<Void> addUser(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public Mono<User> findUserBy(String userName) {
        return null;
    }

    @Override
    public Mono<User> authenticateUser(String userName, String password) {
        return userRepository.findUserByName(userName)
                .filter(user -> user.password().equals(password))
                .switchIfEmpty(Mono.defer(Mono::empty));
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.empty();
    }
}
