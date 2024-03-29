package com.gw.user.repo;

import com.gw.user.domain.ExternalUser;
import com.gw.user.domain.ExternalUserBuilder;
import com.gw.user.domain.User;
import com.gw.user.testutils.DatabaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.gw.user.repo.MongoDBTestUtils.*;
import static com.gw.user.testutils.TestUserBuilder.aUser;

@ActiveProfiles("UserMongoRepositoryTest")
@ContextConfiguration(initializers = TestContainerDatabaseInitializer.class)
@SpringBootTest(classes = {UserRepository.class, UserMongoRepositoryImpl.class})
@ExtendWith(MockitoExtension.class)
class UserMongoRepositoryImplTest extends DatabaseTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @BeforeEach
    void setUp() {
        clearDatabase(reactiveMongoTemplate);
    }

    @Test
    void shouldFindUserById() {
        //given
        User userInDatabase = aUser().build();
        addToDatabase(userInDatabase, reactiveMongoTemplate);
        //when
        Mono<User> actualUser = userRepository.findUserById(userInDatabase.userId());

        //then
        StepVerifier.create(actualUser)
                .expectNext(userInDatabase)
                .verifyComplete();
    }

    @Test
    void shouldAddUser() {
        //given
        User userInDatabase = aUser().build();

        //when
        StepVerifier.create(userRepository.addUser(userInDatabase))
                .verifyComplete();

        //then
        Mono<User> userFromDB = getUser(userInDatabase.userId(), reactiveMongoTemplate);
        StepVerifier.create(userFromDB)
                .expectNext(userInDatabase)
                .verifyComplete();
    }

    @Test
    void shouldAddExternalUser() {
        //given
        ExternalUser userToAdd = ExternalUserBuilder.newBuilder()
                .withUserName("some-user-name")
                .build();

        //when
        StepVerifier.create(userRepository.addExternalUser(userToAdd))
                .verifyComplete();

        //then
        Mono<ExternalUser> userFromDB = getExternalUser2("some-user-name", reactiveMongoTemplate);
        StepVerifier.create(userFromDB)
                .expectNext(userToAdd)
                .verifyComplete();
    }

    @Test
    void shouldFindUserByName() {
        //given
        User userInDatabase = aUser().build();
        addToDatabase(userInDatabase, reactiveMongoTemplate);

        //when
        Mono<User> actualUser = userRepository.findUserByUserName(userInDatabase.userName());

        //then
        StepVerifier.create(actualUser)
                .expectNext(userInDatabase)
                .verifyComplete();
    }

    @Test
    void shouldFindExternalUser() {
        //given
        ExternalUser userInDatabase = ExternalUserBuilder.newBuilder().build();
        addToDatabase(userInDatabase, reactiveMongoTemplate);

        //when
        Mono<ExternalUser> actualUser = userRepository.findExternalUserByUserName(userInDatabase.userName());

        //then
        StepVerifier.create(actualUser)
                .expectNext(userInDatabase)
                .verifyComplete();
    }

    @Test
    void shouldCreateExternalUserIfDoesNotExist() {
        //given
        ExternalUser userToSave = ExternalUserBuilder.newBuilder()
                .withExternalSystem("google")
                .withUserName("some-email@email.com")
                .build();

        //when
        Mono<ExternalUser> userInDB = userRepository.findOrCreateExternalUser(userToSave);

        //then
        StepVerifier.create(userInDB)
                .expectNext(userToSave)
                .verifyComplete();
    }

    @Test
    void shouldFindExternalUserIfAlreadyExist() {
        //given
        ExternalUser userToSave = ExternalUserBuilder.newBuilder()
                .withExternalSystem("google")
                .withUserName("some-email@email.com")
                .build();
        MongoDBTestUtils.addToDatabase(userToSave, reactiveMongoTemplate);

        //when
        Mono<ExternalUser> userInDB = userRepository.findOrCreateExternalUser(userToSave);

        //then
        StepVerifier.create(userInDB)
                .expectNext(userToSave)
                .verifyComplete();
    }
}