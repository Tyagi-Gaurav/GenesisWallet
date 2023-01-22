package com.gw.ui.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("user")
public record UserGrpcConfig(String host, int port, long timeoutInMs,
                             String circuitBreaker) {
}
