package com.gw.user.e2e.test;

import com.gw.test.support.framework.DatabaseResponseSpec;
import com.gw.test.support.framework.HttpResponseSpec;
import com.gw.test.support.framework.ResponseStep;
import com.gw.test.support.framework.VoidStep;
import com.gw.user.domain.User;
import com.gw.user.e2e.domain.UserDetailsResponseDTO;
import com.gw.user.e2e.function.FetchUser;
import com.gw.user.e2e.function.Login;
import com.gw.user.e2e.function.Logout;
import com.gw.user.e2e.function.UserCreate;
import com.gw.user.resource.domain.*;
import org.assertj.core.api.Assertions;
import org.assertj.core.matcher.AssertionMatcher;
import org.awaitility.Awaitility;
import org.hamcrest.Matcher;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class ScenarioExecutor {
    public static ResponseStep aUserIsCreated(UserCreateRequestDTO userCreateRequestDTO) {
        return testContext -> {
            var responseSpec = new UserCreate().apply(testContext.getWebTestClient(), userCreateRequestDTO);
            return new HttpResponseSpec(UserCreateResponseDTO.class, responseSpec);
        };
    }

    public static ResponseStep userLogins(UserCreateRequestDTO userCreateRequestDTO) {
        return testContext -> {
            var responseSpec = new Login().apply(testContext.getWebTestClient(),
                    new LoginRequestDTO(userCreateRequestDTO.userName(), userCreateRequestDTO.password()));
            return new HttpResponseSpec(LoginResponseDTO.class, responseSpec);
        };
    }

    public static VoidStep aHttpResponse(Matcher<Integer> statusMatcher) {
        return testContext -> {
            if (testContext.getLastResponse() instanceof HttpResponseSpec httpResponseSpec) {
                httpResponseSpec.matchStatus(statusMatcher);
            } else {
                Assertions.fail("Invalid response type detected for Http Response");
            }
        };
    }

    public static VoidStep userTokenIsReceivedInResponse() {
        return testContext -> {
            var loginResponseDTO = testContext.getResponseOfType(LoginResponseDTO.class);
            assertThat(loginResponseDTO.token(), is(notNullValue()));
        };
    }

    public static ResponseStep whenUserIsRetrievedFromDatabaseWith(String userName) {
        return testContext -> {
            var reactiveMongoTemplate = testContext.getBeanOfType(ReactiveMongoTemplate.class);
            var databaseResponseSpec = reactiveMongoTemplate
                    .findOne(query(where("userName").is(userName)), User.class)
                    .map(UserDetailsResponseDTO::fromUser)
                    .map(udrdto -> new DatabaseResponseSpec<>(UserDetailsResponseDTO.class, udrdto))
                    .block();

            assertThat("Expected a user to exist in database", databaseResponseSpec.responseValue(), is(notNullValue()));
            return databaseResponseSpec;
        };
    }

    public static VoidStep userRetrievedFromDatabaseMatches(UserCreateRequestDTO userCreateRequestDTO) {
        return testContext -> {
            var userDetailsResponseDTO = testContext.getResponseOfType(UserDetailsResponseDTO.class);
            assertThat(userDetailsResponseDTO, is(notNullValue()));
            assertThat(userDetailsResponseDTO.userName(), is(equalTo(userCreateRequestDTO.userName())));
            assertThat(userDetailsResponseDTO.firstName(), is(equalTo(userCreateRequestDTO.firstName())));
            assertThat(userDetailsResponseDTO.lastName(), is(equalTo(userCreateRequestDTO.lastName())));
            assertThat(userDetailsResponseDTO.dateOfBirth(), is(equalTo(userCreateRequestDTO.dateOfBirth())));
        };
    }

    public static VoidStep userFetchedMatchesDetailsIn(UserCreateRequestDTO userCreateRequestDTO) {
        return testContext -> {
            var userDetailsFetchResponseDTO = testContext.getResponseOfType(UserDetailsFetchResponseDTO.class);
            assertThat(userDetailsFetchResponseDTO, is(notNullValue()));
            assertThat(userDetailsFetchResponseDTO.firstName(), is(equalTo(userCreateRequestDTO.firstName())));
            assertThat(userDetailsFetchResponseDTO.lastName(), is(equalTo(userCreateRequestDTO.lastName())));
            assertThat(userDetailsFetchResponseDTO.dateOfBirth(), is(equalTo(userCreateRequestDTO.dateOfBirth())));
        };
    }

    public static VoidStep associateLoginCredentials(String key) {
        return testContext -> {
            var loginResponseDTO = testContext.getResponseOfType(LoginResponseDTO.class);
            var userCreateResponseDTO = testContext.getResponseOfType(UserCreateResponseDTO.class);
            testContext.associateToken(key, userCreateResponseDTO.userId(), loginResponseDTO.token());
        };
    }

    public static VoidStep theLoginCacheShouldHaveTokenAssociate(String key) {
        return testContext -> {
            JedisPool jedisPool = testContext.getBeanOfType(JedisPool.class);
            Jedis resource = jedisPool.getResource();
            Assertions.assertThat(resource.hget("login:", testContext.getUserIdForCredentialKey(key)))
                    .isNotNull()
                    .isEqualTo(testContext.getTokenForCredentialKey(key));
            var loginResponseDTO = testContext.getResponseOfType(LoginResponseDTO.class);
            testContext.associateToken(key, loginResponseDTO.token(), loginResponseDTO.token());
        };
    }

    public static ResponseStep fetchUserDetails(String credentialKey) {
        return testContext -> {
            WebTestClient.ResponseSpec httpResponseSpec =
                    new FetchUser(testContext.getUserIdForCredentialKey(credentialKey), testContext.getTokenForCredentialKey(credentialKey)).apply(testContext.getWebTestClient());
            return new HttpResponseSpec(UserDetailsFetchResponseDTO.class, httpResponseSpec);
        };
    }

    public static VoidStep userLogsOut(String credentialKey) {
        return testContext ->
            new Logout(testContext.getTokenForCredentialKey(credentialKey)).apply(testContext.getWebTestClient());
    }

    public static VoidStep theInvalidationCacheShouldNOTHaveTokenAfterWaitTimeOf(Duration tokenInvalidationDuration, String credentialKey) {
        return testContext ->
            Awaitility.await().pollDelay(tokenInvalidationDuration)
                    .untilAsserted(() -> {
                        JedisPool jedisPool = testContext.getBeanOfType(JedisPool.class);
                        Jedis resource = jedisPool.getResource();
                        assertThat(resource.get("invalidated:" + testContext.getTokenForCredentialKey(credentialKey)), is(nullValue()));
                    });
    }

    public static VoidStep theLoginCacheShouldHaveCredentialsOf(String credentialKey) {
        return testContext -> {
            JedisPool jedisPool = testContext.getBeanOfType(JedisPool.class);
            Jedis resource = jedisPool.getResource();
            Assertions.assertThat(resource.hget("login:", testContext.getUserIdForCredentialKey(credentialKey)))
                    .isNotNull()
                    .isEqualTo(testContext.getTokenForCredentialKey(credentialKey));
        };
    }

    public static VoidStep theLoginCacheShouldNOTHaveCredentialsOf(String credentialKey) {
        return testContext -> {
            JedisPool jedisPool = testContext.getBeanOfType(JedisPool.class);
                        Jedis resource = jedisPool.getResource();
            Assertions.assertThat(resource.hget("login:", testContext.getUserIdForCredentialKey(credentialKey)))
                    .isNotEqualTo(testContext.getTokenForCredentialKey(credentialKey));
        };
    }

    public static VoidStep theInvalidationCacheShouldHaveCredentialsOf(String credentialKey) {
        return testContext -> {
            JedisPool jedisPool = testContext.getBeanOfType(JedisPool.class);
                        Jedis resource = jedisPool.getResource();
            Assertions.assertThat(resource.get("invalidated:" + testContext.getTokenForCredentialKey(credentialKey)))
                    .isNotNull()
                    .isEqualTo(testContext.getUserIdForCredentialKey(credentialKey));
        };
    }

    public static Matcher<Integer> withStatus(int statusCode) {
        return new AssertionMatcher<>() {
            @Override
            public void assertion(Integer actual) throws AssertionError {
                Assertions.assertThat(actual).isEqualTo(statusCode);
            }
        };
    }
}
