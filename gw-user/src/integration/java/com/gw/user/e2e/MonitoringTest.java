package com.gw.user.e2e;

import com.gw.test.support.framework.WithSyntacticSugar;
import com.gw.user.Application;
import com.gw.user.e2e.security.TestContainerVaultInitializer;
import com.gw.user.repo.TestContainerDatabaseInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.gw.test.support.ScenarioBuilder.aManagementScenarioUsing;
import static com.gw.test.support.executor.BaseScenarioExecutor.aStatusRequestIsSent;
import static com.gw.user.e2e.test.ScenarioExecutor2.aHttpResponse;
import static com.gw.user.e2e.test.ScenarioExecutor2.withStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {TestContainerDatabaseInitializer.class, TestContainerVaultInitializer.class})
@AutoConfigureWebFlux
@ActiveProfiles("UserJourneysTest")
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "user.host=localhost",
        "user.port=${wiremock.server.port}",
        "auth.tokenDuration=2s"
})
class MonitoringTest implements WithSyntacticSugar {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void shouldBeAbleToAccessStatusEndpoint() {
            aManagementScenarioUsing(applicationContext)
                    .given(aStatusRequestIsSent())
                    .then(aHttpResponse(isReceived(withStatus(200))))
                    .execute();
    }
}
