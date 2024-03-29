package com.gw.user.security;

import com.gw.common.util.TokenManager;
import com.gw.user.cache.CacheManager;
import com.gw.user.config.AuthenticationManager;
import com.gw.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Autowired
    private UserService userDetailsService;

    @Bean
    public UserDetailsRepositoryReactiveAuthenticationManager userDetailsRepositoryReactiveAuthenticationManager() {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity httpSecurity,
                                                JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                                TokenManager tokenManager,
                                                CacheManager cacheManager) {
        return httpSecurity
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/actuator/**", "/v2/**", "/swagger-ui/**",
                        "/user/create",
                        "/user/login",
                        "/swagger-resources/**").permitAll()
                .anyExchange().authenticated().and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .securityContextRepository(new SecurityContextRepository(
                        new AuthenticationManager(userDetailsService, tokenManager, cacheManager)
                ))
                .build();
    }

}