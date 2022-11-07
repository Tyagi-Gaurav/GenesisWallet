package com.gw.user.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO;
import com.gw.user.grpc.UserDetailsGrpcResponseDTO;
import com.gw.user.grpc.UserServiceGrpc;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class UserGrpcClient {
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;
    private UserServiceGrpc.UserServiceFutureStub userServiceFutureStub;

    public UserGrpcClient(ManagedChannel managedChannel, UserGrpcClientConfig userGrpcClientConfig) {
        this(managedChannel, userGrpcClientConfig, List.of());
    }

    public  UserGrpcClient(ManagedChannel managedChannel, UserGrpcClientConfig userGrpcClientConfig,
                   List<ClientInterceptor> clientInterceptorList) {
        this.userServiceBlockingStub = UserServiceGrpc.newBlockingStub(managedChannel)
                .withDeadlineAfter(userGrpcClientConfig.timeoutInMilli(), TimeUnit.MILLISECONDS);
        this.userServiceFutureStub = UserServiceGrpc.newFutureStub(managedChannel)
                .withDeadlineAfter(userGrpcClientConfig.timeoutInMilli(), TimeUnit.MILLISECONDS)
                .withInterceptors(clientInterceptorList.toArray(new ClientInterceptor[0]));
    }

    public UserDetailsGrpcResponseDTO fetchUsersByIdSync(FetchUserDetailsByIdGrpcRequestDTO request) {
        return userServiceBlockingStub.fetchUsersById(request);
    }

    public ListenableFuture<UserDetailsGrpcResponseDTO> fetchUsersByIdAsync(FetchUserDetailsByIdGrpcRequestDTO fetchUserDetailsByIdGrpcRequestDTO) {
        return userServiceFutureStub.fetchUsersById(fetchUserDetailsByIdGrpcRequestDTO);
    }
}
