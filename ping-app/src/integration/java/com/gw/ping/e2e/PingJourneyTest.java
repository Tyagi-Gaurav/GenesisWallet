package com.gw.ping.e2e;

import com.gw.ping.Application;
import com.gw.test.support.framework.WithSyntacticSugar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.gw.ping.e2e.ScenarioExecutor.*;
import static com.gw.test.support.ScenarioBuilder.aManagementScenarioUsing;
import static com.gw.test.support.ScenarioBuilder.aScenarioUsing;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebFlux
public class PingJourneyTest implements WithSyntacticSugar  {
    @Autowired
    private ApplicationContext applicationContext;
    @Test
    void ping() {
        aScenarioUsing(applicationContext)
                .given(aPingRequestIsSent())
                .then(aHttpResponse(isReceived(withStatus(200))))
                .execute();
    }

    @Test
    void status() {
        aManagementScenarioUsing(applicationContext)
                .given(aStatusRequestIsSent())
                .then(aHttpResponse(isReceived(withStatus(200))))
                .then(withHttpResponseBody("{\"status\":\"UP\"}"))
                .execute();
    }
}
