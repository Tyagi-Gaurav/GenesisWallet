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
        return databaseConfig.name();
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        var url = String.format("%s://%s:%s@%s/%s?authsource=admin",
                databaseConfig.newDb().scheme(),
                databaseConfig.newDb().username(),
                databaseConfig.newDb().password(),
                databaseConfig.newDb().hostname(),
                databaseConfig.newDb().database());

        builder.applyConnectionString(new ConnectionString(url)); //TODO Rename this
    }
}