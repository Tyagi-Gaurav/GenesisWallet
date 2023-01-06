package com.gw.user.e2e;

import com.gw.common.domain.Gender;
import com.gw.user.e2e.builder.LoginRequestBuilder;
import com.gw.user.e2e.domain.UserDetailsResponseDTO;
import com.gw.user.e2e.function.AccessStatus;
import com.gw.user.e2e.function.Login;
import com.gw.user.e2e.function.UserCreate;
import com.gw.user.resource.domain.LoginRequestDTO;
import com.gw.user.resource.domain.LoginResponseDTO;
import com.gw.user.resource.domain.UserCreateRequestDTO;
import io.r2dbc.spi.Row;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ScenarioExecutor {
    private WebTestClient.ResponseSpec responseSpec;
    private LoginResponseDTO userLoginResponseDTO;

    private final Map<Class, Object> responses = new HashMap<>();
    private final WebTestClient webTestClient;
    private final DatabaseClient databaseClient;

    public ScenarioExecutor(WebTestClient webTestClient, DatabaseClient databaseClient) {
        this.webTestClient = webTestClient;
        this.databaseClient = databaseClient;
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
        return this;
    }

    public ScenarioExecutor userLoginsWith(UserCreateRequestDTO userCreateRequestDTO) {
        return userLoginsWith(LoginRequestBuilder.loginRequestUsing(userCreateRequestDTO));
    }

    public ScenarioExecutor userLoginsWith(LoginRequestDTO loginRequestDTO) {
        this.responseSpec = new Login().apply(webTestClient, loginRequestDTO);
        this.userLoginResponseDTO = this.responseSpec.returnResult(LoginResponseDTO.class)
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

    private UserDetailsResponseDTO toModel(Row row) {
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
}
