package com.gw.user.service;

import com.gw.common.metrics.UserRegistrationCounter;
import com.gw.security.util.PasswordEncryptor;
import com.gw.user.domain.ExternalUser;
import com.gw.user.domain.User;
import com.gw.user.repo.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LogManager.getLogger("APP");

    private final UserRepository userRepository;
    private final PasswordEncryptor passwordEncryptor;
    private final SecureRandom secureRandom;
    private final UserRegistrationCounter userRegistrationCounter;

    public UserServiceImpl(@Qualifier("documentDB") UserRepository userRepository,
                           PasswordEncryptor passwordEncryptor,
                           SecureRandom secureRandom,
                           UserRegistrationCounter userRegistrationCounter) {
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
        this.secureRandom = secureRandom;
        this.userRegistrationCounter = userRegistrationCounter;
    }

    @Override
    public Mono<User> findUserBy(String userName) {
        return userRepository.findUserByUserName(userName)
                .switchIfEmpty(Mono.defer(Mono::empty));
    }

    public Mono<ExternalUser> addExternalUser(ExternalUser externalUser) {
        return userRepository.findOrCreateUser(externalUser)
                .doOnSuccess(v -> userRegistrationCounter.increment("WEB", externalUser.externalSystem()));
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.empty();
    }
}
