package com.gw.user.repo;

import com.gw.common.domain.ExternalUser;
import com.gw.user.domain.User;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Query.query;

@Component("documentDB")
public class UserMongoRepositoryImpl implements UserRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public UserMongoRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<User> findUserById(UUID id) {
        return reactiveMongoTemplate.findOne(query(Criteria.where("userId").is(id)), User.class);
    }

    @Override
    public Mono<Void> addUser(User userToAdd) {
        return reactiveMongoTemplate.save(userToAdd).then();
    }

    @Override
    public Mono<User> findUserByUserName(String username) {
        return reactiveMongoTemplate.findOne(query(Criteria.where("userName").is(username)), User.class);
    }

    @Override
    public Mono<Void> addExternalUser(ExternalUser userToAdd) {
        return reactiveMongoTemplate.save(userToAdd).then();
    }

    @Override
    public Mono<ExternalUser> findExternalUserByUserName(String userName) {
        return reactiveMongoTemplate.findOne(query(Criteria.where("userName").is(userName)), ExternalUser.class);
    }
}
