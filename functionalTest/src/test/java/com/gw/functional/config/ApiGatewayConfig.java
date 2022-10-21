package com.gw.functional.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("api-gateway")
public record ApiGatewayConfig(String host,
                               int securedPort,
                               int nonSecuredPort,
                               String contentUploadContextPath,
                               String userContextPath) {}
