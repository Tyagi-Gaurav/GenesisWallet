package com.gw.user.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.common.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.SQLException;

public class DBTestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DBTestUtils.class);
    final static ObjectMapper mapper = new ObjectMapper();

    private static final String DELETE_ALL_USERS = "DELETE FROM USER_SCHEMA.USER_TABLE";

    public static void addToDatabase(User user, DatabaseClient databaseClient) {

        LOG.info("Adding {} to database: ", user);
        String query = "INSERT INTO USER_SCHEMA.USER_TABLE (ID, USER_NAME, FIRST_NAME, LAST_NAME, PASSWORD, DATE_OF_BIRTH, GENDER, HOME_COUNTRY, ROLE) " +
                "values ($1, $2, $3, $4, $5, $6, $7, $8, $9)";
        Mono<Integer> result = databaseClient.sql(query)
                .bind(0, user.id().toString())
                .bind(1, user.username())
                .bind(2, user.firstName())
                .bind(3, user.lastName())
                .bind(4, user.password())
                .bind(5, user.dateOfBirth())
                .bind(6, user.gender().name())
                .bind(7, user.homeCountry())
                .bind(8, user.role())
                .fetch()
                .rowsUpdated();

        StepVerifier.create(result).expectNext(1).verifyComplete();
        if (LOG.isInfoEnabled()) {
            LOG.info("User added with Id {}", user.id());
        }
    }

    public static void clearDatabase(DatabaseClient databaseClient) throws SQLException {
        Mono<Integer> result = databaseClient.sql(DELETE_ALL_USERS)
                .fetch()
                .rowsUpdated();

        StepVerifier.create(result).expectNext(1).verifyComplete();
    }
}
