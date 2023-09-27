package com.gw.user.service;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.UserIdentity;
import com.gw.common.metrics.UserRegistrationCounter;
import com.gw.security.util.PasswordEncryptor;
import com.gw.user.domain.User;
import com.gw.user.repo.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.UUID;

import static com.gw.user.domain.User.UserBuilder.copyOf;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LogManager.getLogger("APP");

    private final UserRepository userRepository;
    private final PasswordEncryptor passwordEncryptor;
    private final SecureRandom secureRandom;
    private final UserRegistrationCounter userRegistrationCounter;

    public UserServiceImpl(@Qualifier("documentDB") UserRepository userRepository, PasswordEncryptor passwordEncryptor, SecureRandom secureRandom, UserRegistrationCounter userRegistrationCounter) {
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
        this.secureRandom = secureRandom;
        this.userRegistrationCounter = userRegistrationCounter;
    }

    public Mono<User> findUserBy(UUID userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public Mono<Void> addUser(User user) {
        String salt = user.generateSalt(secureRandom.nextLong());
        String encryptedPassword = passwordEncryptor.encrypt(user.password(), salt);
        var userWithSaltAndPassword = copyOf(user)
                .withPassword(encryptedPassword)
                .withSalt(salt).build();

        return userRepository.addUser(userWithSaltAndPassword)
                .doOnSuccess(v -> userRegistrationCounter.increment("WEB", "HOMEPAGE"));
    }

    @Override
    public Mono<UserIdentity> authenticateUser(String userName, String password) {
        return userRepository.findUserByUserName(userName)
                .filter(user -> {
                    String encryptedPassword = passwordEncryptor.encrypt(password, user.salt());
                    boolean equals = user.password().equals(encryptedPassword);
                    if (!equals) {
                        LOG.info("Invalid password provided by user");
                    }
                    return equals;
                })
                .map(UserIdentity.class::cast)
                .switchIfEmpty(Mono.defer(Mono::empty));
    }

    @Override
    public Mono<ExternalUser> addExternalUser(ExternalUser externalUser) {
        return userRepository.findExternalUserByUserName(externalUser.userName())
                .switchIfEmpty(Mono.defer(() ->
                        userRepository.addExternalUser(externalUser)
                                .doOnSuccess(v -> userRegistrationCounter.increment("WEB", externalUser.externalSystem()))
                                .thenReturn(externalUser)
                ));
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.empty();
    }
}
