package com.gw.user.repo;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.Gender;
import com.gw.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static com.gw.user.domain.User.aUser;

@Component("datasource")
public class UserDataSourceRepositoryImpl implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(UserDataSourceRepositoryImpl.class);
    private final DataSource dataSource;
    private static final String FIND_USER_BY_ID = "SELECT * FROM USER_SCHEMA.USER_TABLE WHERE ID = ?";
    private static final String FIND_USER_BY_EMAIL = "SELECT * FROM USER_SCHEMA.USER_TABLE WHERE EMAIL = ?";

    private static final String FIND_EXTERNAL_USER_BY_USER_NAME = "SELECT * FROM USER_SCHEMA.EXTERNAL_USER_TABLE WHERE EMAIL = ?";
    private static final String ADD_USER = "INSERT INTO USER_SCHEMA.USER_TABLE (ID, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, SALT, DATE_OF_BIRTH, GENDER, HOME_COUNTRY, ROLE) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String ADD_EXTERNAL_USER = "INSERT INTO USER_SCHEMA.EXTERNAL_USER_TABLE (ID, EMAIL, FIRST_NAME, LAST_NAME, LOCALE, " +
            "PICTURE_URL, TOKEN_VALUE, TOKEN_TYPE, TOKEN_EXPIRY_TIME, EXTERNAL_SYSTEM, GENDER, DATE_OF_BIRTH, HOME_COUNTRY) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String DATE_OF_BIRTH = "DATE_OF_BIRTH";
    private static final String HOME_COUNTRY = "HOME_COUNTRY";
    private static final String GENDER = "GENDER";
    private static final String ROLE = "ROLE";

    public UserDataSourceRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Mono<User> findUserById(UUID id) {
        return Mono.create(sink -> {
            try (var connection = dataSource.getConnection();
                 var preparedStatement = connection.prepareStatement(FIND_USER_BY_ID)) {
                preparedStatement.setString(1, id.toString());

                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    sink.success(toUserModel(resultSet));
                } else {
                    sink.success();
                }
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
                sink.error(new RuntimeException(e));
            }
        });
    }

    @Override
    public Mono<Void> addUser(User user, String password, String salt) {
        return Mono.create(sink -> {
            try (var connection = dataSource.getConnection();
                 var preparedStatement = connection.prepareStatement(ADD_USER)) {

                preparedStatement.setString(1, user.id());
                preparedStatement.setString(2, user.name());
                preparedStatement.setString(3, user.firstName());
                preparedStatement.setString(4, user.lastName());
                preparedStatement.setString(5, password);
                preparedStatement.setString(6, salt);
                preparedStatement.setString(7, user.dateOfBirth());
                preparedStatement.setString(8, user.gender().name());
                preparedStatement.setString(9, user.homeCountry());
                preparedStatement.setString(10, user.role());
                preparedStatement.execute();
                sink.success();
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
                sink.error(new RuntimeException(e));
            }
        });
    }

    @Override
    public Mono<User> findUserByUserName(String email) {
        return Mono.create(sink -> {
            try (var connection = dataSource.getConnection();
                 var preparedStatement = connection.prepareStatement(FIND_USER_BY_EMAIL)) {
                preparedStatement.setString(1, email);

                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    sink.success(toFullModel(resultSet));
                } else {
                    sink.success();
                }

            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
                sink.error(new RuntimeException(e));
            }
        });
    }

    @Override
    public Mono<Void> addExternalUser(ExternalUser user) {
        return Mono.create(sink -> {
            try (var connection = dataSource.getConnection();
                 var preparedStatement = connection.prepareStatement(ADD_EXTERNAL_USER)) {

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

                sink.success();
            } catch (SQLException e) {
                sink.error(new RuntimeException(e));
            }
        });
    }

    @Override
    public Mono<ExternalUser> findExternalUserByUserName(String email) {
        return Mono.create(sink -> {
            try (var connection = dataSource.getConnection();
                 var preparedStatement = connection.prepareStatement(FIND_EXTERNAL_USER_BY_USER_NAME)) {

                preparedStatement.setString(1, email);

                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    sink.success(toExternalUserModel(resultSet));
                } else {
                    sink.success();
                }
            } catch (SQLException e) {
                sink.error(new RuntimeException(e));
            }
        });
    }

    private User toUserModel(ResultSet row) throws SQLException {
        return aUser()
                .withId(UUID.fromString(row.getString("ID")))
                .withUserName(row.getString(EMAIL))
                .withFirstName(row.getString(FIRST_NAME))
                .withLastName(row.getString(LAST_NAME))
                .withDateOfBirth(row.getString(DATE_OF_BIRTH))
                .withHomeCountry(row.getString(HOME_COUNTRY))
                .withGender(Gender.from(row.getString(GENDER)))
                .withRole(row.getString(ROLE))
                .build();
    }

    private User toFullModel(ResultSet resultSet) throws SQLException {
        return aUser()
                .withId(UUID.fromString(resultSet.getString("ID")))
                .withUserName(resultSet.getString(EMAIL))
                .withPassword(resultSet.getString(PASSWORD))
                .withSalt(resultSet.getString("SALT"))
                .withFirstName(resultSet.getString(FIRST_NAME))
                .withLastName(resultSet.getString(LAST_NAME))
                .withDateOfBirth(resultSet.getString(DATE_OF_BIRTH))
                .withHomeCountry(resultSet.getString(HOME_COUNTRY))
                .withGender(Gender.from(resultSet.getString(GENDER)))
                .withRole(resultSet.getString(ROLE))
                .build();
    }

    private ExternalUser toExternalUserModel(ResultSet row) throws SQLException {
        return new ExternalUser(
                UUID.fromString(row.getString("ID")),
                row.getString("EMAIl"),
                row.getString("LOCALE"),
                row.getString("PICTURE_URL"),
                row.getString(FIRST_NAME),
                row.getString(LAST_NAME),
                row.getString("TOKEN_VALUE"),
                row.getString("TOKEN_TYPE"),
                row.getLong("TOKEN_EXPIRY_TIME"),
                row.getString("EXTERNAL_SYSTEM"),
                row.getString(DATE_OF_BIRTH),
                Gender.from(row.getString(GENDER)),
                row.getString(HOME_COUNTRY));
    }
}
