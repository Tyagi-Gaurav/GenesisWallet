package com.gt.user.function;

import com.gt.user.utils.TestUtils;
import com.gw.user.service.domain.AccountCreateRequestDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.BiFunction;

public class UserCreate
        implements BiFunction<WebTestClient, AccountCreateRequestDTO, WebTestClient.ResponseSpec> {

    @Override
    public WebTestClient.ResponseSpec apply(WebTestClient webClient,
                                            AccountCreateRequestDTO accountCreateRequestDTO) {

        try {
            return webClient.post().uri("/user/account/create")
                    .header(HttpHeaders.CONTENT_TYPE.toString(), "application/vnd+account.create.v1+json")
                    .header(HttpHeaders.ACCEPT.toString(), "application/vnd+account.create.v1+json")
                    .bodyValue(TestUtils.asJsonString(accountCreateRequestDTO))
                    .exchange();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}