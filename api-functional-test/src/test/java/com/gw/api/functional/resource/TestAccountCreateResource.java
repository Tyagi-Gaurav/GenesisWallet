package com.gw.api.functional.resource;

import com.gw.api.functional.config.ApiGatewayConfig;
import com.gw.api.functional.domain.TestAccountCreateRequestDTO;
import com.gw.api.functional.domain.TestAccountCreateResponseDTO;
import com.gw.api.functional.util.ResponseHolder;
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
        String fullUrl = getUrl(apiGatewayConfig.host().trim(),
                apiGatewayConfig.userContextPath(), "/user/create", apiGatewayConfig.port());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/vnd+user.create.v1+json");
        HttpEntity<TestAccountCreateRequestDTO> request = new HttpEntity<>(accountCreateRequestDTO, headers);
        responseHolder.addResponse(this.post(fullUrl, request, String.class), TestAccountCreateResponseDTO.class);
    }

    public void createWithHttp(TestAccountCreateRequestDTO accountCreateRequestDTO) {
        String fullUrl = getFullUrlWithScheme("http", apiGatewayConfig.host().trim(),
                apiGatewayConfig.userContextPath(), "/user/create", apiGatewayConfig.nonSecuredPort());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/vnd+user.create.v1+json");
        HttpEntity<TestAccountCreateRequestDTO> request = new HttpEntity<>(accountCreateRequestDTO, headers);
        responseHolder.addResponse(this.post(fullUrl, request, String.class), TestAccountCreateResponseDTO.class);
    }

}