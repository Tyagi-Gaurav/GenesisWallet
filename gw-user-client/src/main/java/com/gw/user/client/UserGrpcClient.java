package com.gw.user.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.gw.user.grpc.*;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class UserGrpcClient {
    private static final Logger LOG = LoggerFactory.getLogger(UserGrpcClient.class);
    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;
    private final UserServiceGrpc.UserServiceFutureStub userServiceFutureStub;
    private final UserGrpcClientConfig userGrpcClientConfig;

    public UserGrpcClient(UserGrpcClientConfig userGrpcClientConfig) {
        this(ManagedChannelBuilder.forAddress(userGrpcClientConfig.serviceName(), userGrpcClientConfig.port())
                .usePlaintext()
                .build(),
                userGrpcClientConfig,
                List.of());
    }

    public  UserGrpcClient(ManagedChannel managedChannel, UserGrpcClientConfig userGrpcClientConfig,
                   List<ClientInterceptor> clientInterceptorList) {
        this.userGrpcClientConfig = userGrpcClientConfig;
        LOG.debug("GRPC Client Config: {}", userGrpcClientConfig );

        this.userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel)
                .withInterceptors(clientInterceptorList.toArray(new ClientInterceptor[0]));
        this.userServiceFutureStub = UserServiceGrpc.newFutureStub(managedChannel)
                .withInterceptors(clientInterceptorList.toArray(new ClientInterceptor[0]));
    }

    public UserDetailsGrpcResponseDTO fetchUsersByIdSync(FetchUserDetailsByIdGrpcRequestDTO request) {
        return userServiceBlockingStub.fetchUsersById(request);
    }

    public ListenableFuture<UserDetailsGrpcResponseDTO> fetchUsersByIdAsync(FetchUserDetailsByIdGrpcRequestDTO fetchUserDetailsByIdGrpcRequestDTO) {
        return userServiceFutureStub.fetchUsersById(fetchUserDetailsByIdGrpcRequestDTO);
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

    public void createExternalUserSync(ExternalUserCreateGrpcRequestDTO externalUserCreateGrpcRequestDTO) {
        userServiceBlockingStub.createExternalUser(externalUserCreateGrpcRequestDTO);
    }

    public ListenableFuture<ExternalUserCreateGrpcResponseDTO> createExternalUserAsync(ExternalUserCreateGrpcRequestDTO externalUserCreateGrpcRequestDTO) {
        return userServiceFutureStub
                .withDeadlineAfter(userGrpcClientConfig.timeoutInMs(), TimeUnit.MILLISECONDS)
                .createExternalUser(externalUserCreateGrpcRequestDTO);
    }
}
