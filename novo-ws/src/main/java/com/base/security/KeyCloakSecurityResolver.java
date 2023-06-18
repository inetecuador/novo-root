package com.base.security;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.apache.commons.lang3.StringUtils;
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
        System.out.println("getRealm: "+keycloakDeployment.getRealm());
        System.out.println("getTokenUrl: "+keycloakDeployment.getTokenUrl());

        try {
            String token = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(token)) {
                System.out.println("TOKEN: "+token);
                token = token.substring("Bearer".length() + 1);
                JWT jwt = JWTParser.parse(token);
                String issuer= jwt.getJWTClaimsSet().getIssuer();
                System.out.println("issuer: "+issuer);
            }
        } catch (Exception ex) {
        }


        return keycloakDeployment;
    }

    void setAdapterConfig(AdapterConfig adapterConfig) {
        this.adapterConfig = adapterConfig;
    }
}
