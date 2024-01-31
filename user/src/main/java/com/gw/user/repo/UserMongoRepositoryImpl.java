package com.gw.user.repo;

import com.gw.user.domain.ExternalUser;
import com.gw.user.domain.User;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component("documentDB")
public class UserMongoRepositoryImpl implements UserRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private static final String USER_NAME_FIELD = "userName";

    public UserMongoRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<User> findUserByUserName(String username) {
        return reactiveMongoTemplate.findOne(query(where(USER_NAME_FIELD).is(username)), User.class);
    }

    @Override
    public Mono<ExternalUser> findOrCreateExternalUser(ExternalUser externalUser) {
        return reactiveMongoTemplate.findOne(query(where(USER_NAME_FIELD).is(externalUser.userName())), ExternalUser.class)
                .switchIfEmpty(Mono.defer(() -> reactiveMongoTemplate.save(externalUser)));
    }
}
