package com.gw.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("database")
public record DatabaseConfig(String driver,
                             String host,
                             int port,
                             String database,
                             String schema,
                             String user,
                             String password,
                             int minPoolSize,
                             int maxPoolSize) {}