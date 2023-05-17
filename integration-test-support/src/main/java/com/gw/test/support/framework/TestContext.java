package com.gw.test.support.framework;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
    private final WebTestClient webTestClient;
    private final DatabaseClient dataSource;
    private final JedisPool jedisPool;
    Map<String, Object> responseMap = new HashMap<>();
    TestResponse lastResponse;
    private final Map<String, LoginCredentials> tokenMap = new HashMap<>();

    public TestContext(WebTestClient webTestClient, DatabaseClient dataSource, JedisPool jedisPool) {

        this.webTestClient = webTestClient;
        this.dataSource = dataSource;
        this.jedisPool = jedisPool;
    }

    public void mapResponse(TestResponse response) {
        lastResponse = response;
        responseMap.put(response.responseKey(), response.responseValue());
    }

    public TestResponse getLastResponse() {
        return lastResponse;
    }

    public WebTestClient getWebTestClient() {
        return webTestClient;
    }

    public DatabaseClient getDataSource() {
        return dataSource;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public <V> V getResponseOfType(Class<V> clazz) {
        return (V)responseMap.get(clazz.getSimpleName());
    }

    public void associateToken(String key, String userId, String token) {
        this.tokenMap.put(key, new LoginCredentials(userId, token));
    }

    public String getTokenForCredentialKey(String key) {
        return tokenMap.get(key).token();
    }

    public String getUserIdForCredentialKey(String credentialKey) {
        return tokenMap.get(credentialKey).userId();
    }

    private record LoginCredentials(String userId, String token) {}
}
