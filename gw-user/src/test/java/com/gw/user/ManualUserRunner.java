package com.gw.user;

import org.springframework.boot.SpringApplication;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class ManualUserRunner {
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
    public static void main(String[] args) {
        var app = new SpringApplication(Application.class);
        app.run();
    }
}
