package com.gw.user.e2e.function;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.BiFunction;

public class FetchUser
        implements BiFunction<WebTestClient, String, WebTestClient.ResponseSpec> {

    @Override
    public WebTestClient.ResponseSpec apply(WebTestClient webClient,
                                            String userId) {

        try {
            return webClient.get().uri("/user/details/" + userId)
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd+user.details.v1+json")
                    .header(HttpHeaders.ACCEPT, "application/vnd+user.details.v1+json")
                    .exchange();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
