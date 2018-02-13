/*
 * #region
 * unit-service
 * %%
 * Copyright (C) 2017 - 2018 Etilize
 * %%
 * NOTICE: All information contained herein is, and remains the property of ETILIZE.
 * The intellectual and technical concepts contained herein are proprietary to
 * ETILIZE and may be covered by U.S. and Foreign Patents, patents in process, and
 * are protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from ETILIZE. Access to the source code contained herein
 * is hereby forbidden to anyone except current ETILIZE employees, managers or
 * contractors who have executed Confidentiality and Non-disclosure agreements
 * explicitly covering such access.
 *
 * The copyright notice above does not evidence any actual or intended publication
 * or disclosure of this source code, which includes information that is confidential
 * and/or proprietary, and is a trade secret, of ETILIZE. ANY REPRODUCTION, MODIFICATION,
 * DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS
 * SOURCE CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF ETILIZE IS STRICTLY PROHIBITED,
 * AND IN VIOLATION OF APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT
 * OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR
 * IMPLY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO
 * MANUFACTURE, USE, OR SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 * #endregion
 */

package com.etilize.burraq.unit.test.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.etilize.burraq.unit.config.ResourceProperties;

/**
 * Configures the authorization server for testing.
 *
 * @author Nasir Ahmed
 */
@Configuration
@ConfigurationProperties
@EnableAuthorizationServer
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final ResourceProperties resourceProperties;

    /**
     * Constructor used to initialize AuthorizationServerConfig
     *
     * @param authenticationManager authenticationManager
     * @param resourceProperties resourceProperties
     */
    @Autowired
    public AuthorizationServerConfig(final AuthenticationManager authenticationManager,
            final ResourceProperties resourceProperties) {
        this.authenticationManager = authenticationManager;
        this.resourceProperties = resourceProperties;
    }

    /**
     * acts as a {@link TokenEnhancer} when tokens are granted.
     *
     * @return jwt json web token
     * @throws Exception
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws Exception {
        final JwtAccessTokenConverter jwt = new JwtAccessTokenConverter();
        jwt.setSigningKey(resourceProperties.getJwt().getPrivateKeyValue());
        jwt.setVerifierKey(resourceProperties.getJwt().getKeyValue());
        jwt.afterPropertiesSet();
        return jwt;
    }

    /**
     * token store for services that stores tokens in memory.
     *
     * @return inMemoryTokenStore stores tokens in memory
     */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    /**
     * implementation of token services for the access token
     *
     * @return DefaultTokenServices base implementation for token services
     */
    @Bean
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    /**
     * Setting up the endpointsconfigurer authentication manager. The
     * AuthorizationServerEndpointsConfigurer defines the authorization and token
     * endpoints and the token services.
     *
     * @param endpoints configure the properties of the Authorization Server endpoints
     * @throws Exception
     */
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.accessTokenConverter(accessTokenConverter());
    }

    /**
     * Setting up the clients with a clientId, a scope, the grant types and the
     * authorities.
     *
     * @param clients configure the properties of the clients.
     * @throws Exception
     */
    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        final InMemoryClientDetailsServiceBuilder inMemoryClientDetailsServiceBuilder = clients.inMemory();
        // creates a new client "burraq"
        inMemoryClientDetailsServiceBuilder.withClient("burraq") //
                .authorizedGrantTypes("password") //
                .resourceIds("burraq") //
                .scopes("unit.create", "unit.update", "unit.delete", "group.create",
                        "group.update", "group.delete");

        // creates a new client "unauthorized"
        inMemoryClientDetailsServiceBuilder.withClient("unauthorized") //
                .authorizedGrantTypes("password") //
                .authorities("myauthorities") //
                .resourceIds("myresource") //
                .scopes(UUID.randomUUID().toString());
    }
}
