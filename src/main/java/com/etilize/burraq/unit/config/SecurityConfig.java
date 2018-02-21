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

package com.etilize.burraq.unit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Configure Security Configuration for ResourceServer
 *
 * @author Nasir Ahmed
 *
 */
@Configuration
public class SecurityConfig extends ResourceServerConfigurerAdapter {

    private ResourceProperties resourceProperties;

    /**
     * Constructor used to initialize SecurityConfig
     *
     * @param resourceProperties resourceProperties
     */
    @Autowired
    public SecurityConfig(final ResourceProperties resourceProperties) {
        this.resourceProperties = resourceProperties;
    }

    /**
     * Use this to configure the access rules for secure resources.
     *
     * @param http current http filter configuration
     * @throws Exception
     */
    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.cors();
        http.authorizeRequests() //
                .anyRequest() //
                .authenticated();
    }

    /**
     * Add resource-server specific properties (like a resource id).
     *
     * @param resources configurer for the resource server
     * @throws Exception
     */
    @Override
    public void configure(final ResourceServerSecurityConfigurer resources)
            throws Exception {
        // resourceId should be a valid "aud" for jwt
        // https://tools.ietf.org/html/rfc7519#section-4.1.3
        resources.resourceId(resourceProperties.getResourceId());
    }
}
