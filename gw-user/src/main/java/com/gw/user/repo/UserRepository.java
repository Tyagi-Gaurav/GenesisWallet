package com.gw.user.repo;

import com.gw.user.domain.ExternalUser2;
import com.gw.user.domain.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository {
    Mono<User> findUserById(UUID id);

    default Mono<Void> addUser(User userToAdd) {
        return Mono.empty();
    }

    Mono<User> findUserByUserName(String username);

    Mono<Void> addExternalUser(ExternalUser2 userToAdd);

    Mono<ExternalUser2> findExternalUserByUserName(String email);

    Mono<ExternalUser2> findOrCreateExternalUser(ExternalUser2 externalUser2);
}
