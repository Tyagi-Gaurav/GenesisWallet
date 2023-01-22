package com.gw.grpc.common;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.grpc.*;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class ResilienceInterceptor implements ClientInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(ResilienceInterceptor.class);
    private final CircuitBreaker r4jCircuitBreaker;

    public ResilienceInterceptor(String circuitBreakerName,
                                 CircuitBreakerRegistry circuitBreakerRegistry,
                                 MeterRegistry meterRegistry) {
        r4jCircuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
        TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry).bindTo(meterRegistry);
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        Supplier<ForwardingClientCall.SimpleForwardingClientCall> supplier = () -> {
            try {
                return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
                    @Override
                    public void start(Listener<RespT> responseListener, Metadata headers) {
                        super.start(responseListener, headers);
                    }
                };
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            return null;
        };

        Decorators.DecorateSupplier<ForwardingClientCall.SimpleForwardingClientCall> targetObjectSupplier = Decorators.ofSupplier(supplier);
        Decorators.DecorateSupplier<ForwardingClientCall.SimpleForwardingClientCall> resilienceDecorator = targetObjectSupplier.withCircuitBreaker(r4jCircuitBreaker);
        LOG.info("Calling using resilience 4j");
        return resilienceDecorator.get();
    }
}
