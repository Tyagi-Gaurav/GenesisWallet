package com.gw.user.client;

public record UserGrpcClientConfig(String serviceName, int port, long timeoutInMs,
                                   String circuitBreaker) {}
