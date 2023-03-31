package com.gw.user.client;

import com.gw.user.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.atomic.AtomicBoolean;

public class MockUserService extends UserServiceGrpc.UserServiceImplBase {
    private UserDetailsGrpcResponseDTO userDetailsGrpcResponseDTO;
    private final AtomicBoolean callReceived = new AtomicBoolean(false);
    private UserCreateGrpcResponseDTO userCreateGrpcResponseDTO;

    @Override
    public void createExternalUser(ExternalUserCreateGrpcRequestDTO request,
                                   StreamObserver<ExternalUserCreateGrpcResponseDTO> responseObserver) {
        callReceived.set(true);
        responseObserver.onNext(ExternalUserCreateGrpcResponseDTO.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void fetchUsersById(FetchUserDetailsByIdGrpcRequestDTO request,
                               StreamObserver<UserDetailsGrpcResponseDTO> responseObserver) {
        callReceived.set(true);
        responseObserver.onNext(userDetailsGrpcResponseDTO);
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(UserCreateGrpcRequestDTO request, StreamObserver<UserCreateGrpcResponseDTO> responseObserver) {
        callReceived.set(true);
        responseObserver.onNext(userCreateGrpcResponseDTO);
        responseObserver.onCompleted();
    }

    public void shouldReturnResponse(UserDetailsGrpcResponseDTO expectedResult) {
        this.userDetailsGrpcResponseDTO = expectedResult;
    }

    public void shouldReturnResponse(UserCreateGrpcResponseDTO userCreateGrpcResponseDTO) {
        this.userCreateGrpcResponseDTO = userCreateGrpcResponseDTO;
    }

    public boolean getCallReceived() {
        return callReceived.get();
    }
}
