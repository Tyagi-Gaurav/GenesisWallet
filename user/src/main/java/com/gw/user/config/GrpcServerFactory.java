package com.gw.user.config;

import com.gw.grpc.common.CorrelationIdInterceptor;
import com.gw.grpc.common.MetricsInterceptor;
import com.gw.user.grpc.UserServiceGrpcImpl;
import com.gw.user.service.UserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcServerFactory {
    private static final Logger LOG = LogManager.getLogger("APP");

    @Bean(destroyMethod = "shutdown")
    public Server grpcServer(UserServiceGrpcImpl userServiceGrpcImpl,
                             GrpcConfig grpcConfig,
                             MetricsInterceptor metricsInterceptor,
                             CorrelationIdInterceptor correlationIdInterceptor) throws IOException {
        LOG.info("Starting GRPC server on port {}", grpcConfig.port());
        var grpcServer = ServerBuilder.forPort(grpcConfig.port())
                .addService(userServiceGrpcImpl)
                .intercept(correlationIdInterceptor)
                .intercept(metricsInterceptor)
                .build();

        return grpcServer.start();
    }

    @Bean
    public UserServiceGrpcImpl userGrpcService(UserService userService) {
        return new UserServiceGrpcImpl(userService);
    }
}
