package com.gw.test.common.grpc;

import io.grpc.BindableService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerInterceptor;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GrpcExtension implements AfterEachCallback {
    private static final Logger LOG = LoggerFactory.getLogger(GrpcExtension.class);
    private static final int TERMINATION_TIMEOUT_MS = 100;

    private final List<CleanupTarget> cleanupTargets = new ArrayList<>();

    @Override
    public void afterEach(ExtensionContext context) {
        cleanupTargets.forEach(ct -> {
            try {
                ct.shutdown();
                ct.awaitTermination(TERMINATION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                LOG.error("Error occurred while terminating grpc server/channel", e);
            }
        });
        cleanupTargets.clear();
    }

    public ServiceDetails createGrpcServerFor(BindableService bindableService,
                                              ServerInterceptor correlationIdInterceptor) throws IOException {
        var serverName = InProcessServerBuilder.generateName();
        Server server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .intercept(correlationIdInterceptor)
                .addService(bindableService)
                .build().start();

        ManagedChannel channel = InProcessChannelBuilder.forName(serverName)
                .directExecutor().build();

        cleanupTargets.add(new ManagedChannelTarget(channel));
        cleanupTargets.add(new ManagedServerTarget(server));
        return new ServiceDetails(serverName, server.getPort(), channel);
    }

    interface CleanupTarget {
        void shutdown();

        boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;
    }

    public static class ManagedChannelTarget implements CleanupTarget {
        private final ManagedChannel managedChannel;

        public ManagedChannelTarget(ManagedChannel managedChannel) {
            this.managedChannel = managedChannel;
        }

        @Override
        public void shutdown() {
            managedChannel.shutdown();
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return managedChannel.awaitTermination(timeout, unit);
        }
    }

    public static class ManagedServerTarget implements CleanupTarget {
        private final Server server;

        public ManagedServerTarget(Server server) {
            this.server = server;
        }

        @Override
        public void shutdown() {
            server.shutdown();
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return server.awaitTermination(timeout, unit);
        }
    }

    public record ServiceDetails(String serverName, int port, ManagedChannel managedChannel) {
    }
}
