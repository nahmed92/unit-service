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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * SecurityContextFactory that will create a new SecurityContext
 *
 * @author Nasir Ahmed
 *
 */
public class WithOAuth2AuthenticationSecurityContextFactory
        implements WithSecurityContextFactory<WithOAuth2Authentication> {

    private final OAuthHelper oAuthHelper;

    /**
     * @param oAuthHelper oAuth lifecycle
     */
    @Autowired
    public WithOAuth2AuthenticationSecurityContextFactory(final OAuthHelper oAuthHelper) {
        this.oAuthHelper = oAuthHelper;
    }

    /**
     * creates security context
     *
     * @param user mocked user
     * @return securityContext the SecurityContext to use
     */
    @Override
    public SecurityContext createSecurityContext(final WithOAuth2Authentication user) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                oAuthHelper.oAuth2Authentication(user.clientId(), user.username()));
        return context;
    }
}
