package com.gw.user.client;

import com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO;
import com.gw.user.grpc.UserDetailsGrpcResponseDTO;
import com.gw.user.grpc.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class UserGrpcClient {
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    public UserGrpcClient(UserGrpcClientConfig userGrpcClientConfig) {
        this(ManagedChannelBuilder.forAddress(userGrpcClientConfig.serviceName(),
                userGrpcClientConfig.port()).build());
    }

    UserGrpcClient(ManagedChannel managedChannel) {
        this.userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel);
    }

    public UserDetailsGrpcResponseDTO fetchUsersById(FetchUserDetailsByIdGrpcRequestDTO request) {
        return userServiceBlockingStub.fetchUsersById(request);
    }
}
