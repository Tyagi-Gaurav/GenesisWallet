package com.gw.user.e2e.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Container;
import org.testcontainers.vault.VaultContainer;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Some handy commands to debug vault.
 * <p>
 * vaultContainer.execInContainer("vault", "read", "auth/approle/role/user_service");
 * vaultContainer.execInContainer("vault", "token", "capabilities", "database/data/postgres/user_service");
 * vaultContainer.execInContainer("vault", "token", "capabilities", login.getToken(), "database/postgres/user_service");
 * vaultContainer.execInContainer("vault", "secrets", "list");
 * vaultContainer.execInContainer("vault", "kv", "list", "-output-policy", "database/data/postgres/user_service");
 * vaultContainer.execInContainer("vault", "token", "lookup", login.getToken());
 * vaultContainer.execInContainer("vault", "policy", "read", "database");
 */
@Order(1)
public class TestContainerVaultInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOG = LogManager.getLogger("APP");
    private static final String VAULT_TOKEN = "root";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        String databaseHost = applicationContext.getEnvironment().getProperty("database.host");
        String databasePort = applicationContext.getEnvironment().getProperty("database.port");
        String databaseUser = applicationContext.getEnvironment().getProperty("database.user");
        String databasePassword = applicationContext.getEnvironment().getProperty("database.password");
        var vaultContainer = new VaultContainer<>("vault:1.12.2");

        try {
            vaultContainer.withVaultToken(VAULT_TOKEN)
                    .withEnv("VAULT_DEV_LISTEN_ADDRESS", "0.0.0.0:8200")
                    .withInitCommand(
                            "secrets enable -path=database kv-v2")
                    .withClasspathResourceMapping("database-policy.hcl", "/opt/database-policy.hcl", BindMode.READ_WRITE)
                    .withStartupAttempts(1)
                    .start();

            var execResult = vaultContainer.execInContainer("vault", "auth", "enable", "approle");
            checkExitCode(execResult);
            execResult = vaultContainer.execInContainer("vault", "policy", "write", "database", "/opt/database-policy.hcl");
            checkExitCode(execResult);
            execResult = vaultContainer
                    .execInContainer("vault", "kv", "put", "database/postgres/user_service",
                            "username=" + databaseUser,
                            "password=" + databasePassword,
                            "port=" + databasePort,
                            "host=" + databaseHost);
            checkExitCode(execResult);
            execResult = vaultContainer.execInContainer("vault", "write", "auth/approle/role/user_service", "policies=database", "token_ttl=5m", "token_max_ttl=10m");
            checkExitCode(execResult);

            LOG.debug("Vault First Mapped Port: {}", vaultContainer.getFirstMappedPort());
            String httpHostAddress = vaultContainer.getHttpHostAddress();
            LOG.debug("Vault Http address: {}", httpHostAddress);
            Integer vaultPort = vaultContainer.getExposedPorts()
                    .stream()
                    .filter(port -> httpHostAddress.contains(String.valueOf(port)))
                    .findFirst()
                    .orElseGet(vaultContainer::getFirstMappedPort);
            LOG.debug("Vault Port: {}", vaultPort);

            // Override MySql configuration
            String vaultHost = "vault.host=" + vaultContainer.getHost();
            String vaultToken = "vault.token=" + VAULT_TOKEN;
            String vaultPortProperty = "vault.port=" + vaultPort;

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    vaultHost,
                    vaultToken,
                    vaultPortProperty);
            vaultContainer.followOutput(outputFrame ->
                    LOG.debug("Vault Container {}", outputFrame.getUtf8String()));
        } catch (Exception e) {
            LOG.error("Error occurred while starting vault container: {}", e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    private void checkExitCode(Container.ExecResult exitCode) {
        if (exitCode.getExitCode() != 0) {
            LOG.error("Non-zero exit code: {}", exitCode.getExitCode());
            LOG.error("Error output: {}", exitCode.getStderr());
            LOG.error("Normal output: {}", exitCode.getStdout());
            fail();
        }
    }
}