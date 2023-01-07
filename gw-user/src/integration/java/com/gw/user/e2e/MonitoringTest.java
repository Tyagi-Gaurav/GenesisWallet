package com.gw.user.e2e;

import com.gw.user.Application;
import com.gw.user.e2e.security.TestContainerVaultInitializer;
import com.gw.user.repo.TestContainerDatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

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
public class MonitoringTest {
    private ScenarioExecutor scenarioExecutor;

    @Autowired
    private DatabaseClient databaseClient;

    @LocalManagementPort
    private int serverPort;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + serverPort;
        var webTestClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
        scenarioExecutor = new ScenarioExecutor(webTestClient, databaseClient);
    }

    @Test
    void shouldBeAbleToAccessStatusEndpoint() {
        scenarioExecutor
                .accessStatusEndpoint()
                .then().expectReturnCode(200);
    }

    @Disabled
    void shouldBeAbleToAccessMetricsEndpoint() {
        scenarioExecutor
                .accessMetricsEndpoint()
                .then().expectReturnCode(200);
    }
}
