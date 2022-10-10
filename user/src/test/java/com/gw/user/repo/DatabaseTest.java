package com.gw.user.repo;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.io.InputStream;

@Import(DatabaseTest.TestDatabaseContextConfiguration.class)
public abstract class DatabaseTest {

    @TestConfiguration
    @EnableR2dbcRepositories
    static class TestDatabaseContextConfiguration extends AbstractR2dbcConfiguration {

        @Value("${db.port}")
        String port;

        @Value("${db.name}")
        String databaseName;

        @Value("${db.schema}")
        String databaseSchema;

        @Bean
        public ConnectionFactoryInitializer inMemoryR2DbcDatabase(ConnectionFactory connectionFactory) {
            var initializer = new ConnectionFactoryInitializer();
            initializer.setConnectionFactory(connectionFactory);
            final InputStream resourceAsStream = TestDatabaseContextConfiguration.class.getClassLoader()
                    .getResourceAsStream("db.changelog/userchangelog.sql");

            initializer.setDatabasePopulator(new ResourceDatabasePopulator(new InputStreamResource(resourceAsStream)));

            return initializer;
        }

        @Override
        @Bean
        public ConnectionFactory connectionFactory() {
            return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                    .database(databaseName)
                    .schema(databaseSchema)
                    .host("localhost")
                    .port(Integer.parseInt(port))
                    .username("test")
                    .password("test")
                    .build());
        }
    }
}
