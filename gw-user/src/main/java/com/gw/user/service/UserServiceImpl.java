package com.gw.user.service;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.User;
import com.gw.security.util.PasswordEncryptor;
import com.gw.user.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncryptor passwordEncryptor;
    private final SecureRandom secureRandom;

    public UserServiceImpl(UserRepository userRepository, PasswordEncryptor passwordEncryptor, SecureRandom secureRandom) {
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
        this.secureRandom = secureRandom;
    }

    public Mono<User> findUserBy(UUID userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public Mono<Void> addUser(User user) {
        String salt = user.generateSalt(secureRandom.nextLong());
        String encryptedPassword = passwordEncryptor.encrypt(user.password(), salt);
        return userRepository.addUser(user, encryptedPassword, salt);
    }

    @Override
    public Mono<User> authenticateUser(String userName, String password) {
        return userRepository.findUserByEmail(userName)
                .filter(user -> {
                    String encryptedPassword = passwordEncryptor.encrypt(password, user.salt());
                    boolean equals = user.password().equals(encryptedPassword);
                    if (!equals) {
                        LOG.debug("Invalid password provided by user");
                    }
                    return equals;
                })
                .switchIfEmpty(Mono.defer(Mono::empty));
    }

    @Override
    public Mono<ExternalUser> addExternalUser(ExternalUser externalUser) {
        return userRepository.findExternalUserByEmail(externalUser.email())
                .switchIfEmpty(Mono.defer(() ->
                        userRepository.addExternalUser(externalUser)
                                .thenReturn(externalUser)
                ));
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.empty();
    }
}
