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

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.*;
import com.etilize.burraq.unit.test.base.*;

/**
 * This class contains test cases related Get Group(s) functionality.
 *
 * @author Nimra Inam
 * @see AbstractIT
 * @since 1.0.0
 */
public class FindGroupsIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldFindAllGroups() throws Exception {
        author("Nimra Inam");
        description("All existing groups should return");

        variable("urlToCheck", "/groups");
        variable("pageSize", "20");
        variable("totalElements", "3");
        variable("totalPages", "1");
        variable("pageNumber", "0");

        getRequest(GROUPS_URL);

        http().client(serviceClient) //
                .receive() //
                .response(HttpStatus.OK) //
                .messageType(MessageType.JSON) //
                .validate("$._embedded.groups[*].name",
                        "@assertThat(not(isEmptyString())@") //
                .validate("$._embedded.groups[*].description",
                        "@assertThat(not(isEmptyString())@") //
                .validate("$._embedded.groups[*]._links.self.href",
                        "@contains(${urlToCheck})@") //
                .validate("$._embedded.groups[*]._links.group.href",
                        "@contains(${urlToCheck})@") //
                .validate("$.page.size", "${pageSize}") //
                .validate("$.page.totalElements",
                        "@assertThat(greaterThanOrEqualTo(${totalElements}))@") //
                .validate("$.page.totalPages",
                        "@assertThat(greaterThanOrEqualTo(${totalPages}))@") //
                .validate("$.page.number", "${pageNumber}");

    }

    @Test
    @CitrusTest
    public void shouldFindGroupById() {
        author("Nimra Inam");
        description("A group with matching id should be returned");

        variable("groupId", "5995843b0fcdf874985e399d");
        variable("name", "resolution");
        variable("description", "this is resolution unit group");

        getRequest(GROUPS_URL + "${groupId}");

        http().client(serviceClient) //
                .receive() //
                .response(HttpStatus.OK) //
                .messageType(MessageType.JSON) //
                .validate("$.name", "${name}") //
                .validate("$.description", "${description}") //
                .validate("$._links.self.href", "@endsWith(${groupId})@") //
                .validate("$._links.group.href", "@endsWith(${groupId})@");
    }

    @Test
    @CitrusTest
    public void shouldReturnStatusNotFoundWhenGroupDoesNotExist() {
        author("Nimra Inam");
        description("Validate response when group id does not exist");

        variable("groupId", "59b7ef3b74e8ef91a9cfb1ef");

        getRequest(GROUPS_URL + "${groupId}");

        http().client(serviceClient) //
                .receive() //
                .response(HttpStatus.NOT_FOUND) //
                .messageType(MessageType.JSON);
    }

    @Test
    @CitrusTest
    public void shouldFindGroupByName() throws Exception {
        author("Nimra Inam");
        description("Validate response while finding group by name");

        variable("groupId", "599585660fcdf874985e399e");
        variable("name", "weight");

        getRequest(GROUPS_URL + "?name=weight");

        verifyResponse(HttpStatus.OK,
                readFile("/datasets/groups/find/find_group_by_name.json"));
    }

    @Test
    @CitrusTest
    public void shouldFindGroupByDescription() throws Exception {
        author("Nimra Inam");
        description("Validate response while finding group by description");

        variable("groupId", "599585660fcdf874985e399e");
        variable("description", "this is weight unit group");

        getRequest(GROUPS_URL + "?description=" + "${description}");

        verifyResponse(HttpStatus.OK,
                readFile("/datasets/groups/find/find_group_by_description.json"));
    }

    @Test
    @CitrusTest
    public void shouldFindGroupByNameAndDescription() throws Exception {
        author("Nimra Inam");
        description("Validate response while finding group by name and description");

        variable("groupId", "599585660fcdf874985e399e");
        variable("name", "weight");
        variable("description", "this is weight unit group");

        getRequest(GROUPS_URL + "?name=weight&description=this is weight unit group");

        verifyResponse(HttpStatus.OK, readFile(
                "/datasets/groups/find/find_group_by_name_and_description.json"));
    }

    @Test
    @CitrusTest
    public void shouldRetunNoRecordWhenMatchingNameDoesNotExist() throws Exception {
        author("Nimra Inam");
        description("Validate response while finding group by name which does not exist");

        getRequest(GROUPS_URL + "?name=groupNoFound");

        verifyResponse(HttpStatus.OK,
                readFile("/datasets/groups/find/find_group_which_does_not_exist.json"));
    }

    @Test
    @CitrusTest
    public void shouldRetunNoRecordWhenMatchingDescriptionDoesNotExist()
            throws Exception {
        author("Nimra Inam");
        description(
                "Validate response while finding group by description which does not exist");

        getRequest(GROUPS_URL + "?description=groupNoFound");

        verifyResponse(HttpStatus.OK,
                readFile("/datasets/groups/find/find_group_which_does_not_exist.json"));
    }

}
