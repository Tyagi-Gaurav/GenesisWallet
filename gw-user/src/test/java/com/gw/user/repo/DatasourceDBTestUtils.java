package com.gw.user.repo;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.Gender;
import com.gw.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static com.gw.user.domain.User.aUser;

public class DatasourceDBTestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DatasourceDBTestUtils.class);

    private static final String DELETE_ALL_USERS = "DELETE FROM USER_SCHEMA.USER_TABLE";
    private static final String DELETE_ALL_EXTERNAL_USERS = "DELETE FROM USER_SCHEMA.EXTERNAL_USER_TABLE";

    public static void addToDatabase(User user, DataSource dataSource) {
        LOG.info("Adding user {}: ", user);
        String query = "INSERT INTO USER_SCHEMA.USER_TABLE (ID, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, SALT, DATE_OF_BIRTH, GENDER, HOME_COUNTRY, ROLE) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var connections = dataSource.getConnection();
             var preparedStatement = connections.prepareStatement(query)) {
            preparedStatement.setString(1, user.id().toString());
            preparedStatement.setString(2, user.name());
            preparedStatement.setString(3, user.firstName());
            preparedStatement.setString(4, user.lastName());
            preparedStatement.setString(5, user.password());
            preparedStatement.setString(6, user.salt());
            preparedStatement.setString(7, user.dateOfBirth());
            preparedStatement.setString(8, user.gender().name());
            preparedStatement.setString(9, user.homeCountry());
            preparedStatement.setString(10, user.role());

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addToDatabase(ExternalUser user, DataSource dataSource) {
        LOG.info("Adding external user {}: ", user);
        String query = "INSERT INTO USER_SCHEMA.EXTERNAL_USER_TABLE (ID, EMAIL, FIRST_NAME, LAST_NAME, LOCALE, " +
                "PICTURE_URL, TOKEN_VALUE, TOKEN_TYPE, TOKEN_EXPIRY_TIME, EXTERNAL_SYSTEM, GENDER, DATE_OF_BIRTH, HOME_COUNTRY) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (var connections = dataSource.getConnection();
             var preparedStatement = connections.prepareStatement(query)) {
            preparedStatement.setString(1, user.id().toString());
            preparedStatement.setString(2, user.email());
            preparedStatement.setString(3, user.firstName());
            preparedStatement.setString(4, user.lastName());
            preparedStatement.setString(5, user.locale());
            preparedStatement.setString(6, user.pictureUrl());
            preparedStatement.setString(7, user.tokenValue());
            preparedStatement.setString(8, user.tokenType());
            preparedStatement.setLong(9, user.tokenExpiryTime());
            preparedStatement.setString(10, user.externalSystem());
            preparedStatement.setString(11, user.gender().name());
            preparedStatement.setString(12, user.dateOfBirth());
            preparedStatement.setString(13, user.homeCountry());

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("External User added with Id {}", user.id());
        }
    }

    public static Mono<User> getUser(UUID userId, DataSource dataSource) {
        LOG.info("Fetching user Id: {} from user: ", userId);
        String query = "SELECT * FROM USER_SCHEMA.USER_TABLE WHERE ID = ?";

        return Mono.create(sink -> {
            try (var connections = dataSource.getConnection();
                 var preparedStatement = connections.prepareStatement(query)) {

                preparedStatement.setString(1, userId.toString());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    sink.success(DatasourceDBTestUtils.toUserModel(resultSet));
                } else {
                    sink.success();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Mono<ExternalUser> getExternalUser(UUID userId, DataSource dataSource) {
        LOG.info("Fetching external user Id: {} from user: ", userId);
        String query = "SELECT * FROM USER_SCHEMA.EXTERNAL_USER_TABLE WHERE ID = ?";

        return Mono.create(sink -> {
            try (var connections = dataSource.getConnection();
                 var preparedStatement = connections.prepareStatement(query)) {

                preparedStatement.setString(1, userId.toString());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    sink.success(DatasourceDBTestUtils.toExternalUserModel(resultSet));
                } else {
                    sink.success();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static User toUserModel(ResultSet resultSet) throws SQLException {
        return aUser()
                .withId(UUID.fromString(resultSet.getString("ID")))
                .withUserName(resultSet.getString("EMAIL"))
                .withFirstName(resultSet.getString("FIRST_NAME"))
                .withLastName(resultSet.getString("LAST_NAME"))
                .withDateOfBirth(resultSet.getString("DATE_OF_BIRTH"))
                .withPassword(resultSet.getString("PASSWORD"))
                .withSalt(resultSet.getString("SALT"))
                .withHomeCountry(resultSet.getString("HOME_COUNTRY"))
                .withGender(Gender.valueOf(resultSet.getString("GENDER")))
                .withRole(resultSet.getString("ROLE"))
                .build();
    }

    private static ExternalUser toExternalUserModel(ResultSet row) throws SQLException {
        return new ExternalUser(
                UUID.fromString(row.getString("ID")),
                row.getString("EMAIL"),
                row.getString("LOCALE"),
                row.getString("PICTURE_URL"),
                row.getString("FIRST_NAME"),
                row.getString("LAST_NAME"),
                row.getString("TOKEN_VALUE"),
                row.getString("TOKEN_TYPE"),
                row.getLong("TOKEN_EXPIRY_TIME"),
                row.getString("EXTERNAL_SYSTEM"),
                row.getString("DATE_OF_BIRTH"),
                Gender.from(row.getString("GENDER")),
                row.getString("HOME_COUNTRY"));
    }

    public static void clearDatabase(DataSource dataSource) {
        try (var connections = dataSource.getConnection();
             var preparedStatement = connections.prepareStatement(DELETE_ALL_USERS)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (var connections = dataSource.getConnection();
             var preparedStatement = connections.prepareStatement(DELETE_ALL_EXTERNAL_USERS)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
