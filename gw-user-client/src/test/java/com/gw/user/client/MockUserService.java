package com.gw.user.client;

import com.gw.user.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.atomic.AtomicBoolean;

public class MockUserService extends UserServiceGrpc.UserServiceImplBase {
    private UserDetailsGrpcResponseDTO userDetailsGrpcResponseDTO;
    private final AtomicBoolean callReceived = new AtomicBoolean(false);
    private UserCreateGrpcResponseDTO userCreateGrpcResponseDTO;
    private UserAuthResponseDTO userAuthResponseDTO;

    @Override
    public void createExternalUser(ExternalUserCreateGrpcRequestDTO request,
                                   StreamObserver<ExternalUserCreateGrpcResponseDTO> responseObserver) {
        callReceived.set(true);
        responseObserver.onNext(ExternalUserCreateGrpcResponseDTO.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void fetchUsersByUserName(FetchUserDetailsByUserNameGrpcRequestDTO request,
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

    @Override
    public void authenticate(UserAuthRequestDTO request, StreamObserver<UserAuthResponseDTO> responseObserver) {
        callReceived.set(true);
        responseObserver.onNext(userAuthResponseDTO);
        responseObserver.onCompleted();
    }

    public void shouldReturnResponse(UserDetailsGrpcResponseDTO expectedResult) {
        this.userDetailsGrpcResponseDTO = expectedResult;
    }

    public void shouldReturnResponse(UserCreateGrpcResponseDTO userCreateGrpcResponseDTO) {
        this.userCreateGrpcResponseDTO = userCreateGrpcResponseDTO;
    }

    public void shouldReturnResponse(UserAuthResponseDTO userAuthResponseDTO) {
        this.userAuthResponseDTO = userAuthResponseDTO;
    }

    public boolean getCallReceived() {
        return callReceived.get();
    }
}
