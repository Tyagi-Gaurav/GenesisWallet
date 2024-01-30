package com.gw.user.grpc;

import com.google.protobuf.Empty;
import com.gw.user.domain.ExternalUser;
import com.gw.user.domain.User;
import com.gw.user.service.UserService;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {
    private static final Logger LOG = LogManager.getLogger("APP");
    private final UserService userService;

    public UserServiceGrpcImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void fetchUsersByUserName(FetchUserDetailsByUserNameGrpcRequestDTO request,
                                     StreamObserver<UserDetailsGrpcResponseDTO> responseObserver) {
        LOG.info("Inside GRPC fetch Users By Id");
        userService.findUserBy(request.getUserName())
                .map(user -> UserDetailsGrpcResponseDTO.newBuilder()
                        .setFirstName(user.firstName())
                        .setLastName(user.lastName())
                        .setUserName(user.userName())
                        .setId(user.id())
                        .build())
                .subscribe(resp -> {
                    responseObserver.onNext(resp);
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void createUser(UserCreateGrpcRequestDTO request, StreamObserver<UserCreateGrpcResponseDTO> responseObserver) {
        LOG.info("Inside GRPC create user");
        userService.addUser(createUserFrom(request))
                .map(v -> Empty.getDefaultInstance())
                .switchIfEmpty(Mono.defer(() -> Mono.just(Empty.getDefaultInstance())))
                .doOnError(responseObserver::onError)
                .subscribe(v -> {
                    responseObserver.onNext(UserCreateGrpcResponseDTO.newBuilder()
                            .setCreated(true)
                            .build());
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void createOrFindUser(UserCreateOrFindGrpcRequestDTO request, StreamObserver<UserCreateOrFindGrpcResponseDTO> responseObserver) {
        LOG.info("Inside GRPC create external user");
        userService.addExternalUser(createExternalUserFrom(request))
                .map(resp -> UserCreateOrFindGrpcResponseDTO.newBuilder()
                        .setUser(OauthUser.newBuilder()
                                .setUserName(resp.userName())
                                .build())
                        .build())
                .doOnError(responseObserver::onError)
                .subscribe(resp -> {
                    responseObserver.onNext(resp);
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void authenticate(UserAuthRequestDTO request, StreamObserver<UserAuthResponseDTO> responseObserver) {
        LOG.info("Inside GRPC authenticate");
        userService.authenticateUser(request.getUserName(), request.getPassword())
                .map(ui -> UserAuthResponseDTO.newBuilder()
                        .setAuthDetails(UserAuthDetailsDTO.newBuilder()
                                .setFirstName(ui.firstName())
                                .setLastName(ui.lastName())
                                .build())
                        .build())
                .switchIfEmpty(Mono.defer(() -> Mono.just(
                        UserAuthResponseDTO.newBuilder()
                                .setError(com.gw.common.grpc.GenesisError.newBuilder()
                                        .setCode(com.gw.common.grpc.GenesisError.ErrorCode.AUTHENTICATION_ERROR)
                                        .setDescription("Invalid Credentials")
                                        .build())
                                .build()
                )))
                .onErrorResume(throwable -> Mono.just(UserAuthResponseDTO.newBuilder()
                        .setError(com.gw.common.grpc.GenesisError.newBuilder()
                                .setCode(com.gw.common.grpc.GenesisError.ErrorCode.INTERNAL_SYSTEM_ERROR)
                                .setDescription("Internal error occurred. Please try again later.")
                                .build())
                        .build()))
                .subscribe(v -> {
                    responseObserver.onNext(v);
                    responseObserver.onCompleted();
                });
    }

    private User createUserFrom(UserCreateGrpcRequestDTO request) {
        return new User(UUID.randomUUID(),
                request.getFirstName(),
                request.getLastName(),
                request.getUserName(),
                request.getPassword(),
                null,
                "REGISTERED_USER");
    }

    private ExternalUser createExternalUserFrom(UserCreateOrFindGrpcRequestDTO request) {
        return new ExternalUser(
                request.getUserName(),
                request.getExtsource().name());
    }
}
