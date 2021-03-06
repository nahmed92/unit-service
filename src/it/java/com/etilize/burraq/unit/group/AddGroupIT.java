/*
 * #region
 * unit-service
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

package com.etilize.burraq.unit.group;

import org.junit.*;
import org.springframework.http.*;

import com.consol.citrus.annotations.*;
import com.consol.citrus.context.*;
import com.consol.citrus.http.message.HttpMessage;
import com.consol.citrus.message.*;
import com.etilize.burraq.unit.test.base.*;

public class AddGroupIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldAddGroup(@CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description("A group should be added");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(GROUPS_URL, //
                readFile("/datasets/groups/group.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        String unitLocation = parseAndSetVariable(GROUPS_URL, //
                context.getVariable("${" + LOCATION_HEADER_VALUE + "}"));
        echo("unit is: "+ unitLocation);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/groups/group_after_post.json"), //
                unitLocation);

        // Cleanup
        deleteRequest(unitLocation,"");
    }

    @Test
    @CitrusTest
    public void shouldNotAddDuplicateGroup() throws Exception {
        author("Nimra Inam");
        description("A duplicate group should not be added");

        postRequest(GROUPS_URL, //
                readFile("/datasets/groups/duplicate/duplicate_group.json"));

        verifyResponse(HttpStatus.CONFLICT, //
                readFile("/datasets/groups/errors/duplicate_group_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddGroupWhichAlreadyExistsWithSameNameButWithDifferentCase()
            throws Exception {
        author("Nimra Inam");
        description("A case insensitive matching group should not be added");

        postRequest(GROUPS_URL, //
                readFile(
                        "/datasets/groups/case_insensitive_matching/case_insensitive_matching_group.json"));

        verifyResponse(HttpStatus.CONFLICT, //
                readFile("/datasets/groups/errors/duplicate_group_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddGroupWithEmptyNameAndDescription() throws Exception {
        author("Nimra Inam");
        description("A group with empty name and description should not be added");

        variable("jsonForEmptyUnitNameAndDescription",
                "{\"name\": \"\",\"description\": \"\"}");
        variable("nameProperty", "name");
        variable("descriptionProperty", "description");
        variable("nameMessage", "name is required");
        variable("descriptionMessage", "description is required");
        variable("invalidValue", "");

        postRequest(GROUPS_URL, "${jsonForEmptyUnitNameAndDescription}");

        receive(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage() //
                        .status(HttpStatus.BAD_REQUEST)) //
                .messageType(MessageType.JSON) //
                .validate("$.errors[*].property",
                        "@assertThat(allOf(containsString(${nameProperty}),containsString(${descriptionProperty})))@") //
                .validate("$.errors[*].message",
                        "@assertThat(allOf(containsString(${nameMessage}), containsString(${descriptionMessage})))@") //
                .validate("$.errors[*].invalidValue",
                        "@assertThat(allOf(containsString(${invalidValue})))@"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddGroupWithMissingNameAndDescription() throws Exception {
        author("Nimra Inam");
        description("A group with missing name and description should not be added");

        variable("jsonForNullUnitNameAndDescription",
                "{\"name\": null,\"description\": null}");
        variable("nameProperty", "name");
        variable("descriptionProperty", "description");
        variable("nameMessage", "name is required");
        variable("descriptionMessage", "description is required");
        variable("invalidValue", "null");

        postRequest(GROUPS_URL, "${jsonForNullUnitNameAndDescription}");

        receive(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage() //
                        .status(HttpStatus.BAD_REQUEST)) //
                .messageType(MessageType.JSON) //
                .validate("$.errors[*].property",
                        "@assertThat(allOf(containsString(${nameProperty}),containsString(${descriptionProperty})))@") //
                .validate("$.errors[*].message",
                        "@assertThat(allOf(containsString(${nameMessage}), containsString(${descriptionMessage})))@") //
                .validate("$.errors[*].invalidValue",
                        "@assertThat(allOf(containsString(${invalidValue})))@"));
    }

}
