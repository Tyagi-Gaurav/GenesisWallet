package com.gw.functional.resource;

import com.gw.functional.config.ApiGatewayConfig;
import com.gw.functional.util.ResponseHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TestSwaggerResource extends AbstractResource {

    @Autowired
    private ApiGatewayConfig apiGatewayConfig;

    @Autowired
    private ResponseHolder responseHolder;

    public void accessSwagger() {
        String fullUrl = getSecuredUrl(apiGatewayConfig.host().trim(),
                apiGatewayConfig.userContextPath(),
                "/user/swagger-ui", apiGatewayConfig.securedPort());
        responseHolder.setResponse(this.get(fullUrl, new HttpEntity(HttpHeaders.EMPTY), String.class));
    }
}