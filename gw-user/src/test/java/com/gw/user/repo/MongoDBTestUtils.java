package com.gw.user.repo;

import com.gw.common.domain.ExternalUser;
import com.gw.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class MongoDBTestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(MongoDBTestUtils.class);

    public static void addToDatabase(User user, ReactiveMongoTemplate reactiveMongoTemplate) {
        LOG.info("Adding user {}: ", user);
        reactiveMongoTemplate.save(Mono.just(user)).block();
    }

    public static void addToDatabase(ExternalUser user, ReactiveMongoTemplate reactiveMongoTemplate) {
        LOG.info("Adding user {}: ", user);
        reactiveMongoTemplate.save(Mono.just(user)).block();
    }

    public static Mono<User> getUser(UUID userId, ReactiveMongoTemplate reactiveMongoTemplate) {
        LOG.info("Fetching user Id: {} from user: ", userId);
        return reactiveMongoTemplate.findOne(query(where("userId").is(userId)), User.class);
    }

    public static Mono<ExternalUser> getExternalUser(UUID userId, ReactiveMongoTemplate reactiveMongoTemplate) {
        LOG.info("Fetching external user Id: {} from user: ", userId);
        return reactiveMongoTemplate.findOne(query(where("id").is(userId)), ExternalUser.class);
    }

    public static void clearDatabase(ReactiveMongoTemplate reactiveMongoTemplate) {
        reactiveMongoTemplate.findAllAndRemove(new Query(), User.class);
    }
}
