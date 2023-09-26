package com.gw.user.testutils;

import com.gw.user.config.DatabaseConfig;
import com.gw.user.config.MongoConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({DatabaseTest.TestDatabaseContextConfiguration.class,
        MongoConfig.class})
public abstract class DatabaseTest {

    @TestConfiguration
    static class TestDatabaseContextConfiguration {

        @Bean
        public DatabaseConfig databaseConfig(@Value("${database.username}") String userName,
                                             @Value("${database.password}") String newPassword,
                                             @Value("${database.database}") String newDBName,
                                             @Value("${database.hostname}") String newHostName,
                                             @Value("${database.scheme}") String newScheme) {
            return new DatabaseConfig(userName, newPassword, newDBName, newHostName, newScheme);
        }
    }
}
