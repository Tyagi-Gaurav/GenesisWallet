package com.gw.user.repo;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Order(0)
public class TestContainerDatabaseInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static GenericContainer<?> mongoDBContainer;
    private static final String MONGO_USER = "root";
    private static final String MONGO_DB_PASSWORD = "example";
    private static final String MONGO_DB_NAME = "testDB";

    static {
        mongoDBContainer = new GenericContainer(DockerImageName.parse("mongo:6.0"))
                .withEnv("MONGO_INITDB_ROOT_USERNAME", MONGO_USER)
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", MONGO_DB_PASSWORD)
                .withEnv("MONGO_INITDB_DATABASE", MONGO_DB_NAME)
                .withExposedPorts(27017);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        mongoDBContainer.start();

        // Override MySql configuration
        String newDatabaseUserName = "database.username=" + MONGO_USER;
        String newDatabasePassword = "database.password=" + MONGO_DB_PASSWORD;
        String newDatabaseName = "database.database=" + MONGO_DB_NAME;
        String newDatabaseHost = "database.hostname=" + mongoDBContainer.getHost() + ":" + mongoDBContainer.getMappedPort(27017);
        String newDatabaseScheme = "database.scheme=mongodb";

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                newDatabaseUserName,
                newDatabasePassword,
                newDatabaseName,
                newDatabaseHost,
                newDatabaseScheme,
                "spring.data.mongodb.port=" + mongoDBContainer.getMappedPort(27017));
    }
}