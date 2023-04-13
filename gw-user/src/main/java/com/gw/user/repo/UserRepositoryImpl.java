package com.gw.user.repo;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.Gender;
import com.gw.common.domain.User;
import io.r2dbc.spi.Readable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final DatabaseClient databaseClient;
    private static final String FIND_USER_BY_ID = "SELECT * FROM USER_SCHEMA.USER_TABLE WHERE ID = :id";
    private static final String FIND_USER_BY_EMAIL = "SELECT * FROM USER_SCHEMA.USER_TABLE WHERE EMAIL = :email";

    private static final String FIND_EXTERNAL_USER_BY_USER_NAME = "SELECT * FROM USER_SCHEMA.EXTERNAL_USER_TABLE WHERE EMAIL = :email";
    private static final String ADD_USER = "INSERT INTO USER_SCHEMA.USER_TABLE (ID, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, SALT, DATE_OF_BIRTH, GENDER, HOME_COUNTRY, ROLE) " +
            "values ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)";

    private static final String ADD_EXTERNAL_USER = "INSERT INTO USER_SCHEMA.EXTERNAL_USER_TABLE (ID, EMAIL, FIRST_NAME, LAST_NAME, LOCALE, " +
            "PICTURE_URL, TOKEN_VALUE, TOKEN_TYPE, TOKEN_EXPIRY_TIME, EXTERNAL_SYSTEM, GENDER, DATE_OF_BIRTH, HOME_COUNTRY) " +
            "values ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)";
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String DATE_OF_BIRTH = "DATE_OF_BIRTH";
    private static final String HOME_COUNTRY = "HOME_COUNTRY";
    private static final String GENDER = "GENDER";
    private static final String ROLE = "ROLE";

    public UserRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<User> findUserById(UUID id) {
        return databaseClient.sql(FIND_USER_BY_ID)
                .bind("id", id.toString())
                //.map(this::toUserModel)
                .map(this::toUserModel)
                .one();
    }

    @Override
    public Mono<Void> addUser(User user, String password, String salt) {
        return databaseClient.sql(ADD_USER)
                .bind(0, user.id().toString())
                .bind(1, user.email())
                .bind(2, user.firstName())
                .bind(3, user.lastName())
                .bind(4, password)
                .bind(5, salt)
                .bind(6, user.dateOfBirth())
                .bind(7, user.gender().name())
                .bind(8, user.homeCountry())
                .bind(9, user.role())
                .then();
    }

    @Override
    public Mono<User> findUserByEmail(String email) {
        return databaseClient.sql(FIND_USER_BY_EMAIL)
                .bind("email", email)
                .map(this::toFullModel)
                .one();
    }

    @Override
    public Mono<Void> addExternalUser(ExternalUser user) {
        return databaseClient.sql(ADD_EXTERNAL_USER)
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
                .then();
    }

    @Override
    public Mono<ExternalUser> findExternalUserByEmail(String email) {
        return databaseClient.sql(FIND_EXTERNAL_USER_BY_USER_NAME)
                .bind("email", email)
                .map(this::toExternalUserModel)
                .one();
    }

    private User toUserModel(Readable row) {
        return new User.UserBuilder()
                .setId(UUID.fromString(row.get("ID", String.class)))
                .setEmail(row.get(EMAIL, String.class))
                .setFirstName(row.get(FIRST_NAME, String.class))
                .setLastName(row.get(LAST_NAME, String.class))
                .setDateOfBirth(row.get(DATE_OF_BIRTH, String.class))
                .setHomeCountry(row.get(HOME_COUNTRY, String.class))
                .setGender(Gender.from(row.get(GENDER, String.class)))
                .setRole(row.get(ROLE, String.class))
                .createUser();
    }

    private ExternalUser toExternalUserModel(Readable row) {
        return new ExternalUser(
                UUID.fromString(row.get("ID", String.class)),
                row.get("EMAIl", String.class),
                row.get("LOCALE", String.class),
                row.get("PICTURE_URL", String.class),
                row.get(FIRST_NAME, String.class),
                row.get(LAST_NAME, String.class),
                row.get("TOKEN_VALUE", String.class),
                row.get("TOKEN_TYPE", String.class),
                row.get("TOKEN_EXPIRY_TIME", Long.class),
                row.get("EXTERNAL_SYSTEM", String.class),
                row.get(DATE_OF_BIRTH, String.class),
                Gender.from(row.get(GENDER, String.class)),
                row.get(HOME_COUNTRY, String.class));
    }

    private User toFullModel(Readable row) {
        return new User.UserBuilder()
                .setId(UUID.fromString(row.get("ID", String.class)))
                .setEmail(row.get(EMAIL, String.class))
                .setPassword(row.get(PASSWORD, String.class))
                .setSalt(row.get("SALT", String.class))
                .setFirstName(row.get(FIRST_NAME, String.class))
                .setLastName(row.get(LAST_NAME, String.class))
                .setDateOfBirth(row.get(DATE_OF_BIRTH, String.class))
                .setHomeCountry(row.get(HOME_COUNTRY, String.class))
                .setGender(Gender.from(row.get(GENDER, String.class)))
                .setRole(row.get(ROLE, String.class))
                .createUser();
    }
}
