package com.gw.common.http.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingFilterTest {

    @Mock
    private ServerWebExchange serverWebExchange;
    @Mock
    private WebFilterChain webFilterChain;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ServerHttpRequest serverHttpRequest;

    @InjectMocks
    private LoggingFilter loggingFilter;
    @Mock
    private Logger testLogger;

    @Test
    void filter() {
        when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        when(serverHttpRequest.getMethod()).thenReturn(HttpMethod.GET);
        when(serverHttpRequest.getURI().getPath()).thenReturn("/some/uri/path");
        when(serverHttpRequest.getHeaders().getContentType()).thenReturn(MediaType.APPLICATION_JSON);

        when(webFilterChain.filter(serverWebExchange)).thenReturn(Mono.defer(Mono::empty));

        loggingFilter.filter(serverWebExchange, webFilterChain);

        verify(testLogger)
                .info("Received request for {} - {} with content Type: {}",
                        HttpMethod.GET,
                        "/some/uri/path",
                        MediaType.APPLICATION_JSON);
    }

}