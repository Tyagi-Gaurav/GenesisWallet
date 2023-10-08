package com.gw.ui.service;

import com.google.common.util.concurrent.Futures;
import com.gw.ui.service.domain.ExternalUserConnectRequest;
import com.gw.user.client.UserGrpcClient;
import com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO;
import com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserGrpcClient userGrpcClient;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userGrpcClient);
    }

    @Test
    void shouldCallCreateExternalUserWhenGettingDetails() {
        ExternalUserCreateGrpcResponseDTO externalUserCreateGrpcResponseDTO = ExternalUserCreateGrpcResponseDTO.newBuilder()
                .build();
        Instant tokenExpiryTime = Instant.now().plusSeconds(10);
        ExternalUserCreateGrpcRequestDTO request = ExternalUserCreateGrpcRequestDTO.newBuilder()
                .setEmail("userName")
                .setFirstName("firstName")
                .setLastName("lastName")
                .setTokenExpiryTime(tokenExpiryTime.toEpochMilli())
                .build();
        when(userGrpcClient.createExternalUserAsync(request)).thenReturn(Futures.immediateFuture(externalUserCreateGrpcResponseDTO));

        StepVerifier.create(userService.connectUser(new ExternalUserConnectRequest("accessToken", tokenExpiryTime, "tokenType", "userName", "locale", "pictureUrl", "firstName", "lastName")))
                .verifyComplete();

        verify(userGrpcClient).createExternalUserAsync(request);
    }
}