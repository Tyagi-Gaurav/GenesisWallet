package com.gw.user.service;

import com.gw.common.domain.User;
import com.gw.security.util.PasswordEncryptor;
import com.gw.user.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncryptor passwordEncryptor;

    public UserServiceImpl(UserRepository userRepository, PasswordEncryptor passwordEncryptor) {
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
    }

    public Mono<User> findUserBy(UUID userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public Mono<Void> addUser(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public Mono<User> authenticateUser(String userName, String password) {
        return userRepository.findUserByName(userName)
                .filter(user -> {
                    String encryptedPassword = passwordEncryptor.encrypt(password, user.salt());
                    return user.password().equals(encryptedPassword);
                })
                .switchIfEmpty(Mono.defer(Mono::empty));
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.empty();
    }
}
