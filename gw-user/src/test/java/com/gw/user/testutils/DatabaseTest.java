package com.gw.user.testutils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.InputStreamResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;

import javax.sql.DataSource;
import java.io.InputStream;

@Import(DatabaseTest.TestDatabaseContextConfiguration.class)
public abstract class DatabaseTest {

    @TestConfiguration
    static class TestDatabaseContextConfiguration {

        @Bean
        public ConnectionFactoryInitializer inMemoryR2DbcDatabase(ConnectionFactory connectionFactory) {
            var initializer = new ConnectionFactoryInitializer();
            initializer.setConnectionFactory(connectionFactory);
            final InputStream resourceAsStream = TestDatabaseContextConfiguration.class.getClassLoader()
                    .getResourceAsStream("db.changelog/userchangelog.sql");

            initializer.setDatabasePopulator(new ResourceDatabasePopulator(new InputStreamResource(resourceAsStream)));

            return initializer;
        }

        @Bean
        public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
            return DatabaseClient.create(connectionFactory);
        }

        @Bean
        public ConnectionFactory connectionFactory(@Value("${database.host}") String host,
                                                   @Value("${database.port}") String port,
                                                   @Value("${database.user}") String user,
                                                   @Value("${database.password}") String password,
                                                   @Value("${database.name}") String databaseName,
                                                   @Value("${database.schema}") String databaseSchema
                                                   ) {
            return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                    .host(host)
                    .port(Integer.parseInt(port))
                    .username(user)
                    .password(password)
                    .database(databaseName)
                    .schema(databaseSchema)
                    .build());
        }

        @Bean
        public DataSource dataSource(@Value("${database.host}") String host,
                                     @Value("${database.port}") String port,
                                     @Value("${database.user}") String user,
                                     @Value("${database.password}") String password,
                                     @Value("${database.name}") String databaseName,
                                     @Value("${database.driver}") String databaseDriver) {
            var cpds = new ComboPooledDataSource();
            try {
                cpds.setDriverClass(databaseDriver);

                var jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, databaseName);

                cpds.setJdbcUrl(jdbcUrl);
                cpds.setUser(user);
                cpds.setPassword(password);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }

            return cpds;
        }
    }
}
