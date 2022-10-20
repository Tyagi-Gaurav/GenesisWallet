package com.gw.grpc.common;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class CorrelationIdInterceptor implements ClientInterceptor, ServerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(CorrelationIdInterceptor.class);

    private static final String CORRELATION_ID = "X-REQUEST_ID";
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(channel.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                Metadata.Key<String> REQUEST_ID = Metadata.Key.of(CORRELATION_ID, Metadata.ASCII_STRING_MARSHALLER);
                if (!headers.containsKey(REQUEST_ID)) {
                    LOG.warn("No requestId found in request. Generating a new requestID. Please ask client to generate one.");
                    headers.put(REQUEST_ID, UUID.randomUUID().toString());
                    super.start(responseListener, headers);
                }
            }
        };
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> next) {
        Metadata.Key<String> REQUEST_ID = Metadata.Key.of(CORRELATION_ID, Metadata.ASCII_STRING_MARSHALLER);

        if (!metadata.containsKey(REQUEST_ID)) {
            LOG.warn("No requestId found in request. Generating a new requestID. Please ask client to generate one.");
            metadata.put(REQUEST_ID, UUID.randomUUID().toString());
        }
        return next.startCall(serverCall, metadata);
    }
}
