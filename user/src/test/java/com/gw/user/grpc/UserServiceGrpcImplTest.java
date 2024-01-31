package com.gw.user.grpc;

import com.gw.common.metrics.EndpointMetrics;
import com.gw.grpc.common.CorrelationIdInterceptor;
import com.gw.grpc.common.MetricsInterceptor;
import com.gw.test.common.grpc.GrpcExtension;
import com.gw.user.domain.ExternalUser;
import com.gw.user.domain.ExternalUserBuilder;
import com.gw.user.domain.User;
import com.gw.user.service.UserService;
import io.grpc.ServerInterceptor;
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
import static org.assertj.core.api.Assertions.assertThat;
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
    void createExternalUser() {
        when(userService.addExternalUser(any(ExternalUser.class))).thenReturn(Mono.just(ExternalUserBuilder
                .newBuilder()
                .withUserName("some-user-name")
                .build()));

        UserCreateOrFindGrpcResponseDTO response = userServiceBlockingStub.createOrFindUser(UserCreateOrFindGrpcRequestDTO.newBuilder()
                .setUserName("some-user-name")
                .setExtsource(ExternalSystem.GOOGLE)
                .build());

        assertThat(response).isNotNull();
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
}