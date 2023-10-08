package com.gw.user.grpc;

import com.gw.common.domain.ExternalUser;
import com.gw.common.metrics.EndpointMetrics;
import com.gw.grpc.common.CorrelationIdInterceptor;
import com.gw.grpc.common.MetricsInterceptor;
import com.gw.test.common.grpc.GrpcExtension;
import com.gw.user.domain.User;
import com.gw.user.service.UserService;
import com.gw.user.testutils.ExternalUserBuilder;
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

import static com.gw.user.testutils.TestUserBuilder.*;
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
    void createUser()  {
        when(userService.addUser(any(User.class))).thenReturn(Mono.empty());

        User user = aUser().build();
        UserCreateGrpcRequestDTO userCreateGrpcRequestDTO = userCreateGrpcRequestDTOBuilder(user);

        UserCreateGrpcResponseDTO userCreateGrpcResponseDTO = userServiceBlockingStub.createUser(userCreateGrpcRequestDTO);
        assertThat(userCreateGrpcResponseDTO).isNotNull();
        assertThat(userCreateGrpcResponseDTO.getCreated()).isTrue();
    }

    @Test
    void createExternalUser()  {
        when(userService.addExternalUser(any(ExternalUser.class))).thenReturn(Mono.empty());

        ExternalUser externalUser = ExternalUserBuilder.aExternalUser().build();
        ExternalUserCreateGrpcRequestDTO externalUserCreateGrpcRequestDTO =
                externalUserCreateGrpcRequestDTOBuilder(externalUser);

        ExternalUserCreateGrpcResponseDTO externalUserCreateGrpcResponseDTO =
                userServiceBlockingStub.createExternalUser(externalUserCreateGrpcRequestDTO);
        assertThat(externalUserCreateGrpcResponseDTO).isNotNull();
    }

    @Test
    void createUser_handleException()  {
        when(userService.addUser(any(User.class))).thenReturn(Mono.error(new IllegalArgumentException()));

        User user = aUser().build();
        UserCreateGrpcRequestDTO userCreateGrpcRequestDTO = userCreateGrpcRequestDTOBuilder(user);

        assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> userServiceBlockingStub.createUser(userCreateGrpcRequestDTO));
    }

    @Test
    void fetchUsersByUsername()  {
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
    void createUser_shouldRecordMetrics()  {
        when(userService.addUser(any(User.class))).thenReturn(Mono.empty());

        User user = aUser().build();
        UserCreateGrpcRequestDTO userCreateGrpcRequestDTO = userCreateGrpcRequestDTOBuilder(user);

        userServiceBlockingStub.createUser(userCreateGrpcRequestDTO);
        assertThat(meterRegistry.get("grpc_server_request_duration").meter()).isNotNull();
    }
}