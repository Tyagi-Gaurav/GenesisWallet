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

    private ExternalUser createExternalUserFrom(UserCreateOrFindGrpcRequestDTO request) {
        return new ExternalUser(
                request.getUserName(),
                request.getExtsource().name());
    }
}
