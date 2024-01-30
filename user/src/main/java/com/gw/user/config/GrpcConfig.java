package com.gw.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("grpc")
public record GrpcConfig(int port) {
}
