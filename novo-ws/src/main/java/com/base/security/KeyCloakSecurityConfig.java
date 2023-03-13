package com.base.security;

import com.base.security.audit.IKeycloakUserInfo;
import com.base.security.audit.KeycloakUserInfo;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * KeyCloakSecurityConfig.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class KeyCloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests().antMatchers("/swagger-ui/**", "/openapi/**").permitAll()
            .anyRequest().authenticated();
        http.csrf().disable();
        http.cors();
    }

    /**
     * Configuracion global de KeycloakAuthenticationProvider.
     *
     * @param auth AuthenticationManagerBuilder
     * @throws Exception error
     * @author vsangucho on 10/03/2023
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * KeycloakConfigResolver.
     *
     * @return KeycloakConfigResolver
     * @author vsangucho on 10/03/2023
     */
    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    /**
     * Keycloak user info.
     *
     * @return IKeycloakUserInfo
     */
    @Bean
    public IKeycloakUserInfo keycloakUserInfo() {
        return new KeycloakUserInfo();
    }
}
