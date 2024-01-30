package com.gw.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.common.http.filter.LoggingFilter;
import com.gw.common.http.filter.MetricsFilter;
import com.gw.common.http.filter.RequestIdFilter;
import com.gw.common.util.TokenManager;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Configuration
@EnableWebFlux
@ConfigurationPropertiesScan(value = {"com.gw.user.config", "com.gw.common", "com.gw.security.config"})
public class BeanFactory implements WebFluxConfigurer {
    @Autowired
    private LoggingFilter loggingFilter;

    @Autowired
    private RequestIdFilter requestIdFilter;

    @Autowired
    private MetricsFilter metricsFilter;

    @Bean
    @Qualifier("signingKey")
    public Key signingKey(AuthConfig authConfig) {
        byte[] apiKeySecretBytes = Base64.getEncoder().encode(authConfig.secret().getBytes());
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    @Bean
    public WebClient webClient(UserConfig userConfig) {
        return WebClient.builder()
                .baseUrl(String.format("http://%s:%d", userConfig.host(), userConfig.port()))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public TokenManager tokenManager(@Qualifier("signingKey") Key key) {
        return new TokenManager(key);
    }
}
