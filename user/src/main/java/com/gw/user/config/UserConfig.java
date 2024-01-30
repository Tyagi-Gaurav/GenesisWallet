package com.gw.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("user")
public record UserConfig(String host, int port) {}
