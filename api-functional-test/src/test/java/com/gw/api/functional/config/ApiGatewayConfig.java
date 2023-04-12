package com.gw.api.functional.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("api-gateway")
public record ApiGatewayConfig(String host,
                               int securedPort,
                               int nonSecuredPort,
                               boolean enableSecurity,
                               String contentUploadContextPath,
                               String userContextPath,
                               String uiContextPath) {
    public int port() {
        return enableSecurity ? securedPort : nonSecuredPort;
    }
}
