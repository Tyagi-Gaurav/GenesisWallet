package com.gw.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;

@ConstructorBinding
@ConfigurationProperties("database")
public record DatabaseConfig(
        String driver,
        String name,
        String schema,
        Map<String, String> r2dbcPool) {
}
