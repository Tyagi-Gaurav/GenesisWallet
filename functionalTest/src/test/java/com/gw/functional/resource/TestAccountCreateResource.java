package com.gw.functional.resource;

import com.gw.functional.config.ApiGatewayConfig;
import com.gw.functional.domain.TestAccountCreateRequestDTO;
import com.gw.functional.util.ResponseHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TestAccountCreateResource extends AbstractResource {

    @Autowired
    private ApiGatewayConfig apiGatewayConfig;

    @Autowired
    private ResponseHolder responseHolder;

    public void create(TestAccountCreateRequestDTO accountCreateRequestDTO) {
        createUsingRest(accountCreateRequestDTO);
    }

    public void createWithHttp(TestAccountCreateRequestDTO accountCreateRequestDTO) {
        String fullUrl = getFullUrlWithScheme("http", apiGatewayConfig.host().trim(),
                apiGatewayConfig.userContextPath(), "/user/create", apiGatewayConfig.nonSecuredPort());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/vnd+user.create.v1+json");
        HttpEntity<TestAccountCreateRequestDTO> request = new HttpEntity<>(accountCreateRequestDTO, headers);
        responseHolder.setResponse(this.post(fullUrl, request, String.class));
    }

    private void createUsingRest(TestAccountCreateRequestDTO accountCreateRequestDTO) {
        String fullUrl = getSecuredUrl(apiGatewayConfig.host().trim(),
                apiGatewayConfig.userContextPath(), "/user/create", apiGatewayConfig.securedPort());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/vnd+user.create.v1+json");
        HttpEntity<TestAccountCreateRequestDTO> request = new HttpEntity<>(accountCreateRequestDTO, headers);
        responseHolder.setResponse(this.post(fullUrl, request, String.class));
    }
}