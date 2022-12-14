package com.gw.user.config;

import com.gw.grpc.common.CorrelationIdInterceptor;
import com.gw.user.grpc.UserServiceGrpcImpl;
import com.gw.user.service.UserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcServerFactory {
    private static final Logger LOG = LoggerFactory.getLogger(GrpcServerFactory.class);

    @Bean(destroyMethod = "shutdown")
    public Server grpcServer(UserServiceGrpcImpl userServiceGrpcImpl,
                             GrpcConfig grpcConfig) throws IOException {
        LOG.info("Starting GRPC server on port {}", grpcConfig.port());
        var grpcServer = ServerBuilder.forPort(grpcConfig.port())
                .addService(userServiceGrpcImpl)
                .intercept(new CorrelationIdInterceptor())
                .build();

        return grpcServer.start();
    }

    @Bean
    public UserServiceGrpcImpl userGrpcService(UserService userService) {
        return new UserServiceGrpcImpl(userService);
    }
}
