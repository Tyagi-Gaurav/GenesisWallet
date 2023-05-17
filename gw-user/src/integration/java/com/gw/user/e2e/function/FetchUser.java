package com.gw.user.e2e.function;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Function;

public class FetchUser
        implements Function<WebTestClient, WebTestClient.ResponseSpec> {

    private final String userId;
    private final String token;

    public FetchUser(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    @Override
    public WebTestClient.ResponseSpec apply(WebTestClient webClient) {

        try {
            return webClient.get().uri("/user/details/" + userId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd+user.details.v1+json")
                    .header(HttpHeaders.ACCEPT, "application/vnd+user.details.v1+json")
                    .exchange();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
