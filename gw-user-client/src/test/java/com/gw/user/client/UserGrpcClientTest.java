package com.gw.user.client;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
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
import java.util.UUID;
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
        FetchUserDetailsByIdGrpcRequestDTO fetchUserDetailsByIdGrpcRequestDTO = FetchUserDetailsByIdGrpcRequestDTO.newBuilder()
                .setId(UUID.randomUUID().toString())
                .build();
        UserDetailsGrpcResponseDTO expectedResult = UserDetailsGrpcResponseDTO.getDefaultInstance();
        mockUserService.shouldReturnResponse(expectedResult);
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
    void fetchUsersByIdSync_shouldGenerateClientMetric() {
        FetchUserDetailsByIdGrpcRequestDTO fetchUserDetailsByIdGrpcRequestDTO = FetchUserDetailsByIdGrpcRequestDTO.newBuilder()
                .setId(UUID.randomUUID().toString())
                .build();
        UserDetailsGrpcResponseDTO expectedResult = UserDetailsGrpcResponseDTO.getDefaultInstance();
        mockUserService.shouldReturnResponse(expectedResult);
        userGrpcClient.fetchUsersByIdSync(fetchUserDetailsByIdGrpcRequestDTO);
        assertThat(meterRegistry.get("grpc_client_request_duration").tag("fullMethod", "com.gw.user.grpc.UserService/fetchUsersById").meter()).isNotNull();
    }

    @Test
    void fetchUsersByIdAsync() {
        FetchUserDetailsByIdGrpcRequestDTO fetchUserDetailsByIdGrpcRequestDTO = FetchUserDetailsByIdGrpcRequestDTO.newBuilder()
                .setId(UUID.randomUUID().toString())
                .build();
        UserDetailsGrpcResponseDTO expectedResult = UserDetailsGrpcResponseDTO.getDefaultInstance();
        mockUserService.shouldReturnResponse(expectedResult);
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
        UserCreateGrpcRequestDTO userCreateGrpcRequestDTO = UserCreateGrpcRequestDTO.newBuilder()
                .setUserName("username")
                .setPassword("password")
                .setDateOfBirth("01/01/2000")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setHomeCountry("UK")
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
                .setDateOfBirth("01/01/2000")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setHomeCountry("UK")
                .build();
        mockUserService.shouldReturnResponse(UserCreateGrpcResponseDTO.newBuilder()
                .setCreated(true).buildPartial());

        ListenableFuture<UserCreateGrpcResponseDTO> emptyListenableFuture =
                userGrpcClient.createUserAsync(userCreateGrpcRequestDTO);

        AtomicBoolean hasGotResponse = new AtomicBoolean(false);
        Futures.addCallback(emptyListenableFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(UserCreateGrpcResponseDTO result) {
                System.out.println("Response received: " + result.getCreated());
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
                .setDateOfBirth("01/01/2000")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setHomeCountry("UK")
                .build();

        userGrpcClient.createExternalUserSync(externalUserCreateGrpcRequestDTO);
        assertThat(mockUserService.getCallReceived()).isTrue();
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