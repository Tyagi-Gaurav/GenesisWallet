package com.gw.common.http.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.gw.common.http.filter.AccessLogging.Direction.IN;
import static com.gw.common.http.filter.AccessLogging.Direction.OUT;
import static com.gw.common.http.filter.AccessLogging.Type.REQUEST;
import static com.gw.common.http.filter.AccessLogging.Type.RESPONSE;
import static com.gw.common.http.filter.AccessLoggingBuilder.anAccessLog;

@Component
public class LoggingFilter implements WebFilter {
    private static final Logger ACCESS_LOG = LogManager.getLogger("ACCESS");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var method = exchange.getRequest().getMethod();
        var path = exchange.getRequest().getURI().getPath();
        var requestLoggingDecorator = new RequestLoggingDecorator(exchange.getRequest());
        var responseLoggingDecorator = new ResponseLoggingDecorator(exchange.getResponse());

        ServerWebExchangeDecorator decorator = new ServerWebExchangeDecorator(exchange) {
            @Override
            public ServerHttpRequest getRequest() {
                return requestLoggingDecorator;
            }

            @Override
            public ServerHttpResponse getResponse() {
                return responseLoggingDecorator;
            }
        };

        return chain.filter(decorator)
                .doOnSuccess(noop -> {
                    if (ACCESS_LOG.isInfoEnabled()) {
                        ACCESS_LOG.info(anAccessLog()
                                .withDirection(IN)
                                .withType(REQUEST)
                                .withMethod(method.name())
                                .withPath(path)
                                .withHeaders(requestLoggingDecorator.getHeaders().toSingleValueMap())
                                .withBody(requestLoggingDecorator.getFullBody())
                                .build());

                        ACCESS_LOG.info(anAccessLog()
                                .withDirection(OUT)
                                .withType(RESPONSE)
                                .withMethod(method.name())
                                .withPath(path)
                                .withStatusCode(Optional.ofNullable(exchange.getResponse().getStatusCode())
                                        .map(HttpStatusCode::value).map(String::valueOf).orElse(null))
                                .withHeaders(exchange.getResponse().getHeaders().toSingleValueMap())
                                .withBody(responseLoggingDecorator.getFullBody())
                                .build());
                    }
                });
    }
}