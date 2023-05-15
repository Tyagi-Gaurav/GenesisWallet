package com.gw.user.e2e;

import com.gw.user.e2e.function.AccessStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class ScenarioExecutor {
    private WebTestClient.ResponseSpec responseSpec;
    private final Map<Class, Object> responses = new HashMap<>();
    private final WebTestClient webTestClient;
    private final DatabaseClient databaseClient;
    private final JedisPool jedisPool;
    private final Map<String, String> tokenMap = new HashMap<>();

    public ScenarioExecutor(WebTestClient webTestClient, DatabaseClient databaseClient, JedisPool jedisPool) {
        this.webTestClient = webTestClient;
        this.databaseClient = databaseClient;
        this.jedisPool = jedisPool;
    }

    public ScenarioExecutor when() {
        return this;
    }

    public ScenarioExecutor then() {
        return this;
    }

    public ScenarioExecutor expectReturnCode(int expectedStatus) {
        responseSpec.expectStatus().isEqualTo(expectedStatus);
        return this;
    }

    private <T> T getResponseOfType(Class<T> clazz) {
        T response = (T) responses.get(clazz);
        return response;
    }

    public ScenarioExecutor accessStatusEndpoint() {
        this.responseSpec = new AccessStatus().apply(webTestClient);
        return this;
    }
}
