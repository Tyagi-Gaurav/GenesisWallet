package com.gw.common.http.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoggingFilterTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ServerWebExchange serverWebExchange;
    @Mock
    private WebFilterChain webFilterChain;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ServerHttpRequest serverHttpRequest;

    @Mock
    private ServerHttpResponse serverHttpResponse;

    @InjectMocks
    private LoggingFilter loggingFilter;
    @Mock
    private Logger testLogger;

    @Test
    void filter() {
        when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        when(serverWebExchange.getResponse()).thenReturn(serverHttpResponse);
        when(serverHttpResponse.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
        when(serverHttpRequest.getMethod()).thenReturn(HttpMethod.GET);
        when(serverHttpRequest.getURI().getPath()).thenReturn("/some/uri/path");
        when(serverHttpRequest.getHeaders().getContentType()).thenReturn(MediaType.APPLICATION_JSON);

        when(webFilterChain.filter(serverWebExchange)).thenReturn(Mono.empty().then());

        StepVerifier.create(loggingFilter.filter(serverWebExchange, webFilterChain))
                        .verifyComplete();

        verify(testLogger)
                .info("Received request for {} - {} with content Type: {}",
                        HttpMethod.GET,
                        "/some/uri/path",
                        MediaType.APPLICATION_JSON);

        verify(testLogger)
                .info("Responded with status code: {}",
                        HttpStatusCode.valueOf(200));
    }
}