package com.gw.user.client;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.gw.test.common.grpc.GrpcExtension;
import com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO;
import com.gw.user.grpc.UserDetailsGrpcResponseDTO;
import com.gw.user.grpc.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
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
    private UserGrpcClientConfig userGrpcClientConfig;

    @BeforeEach
    void setUp() throws IOException {
        bindableService = mock(UserServiceGrpc.UserServiceImplBase.class);
        GrpcExtension.ServiceDetails serviceDetails = grpcExtension.createGrpcServerFor(bindableService);
        userGrpcClientConfig = new UserGrpcClientConfig(serviceDetails.serverName(),
                0, 300);
        userGrpcClient = new UserGrpcClient(serviceDetails.managedChannel(), userGrpcClientConfig);
    }

    @Test
    void fetchUsersByIdSync() {
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
                userGrpcClient.fetchUsersByIdSync(fetchUserDetailsByIdGrpcRequestDTO);

        assertThat(userDetailsGrpcResponseDTO.getUserName()).isEqualTo(expectedResult.getUserName());
        assertThat(userDetailsGrpcResponseDTO.getDateOfBirth()).isEqualTo(expectedResult.getDateOfBirth());
        assertThat(userDetailsGrpcResponseDTO.getFirstName()).isEqualTo(expectedResult.getFirstName());
        assertThat(userDetailsGrpcResponseDTO.getLastName()).isEqualTo(expectedResult.getLastName());
        assertThat(userDetailsGrpcResponseDTO.getGender()).isEqualTo(expectedResult.getGender());
        assertThat(userDetailsGrpcResponseDTO.getHomeCountry()).isEqualTo(expectedResult.getHomeCountry());
        assertThat(userDetailsGrpcResponseDTO.getId()).isEqualTo(expectedResult.getId());
    }

    @Test
    void fetchUsersByIdAsync() {
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

        ListenableFuture<UserDetailsGrpcResponseDTO> userDetailsGrpcResponseDTOFuture =
                userGrpcClient.fetchUsersByIdAsync(fetchUserDetailsByIdGrpcRequestDTO);

        AtomicBoolean hasGotResponse = new AtomicBoolean(false);
        Futures.addCallback(userDetailsGrpcResponseDTOFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(UserDetailsGrpcResponseDTO result) {
                assertThat(result.getUserName()).isEqualTo(expectedResult.getUserName());
                assertThat(result.getDateOfBirth()).isEqualTo(expectedResult.getDateOfBirth());
                assertThat(result.getFirstName()).isEqualTo(expectedResult.getFirstName());
                assertThat(result.getLastName()).isEqualTo(expectedResult.getLastName());
                assertThat(result.getGender()).isEqualTo(expectedResult.getGender());
                assertThat(result.getHomeCountry()).isEqualTo(expectedResult.getHomeCountry());
                assertThat(result.getId()).isEqualTo(expectedResult.getId());
                hasGotResponse.set(true);
            }

            @Override
            public void onFailure(Throwable t) {
                fail("Got exception", t);
            }
        }, Executors.newSingleThreadExecutor());

        Awaitility.await("Receive response").atMost(Duration.ofMillis(300))
                .until(hasGotResponse::get);
    }
}