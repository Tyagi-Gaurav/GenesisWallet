package com.gw.user.e2e.cache;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import redis.embedded.RedisServer;

import static com.gw.test.common.grpc.PortLookup.getRandomPort;
import static com.gw.test.common.grpc.PortLookup.isAvailable;

public class TestContainerCacheInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>  {
    private static final int DEFAULT_REDIS_PORT = 6379;

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {

        int randomPort = isAvailable(DEFAULT_REDIS_PORT) ? 6379 : getRandomPort();

        String cacheHost = "cache.host=localhost";
        String cachePort = "cache.port=" + randomPort;

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                cacheHost,
                cachePort);

        RedisServer redisServer = new RedisServer(randomPort);
        redisServer.start();

        applicationContext.registerShutdownHook();
    }
}
