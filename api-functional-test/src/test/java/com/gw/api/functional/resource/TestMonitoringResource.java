package com.gw.api.functional.resource;

import com.gw.api.functional.config.ApiGatewayConfig;
import com.gw.api.functional.util.ResponseHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TestMonitoringResource extends AbstractResource {
    @Autowired
    private ApiGatewayConfig apiGatewayConfig;

    @Autowired
    private ResponseHolder responseHolder;

    public void accessUserStatus() {
        String fullUrl = getUrl(apiGatewayConfig.host().trim(),
                apiGatewayConfig.userContextPath(),
                "/user/private/healthcheck/status", apiGatewayConfig.port());
        responseHolder.setResponse(this.get(fullUrl, new HttpEntity(HttpHeaders.EMPTY), String.class));
    }

    public void accessUIStatus() {
        String fullUrl = getUrl(apiGatewayConfig.host().trim(),
                apiGatewayConfig.uiContextPath(),
                "/private/healthcheck/status", apiGatewayConfig.port());
        responseHolder.setResponse(this.get(fullUrl, new HttpEntity(HttpHeaders.EMPTY), String.class));
    }

    public void accessMetrics() {
        String fullUrl = getUrl(apiGatewayConfig.host().trim(),
                apiGatewayConfig.userContextPath(),
                "/user/private/prometheus", apiGatewayConfig.port());
        responseHolder.setResponse(this.get(fullUrl, new HttpEntity(HttpHeaders.EMPTY), String.class));
    }
}
