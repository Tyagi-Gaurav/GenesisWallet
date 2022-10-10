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

    public UserRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<User> findUserById(UUID id) {
        return databaseClient.sql("SELECT * FROM USER_SCHEMA.USER_TABLE WHERE ID = :id")
                .bind("id", id.toString())
                .map(this::toModel)
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
}
