package com.gw.common.http.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

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
                        ACCESS_LOG.info("""
                                                                    
                                        Type: Request
                                        Method: {}
                                        Path: {}
                                        Headers: {}
                                        Body: {}
                                        """
                                , method, path, toString(requestLoggingDecorator.getHeaders()),
                                requestLoggingDecorator.getFullBody());
                        ACCESS_LOG.info("""

                                        Type: Response
                                        Method: {}
                                        Path: {}
                                        StatusCode: {}
                                        Headers: {}
                                        Body: {}
                                        """
                                , method, path,
                                exchange.getResponse().getStatusCode(),
                                toString(exchange.getResponse().getHeaders()),
                                responseLoggingDecorator.getFullBody());
                    }
                });
    }

    private static String toString(HttpHeaders headers) {
        return headers.toSingleValueMap()
                .toString();
    }
}