package com.gw.user.repo;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.PostgreSQLContainer.POSTGRESQL_PORT;


public class DatabaseInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static PostgreSQLContainer postgres;
    private static final String USER_SCHEMA = "USER_SCHEMA";

    static {
        postgres = new PostgreSQLContainer(DockerImageName.parse("postgres"))
                .withDatabaseName(USER_SCHEMA);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        postgres.start();

        // Override MySql configuration
        String databaseHost = "db.host=" + postgres.getHost();
        String databasePort = "db.port=" + postgres.getMappedPort(POSTGRESQL_PORT);
        String databaseUser = "db.user=" + postgres.getUsername();
        String databasePassword = "db.password=" + postgres.getPassword();
        String databaseName = "db.name=" + postgres.getDatabaseName();
        String databaseSchema = "db.schema=" + USER_SCHEMA;

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                databaseHost,
                databasePort,
                databaseName,
                databaseSchema,
                databaseUser,
                databasePassword);
    }
}