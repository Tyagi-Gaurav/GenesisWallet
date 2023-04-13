package com.gw.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("auth")
public record AuthConfig(Duration tokenDuration, String secret) { }
