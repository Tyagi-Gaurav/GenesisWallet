package com.gw.user.config;

import com.gw.vault.config.VaultInitializer;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;
import org.springframework.vault.support.VaultToken;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Configuration
public class DatabaseBeanFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseBeanFactory.class);
    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String VAULT_DB_SERVICE_NAME = "postgres/service_1";

    @Bean
    public VaultKeyValueOperations dbCredentials(VaultInitializer vaultInitializer) {
        VaultToken login = vaultInitializer.clientAuthentication().login();
        var vaultTemplate = new VaultTemplate(vaultInitializer.vaultEndpoint(),
                new TokenAuthentication(login.getToken()));
        return vaultTemplate.opsForKeyValue("database",
                VaultKeyValueOperationsSupport.KeyValueBackend.versioned());
    }

    @Bean
    public DataSource dataSource(DatabaseConfig databaseConfig,
                                 VaultKeyValueOperations operations) {
        var dbCredentials = Optional.ofNullable(operations.get(VAULT_DB_SERVICE_NAME))
                .map(VaultResponseSupport::getData).orElseGet(Map::of);

        var cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(databaseConfig.driver());

            var userName = dbCredentials.get(USER_NAME).toString();
            var password = dbCredentials.get(PASSWORD).toString();
            var host = dbCredentials.get(HOST).toString();
            var port = Integer.parseInt(dbCredentials.get(PORT).toString());
            var jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, databaseConfig.name());

            cpds.setJdbcUrl(jdbcUrl);
            cpds.setUser(userName);
            cpds.setPassword(password);
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Error while initializing database: {}", e.getMessage());
            }
            throw new IllegalArgumentException(e);
        }

        return cpds;
    }

    @Bean
    public PostgresqlConnectionConfiguration configuration(DatabaseConfig databaseConfig,
                                                           VaultKeyValueOperations operations) {
        var dbCredentials = Optional.ofNullable(operations.get(VAULT_DB_SERVICE_NAME))
                .map(VaultResponseSupport::getData).orElseGet(Map::of);

        return PostgresqlConnectionConfiguration.builder()
                .database(databaseConfig.name())
                .password(dbCredentials.get(PASSWORD).toString())
                .username(dbCredentials.get(USER_NAME).toString())
                .port(Integer.parseInt(dbCredentials.get(PORT).toString()))
                .host(dbCredentials.get(HOST).toString())
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
