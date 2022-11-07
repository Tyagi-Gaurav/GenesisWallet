package com.gw.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("vault")
public record VaultProperties(String token, String host, int port, String httpScheme, String appRole)  {
}
