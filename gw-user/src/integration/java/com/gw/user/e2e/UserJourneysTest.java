package com.gw.user.e2e;

import com.gw.user.Application;
import com.gw.user.e2e.builder.UserCreateRequestBuilder;
import com.gw.user.e2e.domain.UserDetailsResponseDTO;
import com.gw.user.e2e.security.TestContainerVaultInitializer;
import com.gw.user.repo.TestContainerDatabaseInitializer;
import com.gw.user.resource.domain.LoginResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {TestContainerDatabaseInitializer.class, TestContainerVaultInitializer.class})
@AutoConfigureWebFlux
@ActiveProfiles("UserJourneysTest")
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "user.host=localhost",
        "user.port=${wiremock.server.port}"
})
public class UserJourneysTest {
    private ScenarioExecutor scenarioExecutor;

    @Autowired
    private DatabaseClient databaseClient;

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + serverPort + "/api";
        var webTestClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
        scenarioExecutor = new ScenarioExecutor(webTestClient, databaseClient);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "abc", "efuusidhfauihsdfuhiusdhfaiuhsfiuhiufhs"})
    void createUserWithInvalidUser(String userName) {
        var accountCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest()
                .withUserName(userName)
                .build();

        scenarioExecutor
                .userIsCreatedFor(accountCreateRequestDTO)
                .then().expectReturnCode(400);
    }

    @Test
    void createValidUserTest() {
        var userCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest().build();

        scenarioExecutor
                .captureMetrics()
                .userIsCreatedFor(userCreateRequestDTO)
                .then().expectReturnCode(201)
                .retrieveUserFromDatabase(userCreateRequestDTO.userName())
                .thenAssertThat(userDetailsResponse -> {
                    assertThat(userDetailsResponse).isNotNull();
                    assertThat(userDetailsResponse.firstName()).isEqualTo(userCreateRequestDTO.firstName());
                    assertThat(userDetailsResponse.lastName()).isEqualTo(userCreateRequestDTO.lastName());
                    assertThat(userDetailsResponse.userName()).isEqualTo(userCreateRequestDTO.userName());
                    assertThat(userDetailsResponse.dateOfBirth()).isEqualTo(userCreateRequestDTO.dateOfBirth());
                    assertThat(userDetailsResponse.gender()).isEqualTo(userCreateRequestDTO.gender());
                    assertThat(userDetailsResponse.homeCountry()).isEqualTo(userCreateRequestDTO.homeCountry());
                }, UserDetailsResponseDTO.class)
                .whenWeRetrieveMetricsFromService()
                .thenUserRegistrationCounterIsIncremented();
    }

    @Test
    void createValidUserAndLogin() {
        var userCreateRequestDTO = UserCreateRequestBuilder.userCreateRequest().build();

        scenarioExecutor
                .when().userIsCreatedFor(userCreateRequestDTO).expectReturnCode(201)
                .userLoginsWith(userCreateRequestDTO).expectReturnCode(200)
                .thenAssertThat(loginResponse ->
                    assertThat(loginResponse.token()).isNotEmpty()
                , LoginResponseDTO.class);
    }
}
