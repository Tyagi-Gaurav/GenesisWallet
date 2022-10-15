package com.gw.user.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import javax.sql.DataSource;

@Configuration
public class DatabaseBeanFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseBeanFactory.class);

    @Bean
    public DataSource dataSource(DatabaseConfig databaseConfig) {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(databaseConfig.driver());
            var jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", databaseConfig.host(),
                    databaseConfig.port(), databaseConfig.name());
            LOG.info("Connecting to database {}", jdbcUrl);
            cpds.setJdbcUrl(jdbcUrl);

            cpds.setUser(databaseConfig.user());
            cpds.setPassword(databaseConfig.password());
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Error while initializing database: {}", e.getMessage());
            }
            throw new IllegalArgumentException(e);
        }

        return cpds;
    }

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
