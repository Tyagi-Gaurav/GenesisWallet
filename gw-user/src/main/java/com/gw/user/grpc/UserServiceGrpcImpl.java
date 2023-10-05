package com.gw.user.grpc;

import com.google.protobuf.Empty;
import com.gw.common.domain.ExternalUser;
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
    public void fetchUsersById(FetchUserDetailsByIdGrpcRequestDTO request, StreamObserver<UserDetailsGrpcResponseDTO> responseObserver) {
        LOG.info("Inside GRPC fetch Users By Id");
        userService.findUserBy(UUID.fromString(request.getId()))
                .map(user -> UserDetailsGrpcResponseDTO.newBuilder()
                        .setFirstName(user.firstName())
                        .setLastName(user.lastName())
                        .setUserName(user.userName())
                        .setDateOfBirth(user.dateOfBirth())
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
    public void createExternalUser(ExternalUserCreateGrpcRequestDTO request, StreamObserver<ExternalUserCreateGrpcResponseDTO> responseObserver) {
        LOG.info("Inside GRPC create external user");
        userService.addExternalUser(createExternalUserFrom(request))
                .map(v -> Empty.getDefaultInstance())
                .switchIfEmpty(Mono.defer(() -> Mono.just(Empty.getDefaultInstance())))
                .doOnError(responseObserver::onError)
                .subscribe(v -> {
                    responseObserver.onNext(ExternalUserCreateGrpcResponseDTO.newBuilder()
                            .build());
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
                request.getDateOfBirth(),
                "REGISTERED_USER");
    }

    private ExternalUser createExternalUserFrom(ExternalUserCreateGrpcRequestDTO request) {
        return new ExternalUser(UUID.randomUUID(),
                request.getEmail(),
                request.getLocale(),
                request.getPictureUrl(),
                request.getFirstName(),
                request.getLastName(),
                request.getTokenValue(),
                request.getTokenType(),
                request.getTokenExpiryTime(),
                request.getExternalSystem(),
                request.getDateOfBirth());
    }
}
