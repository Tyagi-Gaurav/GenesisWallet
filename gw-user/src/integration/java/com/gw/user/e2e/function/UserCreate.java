package com.gw.user.e2e.function;

import com.gw.user.testutils.DtoBuilder;
import com.gw.user.resource.domain.UserCreateRequestDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.BiFunction;

public class UserCreate
        implements BiFunction<WebTestClient, UserCreateRequestDTO, WebTestClient.ResponseSpec> {

    @Override
    public WebTestClient.ResponseSpec apply(WebTestClient webClient,
                                            UserCreateRequestDTO userCreateRequestDTO) {

        try {
            return webClient.post().uri("/user/create")
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd+user.create.v1+json")
                    .header(HttpHeaders.ACCEPT, "application/vnd+user.create.v1+json")
                    .bodyValue(DtoBuilder.asJsonString(userCreateRequestDTO))
                    .exchange();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
