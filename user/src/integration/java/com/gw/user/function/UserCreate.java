package com.gw.user.function;

import com.gw.user.utils.TestUtils;
import com.gw.user.resource.domain.AccountCreateRequestDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.BiFunction;

public class UserCreate
        implements BiFunction<WebTestClient, AccountCreateRequestDTO, WebTestClient.ResponseSpec> {

    @Override
    public WebTestClient.ResponseSpec apply(WebTestClient webClient,
                                            AccountCreateRequestDTO accountCreateRequestDTO) {

        try {
            return webClient.post().uri("/user/create")
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd+user.create.v1+json")
                    .header(HttpHeaders.ACCEPT, "application/vnd+user.create.v1+json")
                    .bodyValue(TestUtils.asJsonString(accountCreateRequestDTO))
                    .exchange();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
