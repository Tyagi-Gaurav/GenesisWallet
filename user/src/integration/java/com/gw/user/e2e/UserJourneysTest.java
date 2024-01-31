package com.gw.user.e2e;

import com.gw.test.support.framework.WithSyntacticSugar;
import com.gw.user.Application;
import com.gw.user.e2e.builder.UserCreateRequestBuilder;
import com.gw.user.e2e.security.TestContainerVaultInitializer;
import com.gw.user.repo.TestContainerDatabaseInitializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;

import java.time.Duration;

import static com.gw.test.support.ScenarioBuilder.aScenarioUsing;
import static com.gw.user.e2e.test.ScenarioExecutor.*;

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
class UserJourneysTest implements WithSyntacticSugar {
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

    @Disabled
    void postLogoutUserShouldNotBeAbleToAccessUserDetails() {
        var userCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest().build();

        aScenarioUsing(applicationContext)
                .given(aUserIsCreated(with(userCreateRequestDTO)))
                .and(aHttpResponse(isReceived(withStatus(201))))
                .and(userLogins(using(userCreateRequestDTO)))
                .and(aHttpResponse(isReceived(withStatus(200))))
                .and(associateLoginCredentials(with("credentialsA")))
                .when(userLogsOut(using("credentialsA")))
                .then(aHttpResponse(isReceived(withStatus(200))))
                .and(fetchUserDetails(using("credentialsA")))
                .then(aHttpResponse(isReceived(withStatus(401))))
                .execute();
    }
}
