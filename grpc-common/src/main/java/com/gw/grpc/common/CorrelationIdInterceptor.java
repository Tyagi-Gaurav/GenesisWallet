package com.gw.grpc.common;

import io.grpc.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CorrelationIdInterceptor implements ClientInterceptor, ServerInterceptor {
    private static final Logger LOG = LogManager.getLogger("APP");

    private static final String CORRELATION_ID = "X-REQUEST_ID";

    //For outgoing calls
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(channel.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                Metadata.Key<String> requestId = Metadata.Key.of(CORRELATION_ID, Metadata.ASCII_STRING_MARSHALLER);
                if (!headers.containsKey(requestId)) {
                    LOG.warn("No requestId found in request. Generating a new requestID. Please ask client to generate one.");
                    headers.put(requestId, UUID.randomUUID().toString());
                }
                super.start(responseListener, headers);
            }
        };
    }

    //For incoming calls
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> next) {
        Metadata.Key<String> requestId = Metadata.Key.of(CORRELATION_ID, Metadata.ASCII_STRING_MARSHALLER);

        if (!metadata.containsKey(requestId)) {
            LOG.warn("No requestId found in request. Generating a new requestID. Please ask client to generate one.");
            metadata.put(requestId, UUID.randomUUID().toString());
        }
        return next.startCall(serverCall, metadata);
    }
}
