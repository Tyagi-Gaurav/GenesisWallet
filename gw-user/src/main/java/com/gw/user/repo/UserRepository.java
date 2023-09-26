package com.gw.user.repo;

import com.gw.common.domain.ExternalUser;
import com.gw.user.domain.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository {
    Mono<User> findUserById(UUID id);

    Mono<Void> addUser(User userToAdd, String encryptedPassword, String salt);
    default Mono<Void> addUser(User userToAdd) {
        return Mono.empty();
    }

    Mono<User> findUserByUserName(String username);

    Mono<Void> addExternalUser(ExternalUser userToAdd);

    Mono<ExternalUser> findExternalUserByUserName(String email);
}
