package com.gw.user.e2e.test;

import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ScenarioBuilder {
    private final TestContext testContext;
    List<ExecutionStep> executors = new ArrayList<>();

    private ScenarioBuilder(TestContext testContext) {
        this.testContext = testContext;
    }

    public static ScenarioBuilder aScenarioUsing(ApplicationContext applicationContext) {
        var databaseClient = applicationContext.getBean(DatabaseClient.class);
        var jedisPool = applicationContext.getBean(JedisPool.class);
        String serverPort = getLocalServerPort(applicationContext);
        String baseUrl = "http://localhost:" + serverPort + "/api";
        var webTestClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
        TestContext testContext = new TestContext(webTestClient, databaseClient, jedisPool);
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
}
