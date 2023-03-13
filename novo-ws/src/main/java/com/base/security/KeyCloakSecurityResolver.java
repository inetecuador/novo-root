package com.base.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.OIDCHttpFacade;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * KeyCloakSecurityResolver.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 * @since 1.0.0
 */
@Configuration
public class KeyCloakSecurityResolver implements KeycloakConfigResolver {

    private KeycloakDeployment keycloakDeployment;

    @Autowired(required = false)
    private AdapterConfig adapterConfig;

    /**
     * {@inheritDoc}
     */
    @Override
    public KeycloakDeployment resolve(OIDCHttpFacade.Request request) {
        if (keycloakDeployment != null) {
            return keycloakDeployment;
        }
        keycloakDeployment = KeycloakDeploymentBuilder.build(adapterConfig);
        return keycloakDeployment;
    }

    void setAdapterConfig(AdapterConfig adapterConfig) {
        this.adapterConfig = adapterConfig;
    }
}
