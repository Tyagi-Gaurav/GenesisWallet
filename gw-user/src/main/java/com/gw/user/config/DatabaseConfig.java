package com.gw.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("database")
public record DatabaseConfig(
            String username,
            String password,
            String database,
            String hostname,
            String scheme) {}
