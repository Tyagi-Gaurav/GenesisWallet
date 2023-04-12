package com.gw.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("vault")
public record VaultProperties(String token, String host, int port, String httpScheme, String appRole)  {
}
