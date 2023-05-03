package com.gw.user.e2e;

import com.gw.user.Application;
import com.gw.user.e2e.builder.UserCreateRequestBuilder;
import com.gw.user.e2e.security.TestContainerVaultInitializer;
import com.gw.user.repo.TestContainerDatabaseInitializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.ApplicationContext;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;

import java.time.Duration;

import static com.gw.user.e2e.test.ScenarioBuilder.aScenarioUsing;
import static com.gw.user.e2e.test.ScenarioExecutor2.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {
        TestContainerDatabaseInitializer.class,
        TestContainerVaultInitializer.class})
@AutoConfigureWebFlux
@ActiveProfiles("UserJourneysTest")
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "user.host=localhost",
        "user.port=${wiremock.server.port}",
        "auth.tokenDuration=2s"
})
class UserJourneysTest {
    @Autowired
    private DatabaseClient databaseClient;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private ApplicationContext applicationContext;

    private static RedisServer redisServer;

    @BeforeAll
    static void beforeAll() {
        redisServer = new RedisServer();
        redisServer.start();
    }

    @AfterAll
    static void afterAll() {
        redisServer.stop();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "abc", "efuusidhfauihsdfuhiusdhfaiuhsfiuhiufhs"})
    void createUserWithInvalidUser(String userName) {
        var userCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest()
                .withUserName(userName)
                .build();

        aScenarioUsing(applicationContext)
                .given(aUserIsCreatedWithTheDetails(userCreateRequestDTO))
                .then(aHttpResponseIsReceived(withStatus(400)))
                .execute();
    }

    @Test
    void createValidUserTest() {
        var userCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest()
                .build();

        aScenarioUsing(applicationContext)
                .given(aUserIsCreatedWithTheDetails(userCreateRequestDTO))
                .then(aHttpResponseIsReceived(withStatus(201)))
                .and(whenUserIsRetrievedFromDatabaseWith(userCreateRequestDTO.userName()))
                .then(userRetrievedFromDatabaseMatches(userCreateRequestDTO))
                .execute();
    }

    @Test
    void createValidUserAndLogin() {
        var userCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest().build();

        aScenarioUsing(applicationContext)
                .given(aUserIsCreatedWithTheDetails(userCreateRequestDTO))
                .then(aHttpResponseIsReceived(withStatus(201)))
                .and(userLoginsUsing(userCreateRequestDTO))
                .then(aHttpResponseIsReceived(withStatus(200)))
                .and(userTokenIsReceivedInResponse())
                .execute();
    }

    @Test
    void AfterLoginTheUserTokenShouldBeInTheCache() {
        var userCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest().build();

        aScenarioUsing(applicationContext)
                .given(aUserIsCreatedWithTheDetails(userCreateRequestDTO))
                .then(aHttpResponseIsReceived(withStatus(201)))
                .and(userLoginsUsing(userCreateRequestDTO))
                .then(aHttpResponseIsReceived(withStatus(200)))
                .and(userTokenIsReceivedInResponse())
                .when(associateLoginCredentialsWithKey("credentialsA"))
                .then(theLoginCacheShouldHaveTokenAssociateWithKey("credentialsA"))
                .execute();
    }

    @Test
    void fetchUserDetailsAfterLogin() {
        var userCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest().build();

        aScenarioUsing(applicationContext)
                .given(aUserIsCreatedWithTheDetails(userCreateRequestDTO))
                .then(aHttpResponseIsReceived(withStatus(201)))
                .and(userLoginsUsing(userCreateRequestDTO))
                .and(aHttpResponseIsReceived(withStatus(200)))
                .and(associateLoginCredentialsWithKey("credentialsA"))
                .when(fetchUserDetailUsingCredentialKey("credentialsA"))
                .and(aHttpResponseIsReceived(withStatus(200)))
                .then(userFetchedMatchesDetailsIn(userCreateRequestDTO))
                .execute();
    }

    @Test
    void invalidatedTokenShouldBeRemovedAfterTTLExpires() {
        var userCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest().build();

        aScenarioUsing(applicationContext)
                .given(aUserIsCreatedWithTheDetails(userCreateRequestDTO))
                .then(aHttpResponseIsReceived(withStatus(201)))
                .and(userLoginsUsing(userCreateRequestDTO))
                .and(aHttpResponseIsReceived(withStatus(200)))
                .and(associateLoginCredentialsWithKey("credentialsA"))
                .and(userLogsOutUsingCredentialKey("credentialsA"))
                .when(fetchUserDetailUsingCredentialKey("credentialsA"))
                .then(aHttpResponseIsReceived(withStatus(401)))
                .and(theInvalidationCacheShouldNOTHaveTokenAfterWaitTimeOf(Duration.ofSeconds(3),"credentialsA"))
                .execute();
    }

    @Test
    void previousUserTokenToBeInvalidatedAfterReLogin() {
        var userCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest().build();

        aScenarioUsing(applicationContext)
                .given(aUserIsCreatedWithTheDetails(userCreateRequestDTO))
                .then(aHttpResponseIsReceived(withStatus(201)))
                .and(userLoginsUsing(userCreateRequestDTO))
                .and(aHttpResponseIsReceived(withStatus(200)))
                .and(associateLoginCredentialsWithKey("credentialsA"))
                .when(fetchUserDetailUsingCredentialKey("credentialsA"))
                .then(aHttpResponseIsReceived(withStatus(200)))
                .and(userLoginsUsing(userCreateRequestDTO))
                .then(aHttpResponseIsReceived(withStatus(200)))
                .and(associateLoginCredentialsWithKey("credentialsB"))
                .when(fetchUserDetailUsingCredentialKey("credentialsA"))
                .then(aHttpResponseIsReceived(withStatus(401)))
                .and(theLoginCacheShouldHaveCredentialsOf("credentialsB"))
                .and(theLoginCacheShouldNOTHaveCredentialsOf("credentialsA"))
                .and(theInvalidationCacheShouldHaveCredentialsOf("credentialsA"))
                .execute();
    }

    @Test
    void postLogoutUserShouldNotBeAbleToAccessUserDetails() {
        var userCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest().build();

        aScenarioUsing(applicationContext)
                .given(aUserIsCreatedWithTheDetails(userCreateRequestDTO))
                .and(aHttpResponseIsReceived(withStatus(201)))
                .and(userLoginsUsing(userCreateRequestDTO))
                .and(aHttpResponseIsReceived(withStatus(200)))
                .and(associateLoginCredentialsWithKey("credentialsA"))
                .when(userLogsOutUsingCredentialKey("credentialsA"))
                .then(aHttpResponseIsReceived(withStatus(200)))
                .and(fetchUserDetailUsingCredentialKey("credentialsA"))
                .then(aHttpResponseIsReceived(withStatus(401)))
                .execute();
    }
}
