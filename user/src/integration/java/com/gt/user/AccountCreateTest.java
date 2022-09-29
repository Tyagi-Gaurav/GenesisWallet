package com.gt.user;

import com.gt.user.utils.AccountCreateRequestBuilder;
import com.gw.user.Application;
import com.gw.user.service.domain.AccountCreateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.sql.DataSource;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = Initializer.class)
@AutoConfigureWebFlux
@ActiveProfiles("MovieReadTest")
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "user.host=localhost",
        "user.port=${wiremock.server.port}"
})
public class AccountCreateTest {
    private ScenarioExecutor scenarioExecutor;

    @LocalServerPort
    private int serverPort;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + serverPort + "/api";
        WebTestClient webTestClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
        scenarioExecutor = new ScenarioExecutor(webTestClient, dataSource);
    }

    @ParameterizedTest
    @NullSource
    //@ValueSource(strings = {"", "abc", "abcde", "efuusidhfauihsdfuhiusdhfaiuhsfiuhiufhs"})
    void createUserWithInvalidUser(String userName) {
        AccountCreateRequestDTO accountCreateRequestDTO = AccountCreateRequestBuilder.accountCreateRequest()
                .withUserName(userName)
                .build();

        scenarioExecutor
                .userIsCreatedFor(accountCreateRequestDTO)
                .then().expectReturnCode(204);
    }
}
