package com.gw.user.repo;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository {
    Mono<User> findUserById(UUID id);

    Mono<Void> addUser(User userToAdd, String encryptedPassword, String salt);

    Mono<User> findUserByEmail(String username);

    Mono<Void> addExternalUser(ExternalUser userToAdd);

    Mono<ExternalUser> findExternalUserByEmail(String email);
}
