package com.gw.user.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobaRlExceptionHandlerTest {
    private final GlobalExceptionHandler validationExceptionHandler = new GlobalExceptionHandler();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ServerWebExchange serverWebExchange;

    @Mock
    private ServerHttpResponse httpResponse;

    @Test
    void onExceptionMarkResponseAsComplete() {
        when(serverWebExchange.getResponse()).thenReturn(httpResponse);
        when(httpResponse.setComplete()).thenReturn(Mono.empty());

        Mono<Void> handle = validationExceptionHandler.handle(serverWebExchange, new IllegalArgumentException());

        StepVerifier.create(handle).verifyComplete();
        verify(httpResponse).setComplete();
    }

    @Test
    void handleResponseStatusException() {
        when(serverWebExchange.getResponse()).thenReturn(httpResponse);
        when(httpResponse.setComplete()).thenReturn(Mono.empty());

        Mono<Void> handle = validationExceptionHandler.handle(serverWebExchange, new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        StepVerifier.create(handle).verifyComplete();
        verify(httpResponse).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(httpResponse).setComplete();
    }

    @Test
    void handle2xxStatusCode() {
        when(serverWebExchange.getResponse()).thenReturn(httpResponse);
        when(httpResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(httpResponse.setComplete()).thenReturn(Mono.empty());

        Mono<Void> handle = validationExceptionHandler.handle(serverWebExchange, new IllegalArgumentException());

        StepVerifier.create(handle).verifyComplete();
        verify(httpResponse).setComplete();
    }

    @Test
    void handleOtherStatusCode() {
        when(serverWebExchange.getResponse()).thenReturn(httpResponse);
        when(httpResponse.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(httpResponse.setComplete()).thenReturn(Mono.empty());

        Mono<Void> handle = validationExceptionHandler.handle(serverWebExchange, new IllegalArgumentException());

        StepVerifier.create(handle).verifyComplete();
        verify(httpResponse).setComplete();
    }

    @Test
    void handleIllegalCallerException() {
        when(serverWebExchange.getResponse()).thenReturn(httpResponse);
        when(httpResponse.setComplete()).thenReturn(Mono.empty());

        Mono<Void> handle = validationExceptionHandler.handle(serverWebExchange, new IllegalCallerException());

        StepVerifier.create(handle).verifyComplete();
        verify(httpResponse).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(httpResponse).setComplete();
    }
}