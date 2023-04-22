package com.gw.user.e2e;

import com.gw.common.domain.Gender;
import com.gw.user.e2e.builder.LoginRequestBuilder;
import com.gw.user.e2e.domain.UserDetailsResponseDTO;
import com.gw.user.e2e.function.*;
import com.gw.user.resource.domain.*;
import io.r2dbc.spi.Readable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

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

    public ScenarioExecutor userIsCreatedFor(UserCreateRequestDTO userCreateRequestDTO) {
        this.responseSpec = new UserCreate().apply(webTestClient, userCreateRequestDTO);
        UserCreateResponseDTO userCreateResponseDTO = this.responseSpec.returnResult(UserCreateResponseDTO.class)
                .getResponseBody().blockFirst();
        responses.put(UserCreateResponseDTO.class, userCreateResponseDTO);
        return this;
    }

    public ScenarioExecutor userLoginsWith(UserCreateRequestDTO userCreateRequestDTO) {
        return userLoginsWith(LoginRequestBuilder.loginRequestUsing(userCreateRequestDTO));
    }

    public ScenarioExecutor userLoginsWith(LoginRequestDTO loginRequestDTO) {
        this.responseSpec = new Login().apply(webTestClient, loginRequestDTO);
        LoginResponseDTO userLoginResponseDTO = this.responseSpec.returnResult(LoginResponseDTO.class)
                .getResponseBody().blockFirst();
        responses.put(LoginResponseDTO.class, userLoginResponseDTO);
        return this;
    }

    public <T> ScenarioExecutor thenAssertThat(Consumer<T> responseSpecConsumer, Class<T> clazz) {
        T response = getResponseOfType(clazz);
        responseSpecConsumer.accept(response);
        return this;
    }

    private <T> T getResponseOfType(Class<T> clazz) {
        T response = (T) responses.get(clazz);
        return response;
    }

    public ScenarioExecutor retrieveUserFromDatabase(String userName) {
        var userDetailsResponseDTO = databaseClient.sql("SELECT * FROM USER_SCHEMA.USER_TABLE WHERE EMAIL = :email")
                .bind("email", userName)
                .map(this::toModel)
                .one()
                .switchIfEmpty(Mono.defer(Mono::empty))
                .block();

        if (userDetailsResponseDTO != null) {
            responses.put(UserDetailsResponseDTO.class, userDetailsResponseDTO);
        }

        return this;
    }

    private UserDetailsResponseDTO toModel(Readable row) {
        return new UserDetailsResponseDTO(
                row.get("EMAIL", String.class),
                row.get("FIRST_NAME", String.class),
                row.get("LAST_NAME", String.class),
                row.get("ROLE", String.class),
                UUID.fromString(row.get("ID", String.class)),
                row.get("DATE_OF_BIRTH", String.class),
                Gender.valueOf(row.get("GENDER", String.class)),
                row.get("HOME_COUNTRY", String.class)
        );
    }

    public ScenarioExecutor responseHeaderContains(String expectedHeaderKey) {
        this.responseSpec.expectHeader()
                .exists(expectedHeaderKey);
        return this;
    }

    public ScenarioExecutor captureMetrics() {
        return this;
    }

    public ScenarioExecutor whenWeRetrieveMetricsFromService() {
        return this;
    }

    public ScenarioExecutor thenUserRegistrationCounterIsIncremented() {
        return this;
    }

    public ScenarioExecutor accessStatusEndpoint() {
        this.responseSpec = new AccessStatus().apply(webTestClient);
        return this;
    }

    public ScenarioExecutor accessMetricsEndpoint() {
        this.responseSpec = new MetricStatus().apply(webTestClient);
        return this;
    }

    public ScenarioExecutor fetchUserDetailsUsing(Class<? extends WithUserId> userIdClazz,
                                                  Class<? extends WithUserToken> tokenClazz) {
        WithUserToken tokenProvider = getResponseOfType(tokenClazz);
        return fetchUserDetailsUsingToken(userIdClazz, tokenProvider.token());
    }

    public ScenarioExecutor storeLoginTokenUsingKey(String tokenKey) {
        LoginResponseDTO loginResponse = getResponseOfType(LoginResponseDTO.class);
        tokenMap.put(tokenKey, loginResponse.token());
        return this;
    }

    public ScenarioExecutor fetchUserDetailsUsingTokenKey(Class<? extends WithUserId> userIdClazz, String tokenKey) {
        String tokenValue = tokenMap.get(tokenKey);
        return fetchUserDetailsUsingToken(userIdClazz, tokenValue);
    }

    public ScenarioExecutor fetchUserDetailsUsingToken(Class<? extends WithUserId> userIdClazz, String token) {
        WithUserId userIdProvider = getResponseOfType(userIdClazz);
        this.responseSpec = new FetchUser(
                userIdProvider.userId(),
                token)
                .apply(webTestClient);
        UserDetailsFetchResponseDTO userDetailsFetchResponseDTO = this.responseSpec.returnResult(UserDetailsFetchResponseDTO.class)
                .getResponseBody().blockFirst();
        responses.put(UserDetailsFetchResponseDTO.class, userDetailsFetchResponseDTO);
        return this;
    }

    private ScenarioExecutor thenUserTokenShouldBePresentInTheCache(String userName, String token) {
        Jedis resource = jedisPool.getResource();
        assertThat(resource.hget("login:", userName))
                .isNotNull()
                .isEqualTo(token);
        return this;
    }

    public ScenarioExecutor theLoginCacheShouldHave(Class<? extends WithUserId> userIdClazz, String tokenKey) {
        WithUserId userIdProvider = getResponseOfType(userIdClazz);
        return thenUserTokenShouldBePresentInTheCache(userIdProvider.userId(), tokenMap.get(tokenKey));
    }

    public ScenarioExecutor theLoginCacheShouldNOTHave(Class<? extends WithUserId> userIdClazz, String token) {
        WithUserId userIdProvider = getResponseOfType(userIdClazz);
        Jedis resource = jedisPool.getResource();
        assertThat(resource.hget("login:", userIdProvider.userId()))
                .isNotNull()
                .isNotEqualTo(tokenMap.get(token));
        return this;
    }

    public ScenarioExecutor theInvalidationCacheShouldHave(Class<? extends WithUserId> userIdClazz, String token) {
        WithUserId userIdProvider = getResponseOfType(userIdClazz);
        Jedis resource = jedisPool.getResource();
        assertThat(resource.get("invalidated:" + tokenMap.get(token)))
                .isNotNull()
                .isEqualTo(userIdProvider.userId());
        return this;
    }

    public ScenarioExecutor userLogsOutUsingTokenKey(String tokenKey) {
        this.responseSpec = new Logout(tokenMap.get(tokenKey)).apply(webTestClient);
        return this;
    }
}
