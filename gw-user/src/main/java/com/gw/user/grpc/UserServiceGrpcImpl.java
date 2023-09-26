package com.gw.user.grpc;

import com.google.protobuf.Empty;
import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.Gender;
import com.gw.user.domain.User;
import com.gw.user.service.UserService;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceGrpcImpl.class);
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
                        .setUserName(user.name())
                        .setDateOfBirth(user.dateOfBirth())
                        .setHomeCountry(user.homeCountry())
                        .setId(user.id())
                        .setGender(toGrpcGender(user.gender()))
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
                toDomainGender(request.getGender()),
                request.getHomeCountry(),
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
                request.getDateOfBirth(),
                toDomainGender(request.getGender()),
                request.getHomeCountry());
    }

    private Gender toDomainGender(com.gw.user.grpc.Gender gender) {
        return switch (gender) {
            case GENDER_MALE -> Gender.MALE;
            case GENDER_FEMALE -> Gender.FEMALE;
            default -> Gender.UNSPECIFIED;
        };
    }

    private com.gw.user.grpc.Gender toGrpcGender(Gender gender) {
        return switch (gender) {
            case MALE -> com.gw.user.grpc.Gender.GENDER_MALE;
            case FEMALE -> com.gw.user.grpc.Gender.GENDER_FEMALE;
            case UNSPECIFIED -> com.gw.user.grpc.Gender.GENDER_UNSPECIFIED;
        };
    }
}
