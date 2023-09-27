package com.gw.user.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {
    private final DatabaseConfig databaseConfig;

    public MongoConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    protected String getDatabaseName() {
        return databaseConfig.database();
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        var url = String.format("%s://%s:%s@%s/%s?authsource=admin",
                databaseConfig.scheme(),
                databaseConfig.username(),
                databaseConfig.password(),
                databaseConfig.hostname(),
                databaseConfig.database());

        builder.applyConnectionString(new ConnectionString(url));
    }
}