package com.gw.user.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
public class DatabaseBeanFactory {

    @Bean
    public PostgresqlConnectionConfiguration configuration(DatabaseConfig databaseConfig) {
        return PostgresqlConnectionConfiguration.builder()
                .database(databaseConfig.name())
                .host(databaseConfig.host())
                .port(databaseConfig.port())
                .password(databaseConfig.password())
                .username(databaseConfig.user())
                .schema(databaseConfig.schema())
                .build();
    }

    @Bean
    public ConnectionFactory connectionFactory(PostgresqlConnectionConfiguration postgresqlConnectionConfiguration) {
        return new PostgresqlConnectionFactory(postgresqlConnectionConfiguration);
    }

    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }
}
