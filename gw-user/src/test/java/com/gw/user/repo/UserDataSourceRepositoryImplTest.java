package com.gw.user.repo;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.User;
import com.gw.user.testutils.DatabaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.sql.DataSource;

import static com.gw.user.repo.DatasourceDBTestUtils.*;
import static com.gw.user.testutils.ExternalUserBuilder.aExternalUser;
import static com.gw.user.testutils.UserBuilder.aUser;
import static com.gw.user.testutils.UserBuilder.copyOf;

@ActiveProfiles("UserRepositoryTest")
@ContextConfiguration(initializers = TestContainerDatabaseInitializer.class)
@SpringBootTest(classes = {UserRepository.class, UserRepositoryImpl.class})
@ExtendWith(MockitoExtension.class)
class UserDataSourceRepositoryImplTest extends DatabaseTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        clearDatabase(dataSource);
    }

    @Test
    void shouldFindUserById() {
        //given
        User userInDatabase = aUser().build();
        addToDatabase(userInDatabase, dataSource);

        User expectedUser = copyOf(userInDatabase)
                .withPassword(null)
                .withSalt(null)
                .build();

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
        User expectedUser = copyOf(userToAdd).build();

        //when
        StepVerifier.create(userRepository.addUser(userToAdd,
                        userToAdd.password(),
                        userToAdd.salt()))
                .verifyComplete();

        //then
        Mono<User> userFromDB = getUser(userToAdd.id(), dataSource);
        StepVerifier.create(userFromDB)
                .expectNext(expectedUser)
                .verifyComplete();
    }

    @Test
    void shouldAddExternalUser() {
        //given
        ExternalUser userToAdd = aExternalUser().build();

        //when
        StepVerifier.create(userRepository.addExternalUser(userToAdd))
                .verifyComplete();

        //then
        Mono<ExternalUser> userFromDB = getExternalUser(userToAdd.id(), dataSource);
        StepVerifier.create(userFromDB)
                .expectNext(userToAdd)
                .verifyComplete();
    }

    @Test
    void shouldFindUserByName() {
        //given
        User userInDatabase = aUser().build();
        addToDatabase(userInDatabase, dataSource);

        //when
        Mono<User> actualUser = userRepository.findUserByEmail(userInDatabase.email());

        //then
        StepVerifier.create(actualUser)
                .expectNext(userInDatabase)
                .verifyComplete();
    }

    @Test
    void shouldFindExternalUser() {
        //given
        ExternalUser userInDatabase = aExternalUser().build();
        addToDatabase(userInDatabase, dataSource);

        //when
        Mono<ExternalUser> actualUser = userRepository.findExternalUserByEmail(userInDatabase.email());

        //then
        StepVerifier.create(actualUser)
                .expectNext(userInDatabase)
                .verifyComplete();
    }
}