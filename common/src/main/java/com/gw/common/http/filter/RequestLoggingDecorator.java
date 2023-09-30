package com.gw.common.http.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

import static java.util.Optional.ofNullable;

public class RequestLoggingDecorator extends ServerHttpRequestDecorator {
  private String body = "";
  private static final Logger LOG = LogManager.getLogger("APP");

  public RequestLoggingDecorator(ServerHttpRequest delegate) {
    super(delegate);
  }

  @Override
  public Flux<DataBuffer> getBody() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    return super.getBody().doOnNext(dataBuffer -> {
      try {
        Channels.newChannel(baos).write(dataBuffer.readableByteBuffers().next());
        body = ofNullable(baos.toString(StandardCharsets.UTF_8)).orElse("");
      } catch (IOException e) {
        LOG.error(e.getMessage(), e);
      } finally {
        try {
          baos.close();
        } catch (IOException e) {
          LOG.error(e.getMessage(), e);
        }
      }
    });
  }

  public String getFullBody() {
    return body;
  }
}