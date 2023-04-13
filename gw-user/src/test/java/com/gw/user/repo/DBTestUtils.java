package com.gw.user.repo;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.Gender;
import com.gw.common.domain.User;
import io.r2dbc.spi.Readable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

public class DBTestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DBTestUtils.class);

    private static final String DELETE_ALL_USERS = "DELETE FROM USER_SCHEMA.USER_TABLE";
    private static final String DELETE_ALL_EXTERNAL_USERS = "DELETE FROM USER_SCHEMA.EXTERNAL_USER_TABLE";

    public static void addToDatabase(User user, DatabaseClient databaseClient) {

        LOG.info("Adding user {}: ", user);
        String query = "INSERT INTO USER_SCHEMA.USER_TABLE (ID, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, SALT, DATE_OF_BIRTH, GENDER, HOME_COUNTRY, ROLE) " +
                "values ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)";
        Mono<Long> result = databaseClient.sql(query)
                .bind(0, user.id().toString())
                .bind(1, user.email())
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

        StepVerifier.create(result).expectNext(1L).verifyComplete();
        if (LOG.isInfoEnabled()) {
            LOG.info("User added with Id {}", user.id());
        }
    }

    public static void addToDatabase(ExternalUser user, DatabaseClient databaseClient) {
        LOG.info("Adding external user {}: ", user);
        String query = "INSERT INTO USER_SCHEMA.EXTERNAL_USER_TABLE (ID, EMAIL, FIRST_NAME, LAST_NAME, LOCALE, " +
                "PICTURE_URL, TOKEN_VALUE, TOKEN_TYPE, TOKEN_EXPIRY_TIME, EXTERNAL_SYSTEM, GENDER, DATE_OF_BIRTH, HOME_COUNTRY) " +
                "values ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)";

        Mono<Long> result = databaseClient.sql(query)
                .bind(0, user.id().toString())
                .bind(1, user.email())
                .bind(2, user.firstName())
                .bind(3, user.lastName())
                .bind(4, user.locale())
                .bind(5, user.pictureUrl())
                .bind(6, user.tokenValue())
                .bind(7, user.tokenType())
                .bind(8, user.tokenExpiryTime())
                .bind(9, user.externalSystem())
                .bind(10, user.gender().name())
                .bind(11, user.dateOfBirth())
                .bind(12, user.homeCountry())
                .fetch()
                .rowsUpdated();

        StepVerifier.create(result).expectNext(1L).verifyComplete();
        if (LOG.isInfoEnabled()) {
            LOG.info("External User added with Id {}", user.id());
        }
    }

    public static Mono<User> getUser(UUID userId, DatabaseClient databaseClient) {
        LOG.info("Fetching user Id: {} from user: ", userId);
        String query = "SELECT * FROM USER_SCHEMA.USER_TABLE WHERE ID = $1";

        return databaseClient.sql(query)
                .bind(0, userId.toString())
                .map(DBTestUtils::toUserModel)
                .one();
    }

    public static Mono<ExternalUser> getExternalUser(UUID userId, DatabaseClient databaseClient) {
        LOG.info("Fetching external user Id: {} from user: ", userId);
        String query = "SELECT * FROM USER_SCHEMA.EXTERNAL_USER_TABLE WHERE ID = $1";

        return databaseClient.sql(query)
                .bind(0, userId.toString())
                .map(DBTestUtils::toExternalUserModel)
                .one();
    }

    private static User toUserModel(Readable row) {
        return new User.UserBuilder()
                .setId(UUID.fromString(row.get("ID", String.class)))
                .setEmail(row.get("EMAIL", String.class))
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

    private static ExternalUser toExternalUserModel(Readable row) {
        return new ExternalUser(
                UUID.fromString(row.get("ID", String.class)),
                row.get("EMAIL", String.class),
                row.get("LOCALE", String.class),
                row.get("PICTURE_URL", String.class),
                row.get("FIRST_NAME", String.class),
                row.get("LAST_NAME", String.class),
                row.get("TOKEN_VALUE", String.class),
                row.get("TOKEN_TYPE", String.class),
                row.get("TOKEN_EXPIRY_TIME", Long.class),
                row.get("EXTERNAL_SYSTEM", String.class),
                row.get("DATE_OF_BIRTH", String.class),
                Gender.from(row.get("GENDER", String.class)),
                row.get("HOME_COUNTRY", String.class));
    }

    public static void clearDatabase(DatabaseClient databaseClient) {
        Mono<Long> result = databaseClient.sql(DELETE_ALL_USERS)
                .fetch()
                .rowsUpdated();

        StepVerifier.create(result).consumeNextWith(i ->
                LOG.info("Number of user records deleted {}", i)).verifyComplete();

        result = databaseClient.sql(DELETE_ALL_EXTERNAL_USERS)
                .fetch()
                .rowsUpdated();

        StepVerifier.create(result).consumeNextWith(i ->
                LOG.info("Number of external user records deleted {}", i)).verifyComplete();
    }
}
