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

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

/**
 * Supports the entire OAuth lifecycle including OAuth2Authentication and generating Authorization http header.
 *
 * @author Nasir Ahmed
 *
 */
@Component
public class OAuthHelper {

    private final ClientDetailsService clientDetailsService;

    private final UserDetailsService userDetailsService;

    private final DefaultTokenServices tokenService;

    /**
     * @param clientDetailsService provides the details about an OAuth2 client
     * @param userDetailsService loads user-specific data
     * @param tokenService base implementation for token services
     */
    @Autowired
    public OAuthHelper(final ClientDetailsService clientDetailsService,
            final UserDetailsService userDetailsService,
            final DefaultTokenServices tokenService) {
        this.clientDetailsService = clientDetailsService;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    /**
     * Issues valid access token to be use with MockMvc
     *
     * @param clientid clientId for the token
     * @param username user name for the token
     *
     * @return mockRequest appended with valid access token
     */
    public RequestPostProcessor bearerToken(final String clientid,
            final String username) {
        return mockRequest -> {
            final OAuth2AccessToken token = tokenService.createAccessToken(
                    oAuth2Authentication(clientid, username));
            mockRequest.addHeader("Authorization", "Bearer " + token.getValue());
            return mockRequest;
        };
    }

    /**
     * Issues invalid access token to be use with MockMvc
     *
     * @param clientid clientId for the token
     * @param username user name for the token
     *
     * @return mockRequest appended with invalid access token
     */
    public RequestPostProcessor revokedToken(final String clientid,
            final String username) {
        return mockRequest -> {
            final OAuth2AccessToken token = tokenService.createAccessToken(
                    oAuth2Authentication(clientid, username));
            mockRequest.addHeader("Authorization", "Bearer " + token.getValue());
            tokenService.revokeToken(token.getValue());
            return mockRequest;
        };
    }

    /**
     * For use with @WithOAuth2Authentication
     *
     * @param clientid clientId for the token
     * @param username user name for the token
     *
     * @return mocked user authentication for unit tests
     */
    public OAuth2Authentication oAuth2Authentication(final String clientId,
            final String username) {
        // Look up authorities, resourceIds and scopes based on clientId
        final ClientDetails client = clientDetailsService.loadClientByClientId(clientId);
        final Collection<GrantedAuthority> authorities = client.getAuthorities();

        // Create request
        final OAuth2Request oAuth2Request = new OAuth2Request(Collections.emptyMap(),
                clientId, authorities, true, client.getScope(), client.getResourceIds(),
                null, Collections.emptySet(), Collections.emptyMap());

        // Create OAuth2AccessToken
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetailsService.loadUserByUsername(username), null, authorities);
        return new OAuth2Authentication(oAuth2Request, authenticationToken);
    }
}
