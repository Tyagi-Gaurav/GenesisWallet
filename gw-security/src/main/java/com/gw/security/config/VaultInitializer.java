package com.gw.security.config;

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

    @Autowired
    private VaultProperties vaultProperties;

    @Override
    public VaultEndpoint vaultEndpoint() {
        System.out.println("vaultEndpoint Properties: " + vaultProperties);
        var vaultEndpoint = VaultEndpoint.create(vaultProperties.host(), vaultProperties.port());
        vaultEndpoint.setScheme(vaultProperties.httpScheme());
        return vaultEndpoint;
    }

    @Override
    public ClientAuthentication clientAuthentication() {
        System.out.println("ClientAuthentication Properties: " + vaultProperties);
        var initialToken = VaultToken.of(vaultProperties.token());
        AppRoleAuthenticationOptions options = AppRoleAuthenticationOptions
                .builder()
                .appRole(vaultProperties.appRole())
                .roleId(AppRoleAuthenticationOptions.RoleId.pull(initialToken))
                .secretId(AppRoleAuthenticationOptions.SecretId.pull(initialToken))
                .build();

        return new AppRoleAuthentication(options, this.restOperations());
    }

//    @Bean
//    public VaultTemplate vaultTemplate(VaultInitializer vaultInitializer) {
//        VaultToken login = vaultInitializer.clientAuthentication().login();
//        return new VaultTemplate(vaultInitializer.vaultEndpoint(),
//                new TokenAuthentication(login.getToken()));
//    }
}
