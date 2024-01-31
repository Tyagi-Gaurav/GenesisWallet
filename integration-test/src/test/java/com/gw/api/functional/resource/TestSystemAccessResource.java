package com.gw.api.functional.resource;

import com.gw.api.functional.config.ApiGatewayConfig;
import com.gw.api.functional.domain.TestAccountCreateRequestDTO;
import com.gw.api.functional.util.ResponseHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TestSystemAccessResource extends AbstractResource {

    @Autowired
    private ApiGatewayConfig apiGatewayConfig;

    @Autowired
    private ResponseHolder responseHolder;

    public void nonExistentEndpoint() {
        String fullUrl = getUrl(apiGatewayConfig.host().trim(),
                apiGatewayConfig.userContextPath(), "/user/non-existent", apiGatewayConfig.port());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TestAccountCreateRequestDTO> request = new HttpEntity<>(headers);
        responseHolder.setResponseWithoutBody(this.post(fullUrl, request, String.class));
    }
}