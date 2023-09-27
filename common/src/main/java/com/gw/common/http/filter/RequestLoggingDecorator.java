package com.gw.common.http.filter;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

public class RequestLoggingDecorator extends ServerHttpRequestDecorator {
  private String body;

  public RequestLoggingDecorator(ServerHttpRequest delegate) {
    super(delegate);
  }

  @Override
  public Flux<DataBuffer> getBody() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    return super.getBody().doOnNext(dataBuffer -> {
      try {
        Channels.newChannel(baos).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
        body = baos.toString(StandardCharsets.UTF_8);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          baos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public String getFullBody() {
    return body;
  }
}