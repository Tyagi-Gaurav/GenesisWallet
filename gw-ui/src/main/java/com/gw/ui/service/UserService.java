package com.gw.ui.service;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.gw.ui.service.domain.ExternalUserConnectRequest;
import com.gw.user.client.UserGrpcClient;
import com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO;
import com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.Executors;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserGrpcClient userGrpcClient;

    public UserService(UserGrpcClient userGrpcClient) {
        this.userGrpcClient = userGrpcClient;
    }

    public Mono<Void> connectUser(ExternalUserConnectRequest externalUserConnectRequest) {
        ListenableFuture<ExternalUserCreateGrpcResponseDTO> userAsyncFuture =
                userGrpcClient.createExternalUserAsync(ExternalUserCreateGrpcRequestDTO.newBuilder()
                        .setEmail(externalUserConnectRequest.userName())
                        .setFirstName(externalUserConnectRequest.firstName())
                        .setLastName(externalUserConnectRequest.lastName())
                        .setPictureUrl(externalUserConnectRequest.pictureUrl())
                        .setTokenExpiryTime(externalUserConnectRequest.expiryTime().toEpochMilli())
                        .build());
        return Mono.create(voidMonoSink -> Futures.addCallback(userAsyncFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(ExternalUserCreateGrpcResponseDTO result) {
                LOG.info("Successfully connected for User.");
                voidMonoSink.success();
            }

            @Override
            public void onFailure(Throwable t) {
                LOG.error("Error occurred while creating User: {}", t.getMessage(), t);
                voidMonoSink.error(new RuntimeException());
            }
        }, Executors.newSingleThreadExecutor()));
    }
}
