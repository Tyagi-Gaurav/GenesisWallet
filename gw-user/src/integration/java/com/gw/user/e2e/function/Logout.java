package com.gw.user.e2e.function;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Function;

public class Logout implements Function<WebTestClient, WebTestClient.ResponseSpec> {
    private final String token;

    public Logout(String token) {
        this.token = token;
    }

    @Override
    public WebTestClient.ResponseSpec apply(WebTestClient webClient) {

        try {
            return webClient.post().uri("/user/logout")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.logout.v1+json")
                    .header(HttpHeaders.ACCEPT, "application/vnd.logout.v1+json")
                    .exchange();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
