package com.gw.vault.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.vault.authentication.AppRoleAuthentication;
import org.springframework.vault.authentication.AppRoleAuthenticationOptions;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import org.springframework.vault.support.VaultToken;

@Component
public class VaultInitializer extends AbstractVaultConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(VaultInitializer.class);

    @Autowired
    private VaultProperties vaultProperties;

    @Override
    public VaultEndpoint vaultEndpoint() {
        final VaultEndpoint vaultEndpoint = VaultEndpoint.create(vaultProperties.host(), vaultProperties.port());
        vaultEndpoint.setScheme(vaultProperties.httpScheme());
        return vaultEndpoint;
    }

    @Override
    public ClientAuthentication clientAuthentication() {
        LOG.info("Using token: " + vaultProperties.token());
        final VaultToken initialToken = VaultToken.of(vaultProperties.token());
        final AppRoleAuthenticationOptions options = AppRoleAuthenticationOptions
                .builder()
                .appRole(vaultProperties.appRole())
                .roleId(AppRoleAuthenticationOptions.RoleId.pull(initialToken))
                .secretId(AppRoleAuthenticationOptions.SecretId.pull(initialToken))
                .build();

        return new AppRoleAuthentication(options, this.restOperations());
    }
}
