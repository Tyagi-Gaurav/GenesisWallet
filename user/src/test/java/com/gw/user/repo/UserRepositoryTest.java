package com.gw.user.repo;

import com.gw.common.domain.User;
import com.gw.user.testutils.DatabaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.SQLException;

import static com.gw.user.repo.DBTestUtils.*;
import static com.gw.user.testutils.UserBuilder.aUser;
import static com.gw.user.testutils.UserBuilder.copyOf;

@ActiveProfiles("UserRepositoryTest")
@ContextConfiguration(initializers = TestContainerDatabaseInitializer.class)
@SpringBootTest(classes = {UserRepository.class, UserRepositoryImpl.class})
@ExtendWith(MockitoExtension.class)
class UserRepositoryTest extends DatabaseTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() throws SQLException {
        clearDatabase(databaseClient);
    }

    @Test
    void shouldFindUserById() {
        //given
        User userInDatabase = aUser().build();
        addToDatabase(userInDatabase, databaseClient);

        User expectedUser = copyOf(userInDatabase).withPassword(null).build();

        //when
        Mono<User> actualUser = userRepository.findUserById(userInDatabase.id());

        //then
        StepVerifier.create(actualUser)
                .expectNext(expectedUser)
                .verifyComplete();
    }

    @Test
    void shouldAddUser() {
        //given
        User userToAdd = aUser().build();
        User expectedUser = copyOf(userToAdd).withPassword(null).build();

        //when
        StepVerifier.create(userRepository.addUser(userToAdd))
                .verifyComplete();

        //then
        Mono<User> userFromDB = getUser(userToAdd.id(), databaseClient);
        StepVerifier.create(userFromDB)
                .expectNext(expectedUser)
                .verifyComplete();
    }

    @Test
    void shouldFindUserByName() {
        //given
        User userInDatabase = aUser().build();
        addToDatabase(userInDatabase, databaseClient);

        //when
        Mono<User> actualUser = userRepository.findUserByName(userInDatabase.username());

        //then
        StepVerifier.create(actualUser)
                .expectNext(userInDatabase)
                .verifyComplete();
    }

    @Test
    void shouldFindUserByName_WhenUserDoesNoExist() {
        //given
        User userInDatabase = aUser().build();
        addToDatabase(userInDatabase, databaseClient);

        //when
        Mono<User> actualUser = userRepository.findUserByName(userInDatabase.username());

        //then
        StepVerifier.create(actualUser)
                .expectNext(userInDatabase)
                .verifyComplete();
    }
}