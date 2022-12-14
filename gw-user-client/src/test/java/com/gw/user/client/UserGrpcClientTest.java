package com.gw.user.client;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.gw.grpc.common.CorrelationIdInterceptor;
import com.gw.test.common.grpc.GrpcExtension;
import com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO;
import com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO;
import com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO;
import com.gw.user.grpc.UserCreateGrpcRequestDTO;
import com.gw.user.grpc.UserCreateGrpcResponseDTO;
import com.gw.user.grpc.UserDetailsGrpcResponseDTO;
import com.gw.user.grpc.UserServiceGrpc;
import io.grpc.ClientInterceptor;
import io.grpc.ServerInterceptor;
import io.grpc.stub.StreamObserver;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
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
    private final ServerInterceptor correlationIdInterceptor = new CorrelationIdInterceptor();
    private final ClientInterceptor clientCorrelationIdInterceptor = new CorrelationIdInterceptor();

    @BeforeEach
    void setUp() throws IOException {
        bindableService = mock(UserServiceGrpc.UserServiceImplBase.class);
        GrpcExtension.ServiceDetails serviceDetails = grpcExtension.createGrpcServerFor(bindableService, correlationIdInterceptor);
        UserGrpcClientConfig userGrpcClientConfig = new UserGrpcClientConfig(serviceDetails.serverName(),
                0, 300);
        userGrpcClient = new UserGrpcClient(serviceDetails.managedChannel(), userGrpcClientConfig, List.of(clientCorrelationIdInterceptor));
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

    @Test
    void createUserSync() {
        AtomicBoolean successCall = new AtomicBoolean(false);
        UserCreateGrpcRequestDTO userCreateGrpcRequestDTO = UserCreateGrpcRequestDTO.newBuilder()
                .setUserName("username")
                .setPassword("password")
                .setDateOfBirth("01/01/2000")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setHomeCountry("UK")
                .build();

        doAnswer(invocation -> {
            successCall.set(true);
            StreamObserver<UserCreateGrpcResponseDTO> streamObserver = invocation.getArgument(1);
            streamObserver.onNext(UserCreateGrpcResponseDTO.getDefaultInstance());
            streamObserver.onCompleted();
            return null;
        }).when(bindableService).createUser(eq(userCreateGrpcRequestDTO), any(StreamObserver.class));

        userGrpcClient.createUserSync(userCreateGrpcRequestDTO);
        assertThat(successCall).isTrue();
    }

    @Test
    void createUserASync() {
        UserCreateGrpcRequestDTO userCreateGrpcRequestDTO = UserCreateGrpcRequestDTO.newBuilder()
                .setUserName("username")
                .setPassword("password")
                .setDateOfBirth("01/01/2000")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setHomeCountry("UK")
                .build();
        doAnswer(invocation -> {
            StreamObserver<UserCreateGrpcResponseDTO> streamObserver = invocation.getArgument(1);
            streamObserver.onNext(UserCreateGrpcResponseDTO.newBuilder()
                            .setCreated(true)
                    .build());
            streamObserver.onCompleted();
            return null;
        }).when(bindableService).createUser(eq(userCreateGrpcRequestDTO), any(StreamObserver.class));

        ListenableFuture<UserCreateGrpcResponseDTO> emptyListenableFuture =
                userGrpcClient.createUserAsync(userCreateGrpcRequestDTO);

        AtomicBoolean hasGotResponse = new AtomicBoolean(false);
        Futures.addCallback(emptyListenableFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(UserCreateGrpcResponseDTO result) {
                hasGotResponse.set(result.getCreated());
            }

            @Override
            public void onFailure(Throwable t) {
                fail("Got exception", t);
            }
        }, Executors.newSingleThreadExecutor());

        Awaitility.await("Receive response").atMost(Duration.ofMillis(300))
                .until(hasGotResponse::get);
    }

    @Test
    void createExternalUserSync() {
        AtomicBoolean successCall = new AtomicBoolean(false);
        ExternalUserCreateGrpcRequestDTO externalUserCreateGrpcRequestDTO = ExternalUserCreateGrpcRequestDTO.newBuilder()
                .setEmail("test@test.com")
                .setDateOfBirth("01/01/2000")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setHomeCountry("UK")
                .build();

        doAnswer(invocation -> {
            successCall.set(true);
            StreamObserver<ExternalUserCreateGrpcResponseDTO> streamObserver = invocation.getArgument(1);
            streamObserver.onNext(ExternalUserCreateGrpcResponseDTO.getDefaultInstance());
            streamObserver.onCompleted();
            return null;
        }).when(bindableService).createExternalUser(eq(externalUserCreateGrpcRequestDTO), any(StreamObserver.class));

        userGrpcClient.createExternalUserSync(externalUserCreateGrpcRequestDTO);
        assertThat(successCall).isTrue();
    }

    @Test
    void createExternalUserASync() {
        ExternalUserCreateGrpcRequestDTO externalUserCreateGrpcRequestDTO = ExternalUserCreateGrpcRequestDTO.newBuilder()
                .setEmail("test@test.com")
                .setDateOfBirth("01/01/2000")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setHomeCountry("UK")
                .build();
        doAnswer(invocation -> {
            StreamObserver<ExternalUserCreateGrpcResponseDTO> streamObserver = invocation.getArgument(1);
            streamObserver.onNext(ExternalUserCreateGrpcResponseDTO.newBuilder()
                    .build());
            streamObserver.onCompleted();
            return null;
        }).when(bindableService).createExternalUser(eq(externalUserCreateGrpcRequestDTO), any(StreamObserver.class));

        ListenableFuture<ExternalUserCreateGrpcResponseDTO> emptyListenableFuture =
                userGrpcClient.createExternalUserAsync(externalUserCreateGrpcRequestDTO);

        AtomicBoolean hasGotResponse = new AtomicBoolean(false);
        Futures.addCallback(emptyListenableFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(ExternalUserCreateGrpcResponseDTO result) {
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