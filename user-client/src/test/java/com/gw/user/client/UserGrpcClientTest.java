package com.gw.user.client;

import com.gw.test.common.grpc.GrpcExtension;
import com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO;
import com.gw.user.grpc.UserDetailsGrpcResponseDTO;
import com.gw.user.grpc.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserGrpcClientTest {
    @RegisterExtension
    private final GrpcExtension grpcExtension = new GrpcExtension();
    private UserServiceGrpc.UserServiceImplBase bindableService;
    private UserGrpcClient userGrpcClient;

    @BeforeEach
    void setUp() throws IOException {
        bindableService = mock(UserServiceGrpc.UserServiceImplBase.class);
        GrpcExtension.ServiceDetails serviceDetails = grpcExtension.createGrpcServerFor(bindableService);
        userGrpcClient = new UserGrpcClient(serviceDetails.managedChannel());
    }

    @Test
    void fetchUsersById() {
        FetchUserDetailsByIdGrpcRequestDTO fetchUserDetailsByIdGrpcRequestDTO = FetchUserDetailsByIdGrpcRequestDTO.newBuilder()
                .setId(UUID.randomUUID().toString())
                .build();
        UserDetailsGrpcResponseDTO expectedResult = UserDetailsGrpcResponseDTO.getDefaultInstance();
        doAnswer(invocation -> {
            StreamObserver<UserDetailsGrpcResponseDTO> streamObserver = invocation.getArgument(1);
            streamObserver.onNext(expectedResult);
            streamObserver.onCompleted();
            return null;
        }).when(bindableService).fetchUsersById(eq(fetchUserDetailsByIdGrpcRequestDTO), any(StreamObserver.class));

        UserDetailsGrpcResponseDTO userDetailsGrpcResponseDTO =
                userGrpcClient.fetchUsersById(fetchUserDetailsByIdGrpcRequestDTO);

        assertThat(userDetailsGrpcResponseDTO.getUserName()).isEqualTo(expectedResult.getUserName());
        assertThat(userDetailsGrpcResponseDTO.getDateOfBirth()).isEqualTo(expectedResult.getDateOfBirth());
        assertThat(userDetailsGrpcResponseDTO.getFirstName()).isEqualTo(expectedResult.getFirstName());
        assertThat(userDetailsGrpcResponseDTO.getLastName()).isEqualTo(expectedResult.getLastName());
        assertThat(userDetailsGrpcResponseDTO.getGender()).isEqualTo(expectedResult.getGender());
        assertThat(userDetailsGrpcResponseDTO.getHomeCountry()).isEqualTo(expectedResult.getHomeCountry());
        assertThat(userDetailsGrpcResponseDTO.getId()).isEqualTo(expectedResult.getId());
    }
}