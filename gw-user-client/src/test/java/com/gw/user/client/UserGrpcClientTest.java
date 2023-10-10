package com.gw.user.client;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.gw.common.grpc.Error;
import com.gw.common.metrics.EndpointMetrics;
import com.gw.grpc.common.CorrelationIdInterceptor;
import com.gw.grpc.common.MetricsInterceptor;
import com.gw.test.common.grpc.GrpcExtension;
import com.gw.user.grpc.*;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.grpc.ClientInterceptor;
import io.grpc.ServerInterceptor;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class UserGrpcClientTest {
    @RegisterExtension
    private final GrpcExtension grpcExtension = new GrpcExtension();
    private MockUserService mockUserService;
    private UserGrpcClient userGrpcClient;
    private final ServerInterceptor correlationIdInterceptor = new CorrelationIdInterceptor();
    private final ClientInterceptor clientCorrelationIdInterceptor = new CorrelationIdInterceptor();
    private final MeterRegistry meterRegistry = new SimpleMeterRegistry();
    private final EndpointMetrics endpointMetrics = new EndpointMetrics(meterRegistry);
    private final MetricsInterceptor metricsInterceptor = new MetricsInterceptor(endpointMetrics);

    @BeforeEach
    void setUp() throws IOException {
        mockUserService = new MockUserService();
        GrpcExtension.ServiceDetails serviceDetails = grpcExtension.createGrpcServerFor(mockUserService, correlationIdInterceptor,
                metricsInterceptor);
        UserGrpcClientConfig userGrpcClientConfig = new UserGrpcClientConfig(serviceDetails.serverName(),
                0, 300, "userCircuitBreaker");
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(Map.of("userCircuitBreaker", CircuitBreakerConfig.ofDefaults()));
        userGrpcClient = new UserGrpcClient(serviceDetails.managedChannel(), userGrpcClientConfig,
                List.of(clientCorrelationIdInterceptor, metricsInterceptor), circuitBreakerRegistry, meterRegistry);
    }

    @Test
    void fetchUsersByIdSync() {
        FetchUserDetailsByUserNameGrpcRequestDTO fetchUserDetailsByUserNameGrpcRequestDTO = FetchUserDetailsByUserNameGrpcRequestDTO.newBuilder()
                .setUserName("some-user-name")
                .build();
        UserDetailsGrpcResponseDTO expectedResult = UserDetailsGrpcResponseDTO.getDefaultInstance();
        mockUserService.shouldReturnResponse(expectedResult);
        UserDetailsGrpcResponseDTO userDetailsGrpcResponseDTO =
                userGrpcClient.fetchUsersByUserNameSync(fetchUserDetailsByUserNameGrpcRequestDTO);

        assertThat(userDetailsGrpcResponseDTO.getUserName()).isEqualTo(expectedResult.getUserName());
        assertThat(userDetailsGrpcResponseDTO.getFirstName()).isEqualTo(expectedResult.getFirstName());
        assertThat(userDetailsGrpcResponseDTO.getLastName()).isEqualTo(expectedResult.getLastName());
        assertThat(userDetailsGrpcResponseDTO.getId()).isEqualTo(expectedResult.getId());
    }

    @Test
    void fetchUsersByIdSync_shouldGenerateClientMetric() {
        FetchUserDetailsByUserNameGrpcRequestDTO fetchUserDetailsByUserNameGrpcRequestDTO = FetchUserDetailsByUserNameGrpcRequestDTO.newBuilder()
                .setUserName("some-user-name")
                .build();
        UserDetailsGrpcResponseDTO expectedResult = UserDetailsGrpcResponseDTO.getDefaultInstance();
        mockUserService.shouldReturnResponse(expectedResult);
        userGrpcClient.fetchUsersByUserNameSync(fetchUserDetailsByUserNameGrpcRequestDTO);
        assertThat(meterRegistry.get("grpc_client_request_duration").tag("fullMethod", "com.gw.user.grpc.UserService/fetchUsersByUserName").meter()).isNotNull();
    }

    @Test
    void fetchUsersByIdAsync() {
        FetchUserDetailsByUserNameGrpcRequestDTO fetchUserDetailsByUserNameGrpcRequestDTO = FetchUserDetailsByUserNameGrpcRequestDTO.newBuilder()
                .setUserName("some-user-name")
                .build();
        UserDetailsGrpcResponseDTO expectedResult = UserDetailsGrpcResponseDTO.getDefaultInstance();
        mockUserService.shouldReturnResponse(expectedResult);
        ListenableFuture<UserDetailsGrpcResponseDTO> userDetailsGrpcResponseDTOFuture =
                userGrpcClient.fetchUsersByIdAsync(fetchUserDetailsByUserNameGrpcRequestDTO);

        AtomicBoolean hasGotResponse = new AtomicBoolean(false);
        Futures.addCallback(userDetailsGrpcResponseDTOFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(UserDetailsGrpcResponseDTO result) {
                assertThat(result.getUserName()).isEqualTo(expectedResult.getUserName());
                assertThat(result.getFirstName()).isEqualTo(expectedResult.getFirstName());
                assertThat(result.getLastName()).isEqualTo(expectedResult.getLastName());
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
        UserCreateGrpcRequestDTO userCreateGrpcRequestDTO = UserCreateGrpcRequestDTO.newBuilder()
                .setUserName("username")
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .build();

        mockUserService.shouldReturnResponse(UserCreateGrpcResponseDTO.getDefaultInstance());
        userGrpcClient.createUserSync(userCreateGrpcRequestDTO);
        assertThat(mockUserService.getCallReceived()).isTrue();
    }

    @Test
    void createUserASync() {
        UserCreateGrpcRequestDTO userCreateGrpcRequestDTO = UserCreateGrpcRequestDTO.newBuilder()
                .setUserName("username")
                .setPassword("password")
                .setFirstName("firstName")
                .setLastName("lastName")
                .build();
        mockUserService.shouldReturnResponse(UserCreateGrpcResponseDTO.newBuilder()
                .setCreated(true).buildPartial());

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

        Awaitility.await("Receive response").atMost(Duration.ofMillis(500))
                .until(hasGotResponse::get);
    }

    @Test
    void createExternalUserSync() {
        ExternalUserCreateGrpcRequestDTO externalUserCreateGrpcRequestDTO = ExternalUserCreateGrpcRequestDTO.newBuilder()
                .setEmail("test@test.com")
                .setFirstName("firstName")
                .setLastName("lastName")
                .build();

        userGrpcClient.createExternalUserSync(externalUserCreateGrpcRequestDTO);
        assertThat(mockUserService.getCallReceived()).isTrue();
    }

    @Test
    void createExternalUserASync() {
        ExternalUserCreateGrpcRequestDTO externalUserCreateGrpcRequestDTO = ExternalUserCreateGrpcRequestDTO.newBuilder()
                .setEmail("test@test.com")
                .setFirstName("firstName")
                .setLastName("lastName")
                .build();
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

    @Test
    void authenticateUserSync() {
        final var userAuthRequestDTO = UserAuthRequestDTO.newBuilder()
                .setUserName("someUserName")
                .setPassword("someencryptedPassword")
                .build();

        mockUserService.shouldReturnResponse(UserAuthResponseDTO.newBuilder()
                .setAuthDetails(UserAuthDetailsDTO.newBuilder()
                        .setFirstName("some-first-name")
                        .setLastName("some-last-name")
                        .build())
                .build());

        final var userAuthResponseDTO = userGrpcClient.authenticateUserSync(userAuthRequestDTO);

        assertThat(userAuthResponseDTO.getEitherCase()).isEqualTo(UserAuthResponseDTO.EitherCase.AUTHDETAILS);
        assertThat(userAuthResponseDTO.getAuthDetails().getFirstName()).isEqualTo("some-first-name");
        assertThat(userAuthResponseDTO.getAuthDetails().getLastName()).isEqualTo("some-last-name");
        assertThat(mockUserService.getCallReceived()).isTrue();
    }

    @Test
    void authenticateUserAsync() {
        final var userAuthRequestDTO = UserAuthRequestDTO.newBuilder()
                .setUserName("someUserName")
                .setPassword("someencryptedPassword")
                .build();

        mockUserService.shouldReturnResponse(UserAuthResponseDTO.newBuilder()
                .setAuthDetails(UserAuthDetailsDTO.newBuilder()
                        .setFirstName("some-first-name")
                        .setLastName("some-last-name")
                        .build())
                .build());

        ListenableFuture<UserAuthResponseDTO> userAuthResponseDTOListenableFuture =
                userGrpcClient.authenticateUserAsync(userAuthRequestDTO);

        AtomicBoolean hasGotResponse = new AtomicBoolean(false);
        Futures.addCallback(userAuthResponseDTOListenableFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(UserAuthResponseDTO result) {
                assertThat(result.getAuthDetails().getFirstName()).isEqualTo("some-first-name");
                assertThat(result.getAuthDetails().getLastName()).isEqualTo("some-last-name");
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
    void authenticateUserSyncWithError() {
        final var userAuthRequestDTO = UserAuthRequestDTO.newBuilder()
                .setUserName("someUserName")
                .setPassword("someencryptedPassword")
                .build();

        mockUserService.shouldReturnResponse(UserAuthResponseDTO.newBuilder()
                .setError(Error.newBuilder()
                        .setCode(Error.ErrorCode.AUTHENTICATION_ERROR)
                        .setDescription("Invalid Credentials")
                        .build())
                .build());

        final var userAuthResponseDTO = userGrpcClient.authenticateUserSync(userAuthRequestDTO);
        assertThat(userAuthResponseDTO.getEitherCase()).isEqualTo(UserAuthResponseDTO.EitherCase.ERROR);
        assertThat(userAuthResponseDTO.getError()).isEqualTo(Error.newBuilder()
                .setCode(Error.ErrorCode.AUTHENTICATION_ERROR)
                .setDescription("Invalid Credentials")
                .build());
        assertThat(mockUserService.getCallReceived()).isTrue();
    }
}