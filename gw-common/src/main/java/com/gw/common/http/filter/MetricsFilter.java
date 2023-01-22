package com.gw.common.http.filter;

import com.gw.common.metrics.EndpointMetrics;
import com.gw.common.metrics.EndpointRequestCounter;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class MetricsFilter implements WebFilter {

    private final EndpointRequestCounter endpointRequestCounter;

    private final EndpointMetrics.Histogram endpointRequestLatency;

    public MetricsFilter(EndpointRequestCounter endpointRequestCounter, EndpointMetrics endpointMetrics) {
        this.endpointRequestCounter = endpointRequestCounter;
        endpointRequestLatency = endpointMetrics.createHistogramFor("request_latency");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        HttpMethod method = request.getMethod();
        String path = request.getURI().getPath();
        endpointRequestCounter.increment(String.valueOf(method), path);

        endpointRequestLatency.start();
        Mono<Void> result = chain.filter(exchange);
        endpointRequestLatency.observe();

        return result;
    }
}
