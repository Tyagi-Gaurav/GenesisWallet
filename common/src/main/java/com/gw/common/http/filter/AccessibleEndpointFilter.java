package com.gw.common.http.filter;

import com.gw.common.config.AccessibleEndpointConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnBean(value = AccessibleEndpointConfig.class)
public class AccessibleEndpointFilter implements WebFilter {
    private static final Logger LOG = LogManager.getLogger("ACCESS");

    private final AccessibleEndpointConfig accessibleEndpointConfig;

    public AccessibleEndpointFilter(AccessibleEndpointConfig accessibleEndpointConfig) {
        this.accessibleEndpointConfig = accessibleEndpointConfig;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().toString();

        var methodPathAsString = String.format("%s-%s", method, path);

        if (!(accessibleEndpointConfig.isEnabled(method, path)
                || accessibleEndpointConfig.satisfiesRegex(method, path))) {
            LOG.error("Not allowed to access {}", methodPathAsString);
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return Mono.empty();
        }

        return chain.filter(exchange);
    }
}
