package com.gw.api.functional.resource;

import com.gw.api.functional.config.ApiGatewayConfig;
import com.gw.api.functional.domain.TestUserDetailsFetchResponseDTO;
import com.gw.api.functional.util.ResponseHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TestAccountDetailsRequestResource extends AbstractResource {
    @Autowired
    private ApiGatewayConfig apiGatewayConfig;

    @Autowired
    private ResponseHolder responseHolder;

    public void getUserDetails(String userId, String token) {
        String path = String.format("/user/details/%s", userId);
        String fullUrl = getFullUrlWithScheme("http", apiGatewayConfig.host().trim(),
                apiGatewayConfig.userContextPath(), path, apiGatewayConfig.nonSecuredPort());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/vnd+user.details.v1+json");
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity<Object> getEntityHeaders = new HttpEntity<>(headers);
        responseHolder.setResponseWithBodyClass(this.get(fullUrl, getEntityHeaders, String.class), TestUserDetailsFetchResponseDTO.class);
    }
}