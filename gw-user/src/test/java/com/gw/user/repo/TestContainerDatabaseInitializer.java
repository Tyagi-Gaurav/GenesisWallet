package com.gw.user.repo;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.PostgreSQLContainer.POSTGRESQL_PORT;

@Order(0)
public class TestContainerDatabaseInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static PostgreSQLContainer postgres;
    static GenericContainer<?> mongoDBContainer;
    private static final String USER_SCHEMA = "USER_SCHEMA";
    private static final String MONGO_USER = "root";
    private static final String MONGO_DB_PASSWORD = "example";
    private static final String MONGO_DB_NAME = "testDB";

    static {
        postgres = new PostgreSQLContainer(DockerImageName.parse("postgres"))
                .withDatabaseName(USER_SCHEMA);

        mongoDBContainer = new GenericContainer(DockerImageName.parse("mongo:6.0"))
                .withEnv("MONGO_INITDB_ROOT_USERNAME", MONGO_USER)
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", MONGO_DB_PASSWORD)
                .withEnv("MONGO_INITDB_DATABASE", MONGO_DB_NAME)
                .withExposedPorts(27017);
        ;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        postgres.start();
        mongoDBContainer.start();

        // Override MySql configuration
        String databaseHost = "database.host=" + postgres.getHost();
        String databasePort = "database.port=" + postgres.getMappedPort(POSTGRESQL_PORT);
        String databaseUser = "database.user=" + postgres.getUsername();
        String databasePassword = "database.password=" + postgres.getPassword();
        String databaseName = "database.name=" + postgres.getDatabaseName();
        String databaseSchema = "database.schema=" + USER_SCHEMA;

        String newDatabaseUserName = "database.newDB.username=" + MONGO_USER;
        String newDatabasePassword = "database.newDB.password=" + MONGO_DB_PASSWORD;
        String newDatabaseName = "database.newDB.database=" + MONGO_DB_NAME;
        String newDatabaseHost = "database.newDB.hostname=" + mongoDBContainer.getHost() + ":" + mongoDBContainer.getMappedPort(27017);
        String newDatabaseScheme = "database.newDB.scheme=mongodb";

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                databaseHost,
                databasePort,
                databaseName,
                databaseSchema,
                databaseUser,
                databasePassword,
                newDatabaseUserName,
                newDatabasePassword,
                newDatabaseName,
                newDatabaseHost,
                newDatabaseScheme,
                "spring.data.mongodb.port=" + mongoDBContainer.getMappedPort(27017));
        //For mongo, we could just use the spring data configuration instead of using custom database config.
    }
}