package com.gw.common.http.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestLoggingDecoratorTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DataBuffer dataBuffer;

    @Mock
    private ByteBuffer byteBuffer;

    @Mock
    private ServerHttpRequest serverHttpRequest;

    private RequestLoggingDecorator requestLoggingDecorator;

    @BeforeEach
    void setUp() {
        when(dataBuffer.asByteBuffer().asReadOnlyBuffer()).thenReturn(byteBuffer);
        when(serverHttpRequest.getBody()).thenReturn(Flux.just(dataBuffer));
        requestLoggingDecorator = new RequestLoggingDecorator(serverHttpRequest);
    }

    @Test
    void returnsBodyFlux() {
        StepVerifier.create(requestLoggingDecorator.getBody())
                .expectNext(dataBuffer)
                .verifyComplete();
    }

    @Test
    void returnsBodyAsString() {
        StepVerifier.create(requestLoggingDecorator.getBody())
                .expectNext(dataBuffer)
                .verifyComplete();

        assertThat(requestLoggingDecorator.getFullBody()).isEmpty();
    }
}