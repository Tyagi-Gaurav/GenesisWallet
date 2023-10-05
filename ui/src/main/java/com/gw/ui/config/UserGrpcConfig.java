package com.gw.ui.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("user")
public record UserGrpcConfig(String host, int port, long timeoutInMs,
                             String circuitBreaker) {
}
