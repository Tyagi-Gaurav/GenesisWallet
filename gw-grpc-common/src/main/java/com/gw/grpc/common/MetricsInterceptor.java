package com.gw.grpc.common;

import com.gw.common.metrics.EndpointMetrics;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MetricsInterceptor implements ClientInterceptor, ServerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(MetricsInterceptor.class);

    private static final String CORRELATION_ID = "X-REQUEST_ID";
    private final EndpointMetrics.Histogram serverGrpcDuration;
    private final EndpointMetrics endpointMetrics;

    public MetricsInterceptor(EndpointMetrics endpointMetrics) {
        serverGrpcDuration = endpointMetrics.createHistogramFor("grpc_server_request_duration");
        this.endpointMetrics = endpointMetrics;
    }


    //For outgoing calls as a client
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(channel.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                var clientGrpcDuration = endpointMetrics.createHistogramFor("grpc_client_request_duration", "fullMethod", method.getFullMethodName());
                clientGrpcDuration.start();
                super.start(responseListener, headers);
                clientGrpcDuration.observe();
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
        serverGrpcDuration.start();
        ServerCall.Listener<ReqT> reqTListener = next.startCall(serverCall, metadata);
        serverGrpcDuration.observe();
        return reqTListener;
    }
}
