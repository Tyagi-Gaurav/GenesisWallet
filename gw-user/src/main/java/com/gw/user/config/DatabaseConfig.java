package com.gw.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("database")
public record DatabaseConfig(
        String driver,
        String name,
        String schema,
        String url,
        NewDb newDb ,
        Map<String, String> r2dbcPool) {

    public record NewDb(
            String username,
            String password,
            String database,
            String hostname,
            String scheme) {}
}
