/*
 * #region
 * Unit Service
 * %%
 * Copyright (C) 2017 Etilize
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

package com.etilize.burraq.unit;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.apache.http.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import com.consol.citrus.TestCaseMetaInfo.Status;
import com.consol.citrus.actions.*;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.*;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import java.io.IOException;
import com.etilize.burraq.unit.test.base.*;

public class UpdateGroupIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldUpdateGroup() throws Exception {
        // Variable to hold location header
        variable("locationHeaderValue", ""); //
        // Test case metadata
        author("ninam");
        description("A group should not be updated");
        variable("groupId", "5995843b0fcdf874985e399d");
        // Sends an update request for an existing group
        putRequest(GROUPS_URL, "${groupId}",
                readFile("/datasets/groups/group_to_update.json"));
        // Extract header location
        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        action(new AbstractTestAction() {

            @Override
            public void doExecute(final TestContext context) {
                final String location = context.getVariable("locationHeaderValue");
                final String newLocation = location.substring(
                        location.indexOf(GROUPS_URL));
                context.setVariable("locationHeaderValue", newLocation);
            }
        });
        // Verify updated group
        verifyResponse(HttpStatus.OK, readFile("/datasets/groups/group_after_put.json"),
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldNotUpdateGroupWhenUpdatedNameAlreadyExists() throws Exception {
        // Variable to hold location header
        variable("groupId", "5995843b0fcdf874985e399d");
        // Test case metadata
        author("ninam");
        description("An update operation should not produce dupliate unit groups");
        // Send an update request for an existing group
        putRequest(GROUPS_URL, "${groupId}",
                readFile("/datasets/groups/duplicate/duplicate_group_to_update.json"));
        // Verify response
        verifyResponse(HttpStatus.CONFLICT,
                readFile("/datasets/groups/errors/duplicate_group_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotUpdateGroupWhichAlreadyExistsWithSameNameButWithDifferentCase()
            throws Exception {
        // Variable to hold location header
        variable("groupId", "5995843b0fcdf874985e399d");
        // Test case metadata
        author("ninam");
        description(
                "An existing unit group should not be updated by just converting it to a different case");
        // Send an update request for an existing group
        putRequest(GROUPS_URL, "${groupId}", readFile(
                "/datasets/groups/case_insensitive_matching/case_insensitive_matching_group.json"));
        // Verify response
        verifyResponse(HttpStatus.CONFLICT,
                readFile("/datasets/groups/errors/duplicate_group_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotUpdateGroupWithMissingNameAndDescription() throws Exception {
        // Variable to hold location header
        variable("groupId", "5995843b0fcdf874985e399d");
        // Test case metadata
        author("ninam");
        description(
                "A unit group with missing name and description should not be updated");
        // Sends a put request to api and verify response too
        putRequest(GROUPS_URL, "${groupId}", readFile(
                "/datasets/groups/missing_null_groups/group_with_missing_name_and_description_to_update.json"));
        // Verify response
        verifyResponse(HttpStatus.BAD_REQUEST,
                readFile("/datasets/groups/errors/null_group_error.json"));
    }

}
