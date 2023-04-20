package com.gw.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cache")
public record CacheConfig(String host,
                          int port) {
}
