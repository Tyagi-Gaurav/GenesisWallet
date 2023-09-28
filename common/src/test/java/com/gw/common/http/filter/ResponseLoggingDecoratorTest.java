package com.gw.common.http.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ResponseLoggingDecoratorTest {

    private ServerHttpResponse serverHttpResponse = new MockServerHttpResponse();

    @Mock
    private DataBuffer dataBuffer;

    @Mock
    private ByteBuffer byteBuffer;

    private ResponseLoggingDecorator responseLoggingDecorator;

    @BeforeEach
    void setUp() {
        byteBuffer = ByteBuffer.wrap("Hello World".getBytes(StandardCharsets.UTF_8));
        responseLoggingDecorator = new ResponseLoggingDecorator(serverHttpResponse);
    }

    @Test
    void returnFullBodyResponse() {
        DefaultDataBuffer defaultDataBuffer = new DefaultDataBufferFactory().allocateBuffer(20);
        responseLoggingDecorator.writeWith(Flux.just(defaultDataBuffer));

        assertThat(responseLoggingDecorator.getFullBody()).isEqualTo("");
    }
}