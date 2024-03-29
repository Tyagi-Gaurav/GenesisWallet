package com.gw.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ConfigurationProperties("accessible")
public record AccessibleEndpointConfig(Map<String, Boolean> endpoints,
                                       Map<String, Boolean> endpointsRegex) {

    public boolean isEnabled(String method, String path) {
        return endpoints != null &&
                endpoints.getOrDefault(String.format("%s-%s", method, path), false);
    }

    public boolean satisfiesRegex(String method, String path) {
        String toMatch = String.format("%s-%s", method, path);
        return endpointsRegex != null && endpointsRegex.keySet().stream().map(Pattern::compile)
                .map(p -> p.matcher(toMatch))
                .anyMatch(Matcher::matches);
    }
}
