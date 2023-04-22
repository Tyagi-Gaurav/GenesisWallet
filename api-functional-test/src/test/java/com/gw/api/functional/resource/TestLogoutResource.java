package com.gw.api.functional.resource;

import com.gw.api.functional.config.ApiGatewayConfig;
import com.gw.api.functional.domain.TestLoginRequestDTO;
import com.gw.api.functional.util.ResponseHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TestLogoutResource extends AbstractResource {

    @Autowired
    private ApiGatewayConfig apiGatewayConfig;

    @Autowired
    private ResponseHolder responseHolder;

    public void doLogout(String token) {
        String fullUrl = getUrl(apiGatewayConfig.host().trim(),
                apiGatewayConfig.userContextPath(),
                "/user/logout", apiGatewayConfig.port());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/vnd.logout.v1+json");
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity<TestLoginRequestDTO> request = new HttpEntity<>(headers);
        responseHolder.setResponseWithoutBody(this.post(fullUrl, request, String.class));
    }
}