package com.gw.user.repo;

import com.gw.user.domain.ExternalUser;
import com.gw.user.domain.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository {
    Mono<User> findUserByUserName(String username);

    Mono<ExternalUser> findOrCreateExternalUser(ExternalUser externalUser);
}
