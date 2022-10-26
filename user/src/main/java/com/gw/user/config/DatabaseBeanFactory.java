package com.gw.user.config;

import com.gw.vault.config.VaultInitializer;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultToken;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class DatabaseBeanFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseBeanFactory.class);
    private static final String URL = "url";
    private static final String USER_NAME = "userName";
    private static final String PASSWORD = "password";
    private static final String HOST = "host";
    private static final String PORT = "port";

    @Bean
    @Qualifier("DbCredentials")
    public Map<String, Object> dbCredentials(VaultInitializer vaultInitializer) {
        VaultToken login = vaultInitializer.clientAuthentication().login();
        var vaultTemplate = new VaultTemplate(vaultInitializer.vaultEndpoint(),
                new TokenAuthentication(login.getToken()));
        VaultKeyValueOperations operations = vaultTemplate.opsForKeyValue("database",
                VaultKeyValueOperationsSupport.KeyValueBackend.versioned());
        return operations.get("postgres/service_1").getData();
    }

    @Bean
    public DataSource dataSource(DatabaseConfig databaseConfig,
                                 @Qualifier("DbCredentials") Map<String, Object> dbCredentials) {
        var cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(databaseConfig.driver());


            var jdbcUrl = dbCredentials.get(URL).toString();
            var userName = dbCredentials.get(USER_NAME).toString();
            var password = dbCredentials.get(PASSWORD).toString();
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
                                                           @Qualifier("DbCredentials") Map<String, Object> dbCredentials) {
        return PostgresqlConnectionConfiguration.builder()
                .database(databaseConfig.name())
                .host(dbCredentials.get(HOST).toString())
                .port(Integer.parseInt(dbCredentials.get(PORT).toString()))
                .password(dbCredentials.get(PASSWORD).toString())
                .username(dbCredentials.get(USER_NAME).toString())
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
