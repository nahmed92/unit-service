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

package com.etilize.burraq.unit.group;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import com.etilize.burraq.unit.test.AbstractIntegrationTest;
import com.etilize.burraq.unit.test.security.WithOAuth2Authentication;

@WithOAuth2Authentication(username = "ROLE_PTM", clientId = "unauthorized")
public class GroupRepositoryAuthorizationTest extends AbstractIntegrationTest {

    @Autowired
    private GroupRepository repository;

    @Test(expected = AccessDeniedException.class)
    public void shouldThrowAccessDeniedExceptionExceptionWhenUnAuthorizedUserCreatesNewGroup()
            throws Exception {
        final Group group = new Group("pressure", "This is pressure group");
        repository.save(group);
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldThrowAccessDeniedExceptionExceptionWhenUnAuthorizedUserUpdatesGroup()
            throws Exception {
        final Group group = new Group(null, "This is distance group");
        group.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        repository.save(group);
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldThrowAccessDeniedExceptionExceptionWhenUnAuthorizedUserDeletesGroup()
            throws Exception {
        Group group = repository.findOne(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        repository.delete(group);
    }
}
