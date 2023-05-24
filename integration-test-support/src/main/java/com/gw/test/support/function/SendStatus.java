package com.gw.test.support.function;

import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Function;

public class SendStatus
        implements Function<WebTestClient, WebTestClient.ResponseSpec> {

    @Override
    public WebTestClient.ResponseSpec apply(WebTestClient webClient) {

        try {
            return webClient.get().uri("/actuator/healthcheck/status")
                    .exchange();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
