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

package com.etilize.burraq.unit;

import org.junit.*;
import org.springframework.http.*;

import com.consol.citrus.annotations.*;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.etilize.burraq.unit.test.base.*;

/**
 * This class contains test cases related Add Unit functionality with different metric
 * systems with unit and base units.
 *
 * @author Nimra Inam
 * @see AbstractIT
 * @since 1.0.0
 */
public class ValidateUnitIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldNotAddTwoSameUnitsInSameGroupOnAdd() throws Exception {
        author("Nimra Inam");
        description("Two similar units should not be added in the same group on add");

        postRequest(UNITS_URL, readFile("/datasets/units/validations/unit.json"));

        verifyResponse(HttpStatus.CONFLICT, //
                readFile("/datasets/units/validations/similar_units_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddTwoSameUnitsInSameGroupOnUpdate() throws Exception {
        author("Nimra Inam");
        description("Two similar units should not be added in the same group on update");

        variable(UNIT_ID, "59df4a400fcdf86d872acdfb");

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/validations/update_unit.json"));

        verifyResponse(HttpStatus.CONFLICT, //
                readFile("/datasets/units/validations/similar_units_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddSameUnitInADifferentGroupOnAdd() throws Exception {
        author("Nimra Inam");
        description("Same Unit should not be added in a different group on add");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/same_unit_in_a_different_group.json"));

        verifyResponse(HttpStatus.CONFLICT, //
                readFile("/datasets/units/validations/same_unit_in_a_different_group_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddSameUnitInADifferentGroupOnUpdate() throws Exception {
        author("Nimra Inam");
        description("Same Unit should not be added in a different group on update");

        variable(UNIT_ID, "59cc914b2a26200bc964e26e");

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/validations/same_unit_in_a_different_group_after_update.json"));

        verifyResponse(HttpStatus.CONFLICT, //
                readFile("/datasets/units/validations/same_unit_in_a_different_group_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitToAGroupWhichDoesNotExist()
            throws Exception {
        author("Nimra Inam");
        description("Bad request should be returned on adding a unit to a group which does not exist");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/unit_with_group_does_not_exist.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/validations/unit_with_group_does_not_exist_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldSetBaseUnitIdWhenFirstUnitIsAddedToAGroup(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description("The frist unit to a group should be added as a base unit");

        variable(LOCATION_HEADER_VALUE, "");
        variable("existingGroup", "5a1805530fcdf812bee4dd66");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/first_unit_in_group_request.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        String resourceLocation = parseAndSetVariable(UNITS_URL,
                context.getVariable("${" + LOCATION_HEADER_VALUE + "}"));
        String resourceId = parseAndSetResourceId(resourceLocation);
        variable("baseUnitId", resourceId);

        // Verify Unit
        getRequest(UNITS_URL + "?groupId=${existingGroup}&isBaseUnit=true");
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/first_unit_in_group_response.json"));

        // Verify Group
        getRequest(GROUPS_URL + "?id=${existingGroup}");
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/groups/group_with_base_unit_response.json"));
    }

    @Test
    @CitrusTest
    public void shouldSetIsBaseUnitToFalseForAnyUnitAfterFirstUnitToAGroup()
            throws Exception {
        author("Nimra Inam");
        description("Unit after the first unit to a group should be added with isBaseUnit set to false");

        variable("existingGroup", "5a1d3e560fcdf812bee4e099");
        variable("existingUnit", "5a1eac070fcdf812bee4e270");

        // Second Unit
        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/second_unit_in_group_request.json"));

        // Verify Unit
        getRequest(UNITS_URL + "?groupId=${existingGroup}&isBaseUnit=false");
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/second_unit_in_group_response.json"));

        // Verify Group
        getRequest(GROUPS_URL + "?id=${existingGroup}");
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/groups/group_with_two_units_and_base_unit_response.json"));
    }
}
