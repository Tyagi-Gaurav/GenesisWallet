package com.gw.user.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO;
import com.gw.user.grpc.UserDetailsGrpcResponseDTO;
import com.gw.user.grpc.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class UserGrpcClient {
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;
    private UserServiceGrpc.UserServiceFutureStub userServiceFutureStub;

    public UserGrpcClient(UserGrpcClientConfig userGrpcClientConfig) {
        this(ManagedChannelBuilder.forAddress(userGrpcClientConfig.serviceName(),
                userGrpcClientConfig.port()).build(), userGrpcClientConfig);
    }

    UserGrpcClient(ManagedChannel managedChannel, UserGrpcClientConfig userGrpcClientConfig) {
        this.userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel)
                .withDeadlineAfter(userGrpcClientConfig.timeoutInMilli(), TimeUnit.MILLISECONDS);
        this.userServiceFutureStub = UserServiceGrpc.newFutureStub(managedChannel)
                .withDeadlineAfter(userGrpcClientConfig.timeoutInMilli(), TimeUnit.MILLISECONDS);
    }

    public UserDetailsGrpcResponseDTO fetchUsersByIdSync(FetchUserDetailsByIdGrpcRequestDTO request) {
        return userServiceBlockingStub.fetchUsersById(request);
    }

    public ListenableFuture<UserDetailsGrpcResponseDTO> fetchUsersByIdAsync(FetchUserDetailsByIdGrpcRequestDTO fetchUserDetailsByIdGrpcRequestDTO) {
        return userServiceFutureStub.fetchUsersById(fetchUserDetailsByIdGrpcRequestDTO);
    }
}
