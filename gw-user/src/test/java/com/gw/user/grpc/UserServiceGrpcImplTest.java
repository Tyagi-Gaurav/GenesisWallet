package com.gw.user.grpc;

import com.gw.common.grpc.Error;
import com.gw.common.metrics.EndpointMetrics;
import com.gw.grpc.common.CorrelationIdInterceptor;
import com.gw.grpc.common.MetricsInterceptor;
import com.gw.test.common.grpc.GrpcExtension;
import com.gw.user.domain.ExternalUser2;
import com.gw.user.domain.ExternalUser2Builder;
import com.gw.user.domain.User;
import com.gw.user.service.UserService;
import io.grpc.ServerInterceptor;
import io.grpc.StatusRuntimeException;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static com.gw.user.testutils.TestUserBuilder.aUser;
import static com.gw.user.testutils.TestUserBuilder.userCreateGrpcRequestDTOBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceGrpcImplTest {
    @RegisterExtension
    private final GrpcExtension grpcExtension = new GrpcExtension();
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;
    private UserService userService;
    private final MeterRegistry meterRegistry = new SimpleMeterRegistry();
    private final EndpointMetrics endpointMetrics = new EndpointMetrics(meterRegistry);
    private final MetricsInterceptor metricsInterceptor = new MetricsInterceptor(endpointMetrics);
    private final ServerInterceptor correlationIdInterceptor = new CorrelationIdInterceptor();

    @BeforeEach
    void setUp() throws IOException {
        userService = Mockito.mock(UserService.class);
        UserServiceGrpcImpl bindableService = new UserServiceGrpcImpl(userService);
        GrpcExtension.ServiceDetails serviceDetails = grpcExtension.createGrpcServerFor(bindableService,
                correlationIdInterceptor, metricsInterceptor);
        userServiceBlockingStub = UserServiceGrpc.newBlockingStub(serviceDetails.managedChannel());
    }

    @Test
    void createUser() {
        when(userService.addUser(any(User.class))).thenReturn(Mono.empty());

        User user = aUser().build();
        UserCreateGrpcRequestDTO userCreateGrpcRequestDTO = userCreateGrpcRequestDTOBuilder(user);

        UserCreateGrpcResponseDTO userCreateGrpcResponseDTO = userServiceBlockingStub.createUser(userCreateGrpcRequestDTO);
        assertThat(userCreateGrpcResponseDTO).isNotNull();
        assertThat(userCreateGrpcResponseDTO.getCreated()).isTrue();
    }

    @Test
    void createExternalUser() {
        when(userService.addExternalUser(any(ExternalUser2.class))).thenReturn(Mono.empty());

        ExternalUser2 externalUser = ExternalUser2Builder.newBuilder().build();

        UserCreateOrFindGrpcResponseDTO response = userServiceBlockingStub.createOrFindUser(UserCreateOrFindGrpcRequestDTO.newBuilder()
                .setUserName("some-user-name")
                .setExtsource(ExternalSystem.GOOGLE)
                .build());

        assertThat(response).isNotNull();
    }

    @Test
    void createUser_handleException() {
        when(userService.addUser(any(User.class))).thenReturn(Mono.error(new IllegalArgumentException()));

        User user = aUser().build();
        UserCreateGrpcRequestDTO userCreateGrpcRequestDTO = userCreateGrpcRequestDTOBuilder(user);

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> userServiceBlockingStub.createUser(userCreateGrpcRequestDTO));
    }

    @Test
    void fetchUsersByUsername() {
        User user = aUser().build();
        when(userService.findUserBy(user.userName())).thenReturn(Mono.just(user));

        FetchUserDetailsByUserNameGrpcRequestDTO fetchUserDetailsByUserNameGrpcRequestDTO = FetchUserDetailsByUserNameGrpcRequestDTO.newBuilder()
                .setUserName(user.userName())
                .build();

        UserDetailsGrpcResponseDTO userDetailsGrpcResponseDTO =
                userServiceBlockingStub.fetchUsersByUserName(fetchUserDetailsByUserNameGrpcRequestDTO);

        assertThat(userDetailsGrpcResponseDTO.getUserName()).isEqualTo(user.userName());
        assertThat(userDetailsGrpcResponseDTO.getFirstName()).isEqualTo(user.firstName());
        assertThat(userDetailsGrpcResponseDTO.getLastName()).isEqualTo(user.lastName());
        assertThat(userDetailsGrpcResponseDTO.getId()).isEqualTo(user.id().toString());
    }

    @Test
    void authenticateUserSuccess() {
        User user = aUser().build();
        when(userService.authenticateUser(user.userName(), user.password())).thenReturn(Mono.just(user));

        UserAuthRequestDTO userAuthRequestDTO = UserAuthRequestDTO.newBuilder()
                .setUserName(user.userName())
                .setPassword(user.password())
                .build();

        UserAuthResponseDTO userAuthResponseDTO = userServiceBlockingStub.authenticate(userAuthRequestDTO);

        assertThat(userAuthResponseDTO.getAuthDetails().getFirstName()).isEqualTo(user.firstName());
        assertThat(userAuthResponseDTO.getAuthDetails().getLastName()).isEqualTo(user.lastName());
    }

    @Test
    void authenticateUserInvalidCredentials() {
        User user = aUser().build();
        when(userService.authenticateUser(user.userName(), user.password()))
                .thenReturn(Mono.empty());

        UserAuthRequestDTO userAuthRequestDTO = UserAuthRequestDTO.newBuilder()
                .setUserName(user.userName())
                .setPassword(user.password())
                .build();

        UserAuthResponseDTO userAuthResponseDTO = userServiceBlockingStub.authenticate(userAuthRequestDTO);

        assertThat(userAuthResponseDTO.getEitherCase()).isEqualTo(UserAuthResponseDTO.EitherCase.ERROR);
        assertThat(userAuthResponseDTO.getError()).isEqualTo(com.gw.common.grpc.Error.newBuilder()
                .setCode(Error.ErrorCode.AUTHENTICATION_ERROR)
                .setDescription("Invalid Credentials")
                .build());
    }

    @Test
    void authenticateUserError() {
        User user = aUser().build();
        when(userService.authenticateUser(user.userName(), user.password()))
                .thenReturn(Mono.error(new RuntimeException()));

        UserAuthRequestDTO userAuthRequestDTO = UserAuthRequestDTO.newBuilder()
                .setUserName(user.userName())
                .setPassword(user.password())
                .build();

        UserAuthResponseDTO userAuthResponseDTO = userServiceBlockingStub.authenticate(userAuthRequestDTO);

        assertThat(userAuthResponseDTO.getEitherCase()).isEqualTo(UserAuthResponseDTO.EitherCase.ERROR);
        assertThat(userAuthResponseDTO.getError()).isEqualTo(com.gw.common.grpc.Error.newBuilder()
                .setCode(Error.ErrorCode.INTERNAL_SYSTEM_ERROR)
                .setDescription("Internal error occurred. Please try again later.")
                .build());
    }

    @Test
    void createUser_shouldRecordMetrics() {
        when(userService.addUser(any(User.class))).thenReturn(Mono.empty());

        User user = aUser().build();
        UserCreateGrpcRequestDTO userCreateGrpcRequestDTO = userCreateGrpcRequestDTOBuilder(user);

        userServiceBlockingStub.createUser(userCreateGrpcRequestDTO);
        assertThat(meterRegistry.get("grpc_server_request_duration").meter()).isNotNull();
    }
}