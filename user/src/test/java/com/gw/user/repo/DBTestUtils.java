package com.gw.user.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.common.domain.Gender;
import com.gw.common.domain.User;
import io.r2dbc.spi.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.SQLException;
import java.util.UUID;

public class DBTestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DBTestUtils.class);
    final static ObjectMapper mapper = new ObjectMapper();

    private static final String DELETE_ALL_USERS = "DELETE FROM USER_SCHEMA.USER_TABLE";

    public static void addToDatabase(User user, DatabaseClient databaseClient) {

        LOG.info("Adding {} to name: ", user);
        String query = "INSERT INTO USER_SCHEMA.USER_TABLE (ID, USER_NAME, FIRST_NAME, LAST_NAME, PASSWORD, SALT, DATE_OF_BIRTH, GENDER, HOME_COUNTRY, ROLE) " +
                "values ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)";
        Mono<Integer> result = databaseClient.sql(query)
                .bind(0, user.id().toString())
                .bind(1, user.username())
                .bind(2, user.firstName())
                .bind(3, user.lastName())
                .bind(4, user.password())
                .bind(5, user.salt())
                .bind(6, user.dateOfBirth())
                .bind(7, user.gender().name())
                .bind(8, user.homeCountry())
                .bind(9, user.role())
                .fetch()
                .rowsUpdated();

        StepVerifier.create(result).expectNext(1).verifyComplete();
        if (LOG.isInfoEnabled()) {
            LOG.info("User added with Id {}", user.id());
        }
    }

    public static Mono<User> getUser(UUID userId, DatabaseClient databaseClient) {
        LOG.info("Fetching user Id: {} from user: ", userId);
        String query = "SELECT * FROM USER_SCHEMA.USER_TABLE WHERE ID = $1";

        return databaseClient.sql(query)
                .bind(0, userId.toString())
                .map(DBTestUtils::toModel)
                .one();
    }

    private static User toModel(Row row) {
        return new User.UserBuilder()
                .setId(UUID.fromString(row.get("ID", String.class)))
                .setUsername(row.get("USER_NAME", String.class))
                .setFirstName(row.get("FIRST_NAME", String.class))
                .setLastName(row.get("LAST_NAME", String.class))
                .setDateOfBirth(row.get("DATE_OF_BIRTH", String.class))
                .setPassword(row.get("PASSWORD", String.class))
                .setSalt(row.get("SALT", String.class))
                .setHomeCountry(row.get("HOME_COUNTRY", String.class))
                .setGender(Gender.valueOf(row.get("GENDER", String.class)))
                .setRole(row.get("ROLE", String.class))
                .createUser();
    }

    public static void clearDatabase(DatabaseClient databaseClient) throws SQLException {
        Mono<Integer> result = databaseClient.sql(DELETE_ALL_USERS)
                .fetch()
                .rowsUpdated();

        StepVerifier.create(result).expectNext(1).verifyComplete();
    }
}
