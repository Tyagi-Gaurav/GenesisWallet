package com.gw.test.support.framework;

import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
    private final WebTestClient webTestClient;
    private final ApplicationContext applicationContext;
    Map<String, Object> responseMap = new HashMap<>();
    TestResponse lastResponse;
    private final Map<String, LoginCredentials> tokenMap = new HashMap<>();

    public TestContext(WebTestClient webTestClient, ApplicationContext applicationContext) {
        this.webTestClient = webTestClient;
        this.applicationContext = applicationContext;
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

    public <T> T getBeanOfType(Class<T> tClass) {
        return applicationContext.getBean(tClass);
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
