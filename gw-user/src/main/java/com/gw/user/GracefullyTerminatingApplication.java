package com.gw.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class GracefullyTerminatingApplication implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger("APP");

    private final String[] args;
    private final Class<?> bootstrapClass;

    public GracefullyTerminatingApplication(Class<?> bootstrapClass, String... args) {
        this.args = args;
        this.bootstrapClass = bootstrapClass;
    }

    @Override
    public void run() {
        var app = new SpringApplication(bootstrapClass);
        app.setRegisterShutdownHook(false);
        LOGGER.info("Application has removed default shutdown hook and will now initialise spring config...");
        ConfigurableApplicationContext applicationContext = app.run(args);
        LOGGER.info("Application has completed spring initialisation");
        Runtime.getRuntime().addShutdownHook(new Thread(new GracefulShutdownHook(applicationContext)));
        LOGGER.info("Application has added GracefulShutdownHook");
    }
}
