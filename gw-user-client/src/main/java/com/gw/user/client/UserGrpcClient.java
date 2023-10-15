package com.gw.user.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.gw.grpc.common.ResilienceInterceptor;
import com.gw.user.grpc.*;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Component
public class UserGrpcClient {
    private static final Logger LOG = LogManager.getLogger("APP");
    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;
    private final UserServiceGrpc.UserServiceFutureStub userServiceFutureStub;
    private final UserGrpcClientConfig userGrpcClientConfig;

    public UserGrpcClient(UserGrpcClientConfig userGrpcClientConfig,
                          List<ClientInterceptor> clientInterceptorList,
                          CircuitBreakerRegistry circuitBreakerRegistry,
                          MeterRegistry meterRegistry) {
        this(ManagedChannelBuilder.forAddress(userGrpcClientConfig.serviceName(), userGrpcClientConfig.port())
                        .usePlaintext()
                        .build(),
                userGrpcClientConfig,
                clientInterceptorList,
                circuitBreakerRegistry,
                meterRegistry);
    }

    public UserGrpcClient(ManagedChannel managedChannel, UserGrpcClientConfig userGrpcClientConfig,
                          List<ClientInterceptor> clientInterceptorList,
                          CircuitBreakerRegistry circuitBreakerRegistry,
                          MeterRegistry meterRegistry) {
        this.userGrpcClientConfig = userGrpcClientConfig;
        LOG.debug("GRPC Client Config: {}", userGrpcClientConfig);

        var resilienceInterceptor = new ResilienceInterceptor(userGrpcClientConfig.circuitBreaker(),
                circuitBreakerRegistry, meterRegistry);
        clientInterceptorList = Stream.concat(clientInterceptorList.stream(), Stream.of(resilienceInterceptor)).toList();
        this.userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel)
                .withInterceptors(clientInterceptorList.toArray(new ClientInterceptor[0]));

        this.userServiceFutureStub = UserServiceGrpc.newFutureStub(managedChannel)
                .withInterceptors(clientInterceptorList.toArray(new ClientInterceptor[0]));
    }

    public UserDetailsGrpcResponseDTO fetchUsersByUserNameSync(FetchUserDetailsByUserNameGrpcRequestDTO request) {
        return userServiceBlockingStub.fetchUsersByUserName(request);
    }

    public ListenableFuture<UserDetailsGrpcResponseDTO> fetchUsersByIdAsync(FetchUserDetailsByUserNameGrpcRequestDTO fetchUserDetailsByUserNameGrpcRequestDTO) {
        return userServiceFutureStub.fetchUsersByUserName(fetchUserDetailsByUserNameGrpcRequestDTO);
    }

    public void createUserSync(UserCreateGrpcRequestDTO userCreateGrpcRequestDTO) {
        userServiceBlockingStub.withDeadlineAfter(userGrpcClientConfig.timeoutInMs(), TimeUnit.MILLISECONDS)
                .createUser(userCreateGrpcRequestDTO);
    }

    public ListenableFuture<UserCreateGrpcResponseDTO> createUserAsync(UserCreateGrpcRequestDTO userCreateGrpcRequestDTO) {
        return userServiceFutureStub
                .withDeadlineAfter(userGrpcClientConfig.timeoutInMs(), TimeUnit.MILLISECONDS)
                .createUser(userCreateGrpcRequestDTO);
    }

    public void createExternalUserSync(UserCreateOrFindGrpcRequestDTO userCreateOrFindGrpcRequestDTO) {
        userServiceBlockingStub.createOrFindUser(userCreateOrFindGrpcRequestDTO);
    }

    public ListenableFuture<UserCreateOrFindGrpcResponseDTO> createExternalUserAsync(UserCreateOrFindGrpcRequestDTO userCreateOrFindGrpcRequestDTO) {
        return userServiceFutureStub
                .withDeadlineAfter(userGrpcClientConfig.timeoutInMs(), TimeUnit.MILLISECONDS)
                .createOrFindUser(userCreateOrFindGrpcRequestDTO);
    }

    public UserAuthResponseDTO authenticateUserSync(UserAuthRequestDTO userAuthRequestDTO) {
        return userServiceBlockingStub.authenticate(userAuthRequestDTO);
    }

    public ListenableFuture<UserAuthResponseDTO> authenticateUserAsync(UserAuthRequestDTO userAuthRequestDTO) {
        return userServiceFutureStub
                .withDeadlineAfter(userGrpcClientConfig.timeoutInMs(), TimeUnit.MILLISECONDS)
                .authenticate(userAuthRequestDTO);
    }
}
