package com.gw.user.e2e.function;

import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Function;

public class MetricStatus implements Function<WebTestClient, WebTestClient.ResponseSpec> {

    @Override
    public WebTestClient.ResponseSpec apply(WebTestClient webClient) {

        try {
            return webClient.get().uri("/actuator/prometheus")
                    .exchange();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
