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

import org.apache.http.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * This class contains test cases related to Update Group functionality.
 *
 * @author Nimra Inam
 * @see AbstractIT
 * @since 1.0.0
 */
public class UpdateGroupIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldUpdateGroup() throws Exception {
        author("Nimra Inam");
        description("A group should not be updated");

        variable(LOCATION_HEADER_VALUE, "");
        variable(GROUP_ID, EXISTING_GROUP_ID);

        putRequest(GROUPS_URL, "${groupId}",
                readFile("/datasets/groups/group_to_update.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        parseAndSetVariable(GROUPS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, readFile("/datasets/groups/group_after_put.json"),
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldNotUpdateGroupWhenUpdatedNameAlreadyExists() throws Exception {
        author("Nimra Inam");
        description("An update operation should not produce dupliate unit groups");

        variable(GROUP_ID, EXISTING_GROUP_ID);

        putRequest(GROUPS_URL, "${groupId}",
                readFile("/datasets/groups/duplicate/duplicate_group_to_update.json"));

        verifyResponse(HttpStatus.CONFLICT,
                readFile("/datasets/groups/errors/duplicate_group_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotUpdateGroupWhichAlreadyExistsWithSameNameButWithDifferentCase()
            throws Exception {
        author("Nimra Inam");
        description(
                "An existing unit group should not be updated by just converting it to a different case");

        variable(GROUP_ID, EXISTING_GROUP_ID);

        putRequest(GROUPS_URL, "${groupId}", readFile(
                "/datasets/groups/case_insensitive_matching/case_insensitive_matching_group.json"));

        verifyResponse(HttpStatus.CONFLICT,
                readFile("/datasets/groups/errors/duplicate_group_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotUpdateGroupWithMissingNameAndDescription() throws Exception {
        author("Nimra Inam");
        description(
                "A unit group with missing name and description should not be updated");

        variable(GROUP_ID, EXISTING_GROUP_ID);

        putRequest(GROUPS_URL, "${groupId}", readFile(
                "/datasets/groups/missing_null_groups/group_with_missing_name_and_description_to_update.json"));

        verifyResponse(HttpStatus.BAD_REQUEST,
                readFile("/datasets/groups/errors/null_group_error.json"));
    }

}
