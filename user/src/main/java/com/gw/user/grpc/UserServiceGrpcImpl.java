package com.gw.user.grpc;

import com.google.protobuf.Empty;
import com.gw.common.domain.Gender;
import com.gw.common.domain.User;
import com.gw.user.grpc.UserServiceGrpc.UserServiceImplBase;
import com.gw.user.service.UserService;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class UserServiceGrpcImpl extends UserServiceImplBase {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceGrpcImpl.class);
    private final UserService userService;

    public UserServiceGrpcImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void fetchUsersById(FetchUserDetailsByIdGrpcRequestDTO request, StreamObserver<UserDetailsGrpcResponseDTO> responseObserver) {
        userService.findUserBy(UUID.fromString(request.getId()))
                .map(user -> UserDetailsGrpcResponseDTO.newBuilder()
                        .setFirstName(user.firstName())
                        .setLastName(user.lastName())
                        .setUserName(user.username())
                        .setDateOfBirth(user.dateOfBirth())
                        .setHomeCountry(user.homeCountry())
                        .setId(user.id().toString())
                        .setGender(toGrpcGender(user.gender()))
                        .build())
                .subscribe(resp -> {
                    responseObserver.onNext(resp);
                    responseObserver.onCompleted();
                });
    }

    @Override
    public void createUser(UserCreateGrpcRequestDTO request, StreamObserver<Empty> responseObserver) {
        LOG.info("Inside GRPC create user");
        userService.addUser(createUserFrom(request))
                .map((v) -> Empty.getDefaultInstance())
                .switchIfEmpty(Mono.defer(() -> Mono.just(Empty.getDefaultInstance())))
                .doOnError(responseObserver::onError) //TODO Throw external exception and send error payload.
                .subscribe(v -> {
                    responseObserver.onNext(Empty.newBuilder().build());
                    responseObserver.onCompleted();
                });
    }

    private User createUserFrom(UserCreateGrpcRequestDTO request) {
        return new User(UUID.randomUUID(),
                request.getFirstName(),
                request.getLastName(),
                request.getUserName(),
                request.getPassword(),
                request.getDateOfBirth(),
                toDomainGender(request.getGender()),
                request.getHomeCountry(),
                "REGISTERED_USER");
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
