package com.gw.test.support;

import com.gw.test.support.framework.*;
import jakarta.annotation.Nullable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

public class ScenarioBuilder {
    private final TestContext testContext;
    List<ExecutionStep> executors = new ArrayList<>();

    private ScenarioBuilder(TestContext testContext) {
        this.testContext = testContext;
    }

    public static ScenarioBuilder aScenarioUsing(ApplicationContext applicationContext) {
        String serverPort = getLocalServerPort(applicationContext);
        String baseUrl = "http://localhost:" + serverPort + "/api";
        var webTestClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
        TestContext testContext = new TestContext(webTestClient, applicationContext);
        return new ScenarioBuilder(testContext);
    }

    public static ScenarioBuilder aManagementScenarioUsing(ApplicationContext applicationContext) {
        String serverPort = getManagementServerPort(applicationContext);
        String baseUrl = "http://localhost:" + serverPort;
        var webTestClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
        TestContext testContext = new TestContext(webTestClient, applicationContext);
        return new ScenarioBuilder(testContext);
    }

    public ScenarioBuilder given(ExecutionStep givenBuilder) {
        this.executors.add(givenBuilder);
        return this;
    }

    public ScenarioBuilder when(ExecutionStep givenBuilder) {
        this.executors.add(givenBuilder);
        return this;
    }

    public ScenarioBuilder and(ExecutionStep givenBuilder) {
        this.executors.add(givenBuilder);
        return this;
    }

    public ScenarioBuilder then(ExecutionStep thenBuilder) {
        this.executors.add(thenBuilder);
        return this;
    }

    public void execute() {
        for (ExecutionStep executor : executors) {
            try {
                if (executor instanceof ResponseStep responseStep) {
                    TestResponse response = responseStep.apply(testContext);
                    testContext.mapResponse(response);
                } else if (executor instanceof VoidStep voidStep) {
                    voidStep.apply(testContext);
                }
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Nullable
    private static String getLocalServerPort(ApplicationContext applicationContext) {
        return applicationContext.getBean(PropertySourcesPlaceholderConfigurer.class)
                .getAppliedPropertySources().get("environmentProperties").getProperty("local.server.port").toString();
    }

    @Nullable
    private static String getManagementServerPort(ApplicationContext applicationContext) {
        return applicationContext.getBean(PropertySourcesPlaceholderConfigurer.class)
                .getAppliedPropertySources().get("environmentProperties").getProperty("local.management.port").toString();
    }
}
