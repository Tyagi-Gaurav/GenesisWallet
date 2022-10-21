package com.gw.user.e2e.function;

import com.gw.user.resource.domain.LoginRequestDTO;
import com.gw.user.testutils.DtoBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;
import java.util.function.BiFunction;

public class Login
        implements BiFunction<WebTestClient, LoginRequestDTO, WebTestClient.ResponseSpec> {

    private String requestId = "";

    public Login(UUID requestId) {
        this.requestId = requestId.toString();
    }

    public Login() {
    }

    @Override
    public WebTestClient.ResponseSpec apply(WebTestClient webTestClient, LoginRequestDTO loginRequestDTO) {
        try {
            return webTestClient.post()
                    .uri("/user/login")
                    .bodyValue(DtoBuilder.asJsonString(loginRequestDTO))
                    .header(HttpHeaders.CONTENT_TYPE.toString(), "application/vnd.login.v1+json")
                    .header(HttpHeaders.ACCEPT.toString(), "application/vnd.login.v1+json")
                    .header("requestId", requestId)
                    .exchange();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}

