package com.gw.common.http.filter;

import com.gw.common.metrics.EndpointHistogram;
import com.gw.common.metrics.EndpointRequestCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class MetricsFilter implements WebFilter {

    private static final Logger LOG = LoggerFactory.getLogger(MetricsFilter.class);

    private final EndpointRequestCounter endpointRequestCounter;

    private final EndpointHistogram endpointHistogram;

    public MetricsFilter(EndpointRequestCounter endpointRequestCounter, EndpointHistogram endpointHistogram) {
        this.endpointRequestCounter = endpointRequestCounter;
        this.endpointHistogram = endpointHistogram;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        HttpMethod method = request.getMethod();
        String path = request.getURI().getPath();
        endpointRequestCounter.increment(String.valueOf(method), path);
        var startTime = Instant.now();

        Mono<Void> result = chain.filter(exchange);

        long duration = Instant.now().toEpochMilli() - startTime.toEpochMilli();

        LOG.info("Duration of request was : {} milli seconds", duration);
        endpointHistogram.observe(duration);

        return result;
    }
}
