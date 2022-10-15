package com.gw.user.repo;

import com.gw.common.domain.Gender;
import com.gw.common.domain.User;
import io.r2dbc.spi.Row;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final DatabaseClient databaseClient;
    private static String FIND_USER_BY_ID = "SELECT * FROM USER_SCHEMA.USER_TABLE WHERE ID = :id";
    private static String FIND_USER_BY_USER_NAME = "SELECT * FROM USER_SCHEMA.USER_TABLE WHERE USER_NAME = :username";
    private static String ADD_USER = "INSERT INTO USER_SCHEMA.USER_TABLE (ID, USER_NAME, FIRST_NAME, LAST_NAME, PASSWORD, DATE_OF_BIRTH, GENDER, HOME_COUNTRY, ROLE) " +
            "values ($1, $2, $3, $4, $5, $6, $7, $8, $9)";

    public UserRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<User> findUserById(UUID id) {
        return databaseClient.sql(FIND_USER_BY_ID)
                .bind("id", id.toString())
                .map(this::toModel)
                .one();
    }

    @Override
    public Mono<Void> addUser(User user) {
        return databaseClient.sql(ADD_USER)
                .bind(0, user.id().toString())
                .bind(1, user.username())
                .bind(2, user.firstName())
                .bind(3, user.lastName())
                .bind(4, user.password())
                .bind(5, user.dateOfBirth())
                .bind(6, user.gender().name())
                .bind(7, user.homeCountry())
                .bind(8, user.role())
                .then();
    }

    @Override
    public Mono<User> findUserByName(String username) {
        return databaseClient.sql(FIND_USER_BY_USER_NAME)
                .bind("username", username)
                .map(this::toFullModel)
                .one();
    }

    private User toModel(Row row) {
        return new User.UserBuilder()
                .setId(UUID.fromString(row.get("ID", String.class)))
                .setUsername(row.get("USER_NAME", String.class))
                .setFirstName(row.get("FIRST_NAME", String.class))
                .setLastName(row.get("LAST_NAME", String.class))
                .setDateOfBirth(row.get("DATE_OF_BIRTH", String.class))
                .setHomeCountry(row.get("HOME_COUNTRY", String.class))
                .setGender(Gender.valueOf(row.get("GENDER", String.class)))
                .setRole(row.get("ROLE", String.class))
                .createUser();
    }

    private User toFullModel(Row row) {
        return new User.UserBuilder()
                .setId(UUID.fromString(row.get("ID", String.class)))
                .setUsername(row.get("USER_NAME", String.class))
                .setPassword(row.get("PASSWORD", String.class))
                .setFirstName(row.get("FIRST_NAME", String.class))
                .setLastName(row.get("LAST_NAME", String.class))
                .setDateOfBirth(row.get("DATE_OF_BIRTH", String.class))
                .setHomeCountry(row.get("HOME_COUNTRY", String.class))
                .setGender(Gender.valueOf(row.get("GENDER", String.class)))
                .setRole(row.get("ROLE", String.class))
                .createUser();
    }
}
