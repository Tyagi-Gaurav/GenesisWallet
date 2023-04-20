package com.gw.user.e2e.cache;

import com.gw.test.common.grpc.PortLookup;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import redis.embedded.RedisServer;

public class TestContainerCacheInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>  {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        int randomPort = PortLookup.getRandomPort();

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
