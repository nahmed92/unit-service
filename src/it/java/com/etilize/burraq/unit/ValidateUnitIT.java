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
    public void shouldNotAddMoreThanTwoBaseUnitsInOneGroupOnAdd() throws Exception {
        author("Nimra Inam");
        description(
                "More than two base units should not be added in the same group on add");

        postRequest(UNITS_URL, //
                readFile(
                        "/datasets/units/validations/third_base_unit_in_same_group_to_add.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile(
                        "/datasets/units/validations/more_than_two_base_units_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddMoreThanTwoBaseUnitsInOneGroupOnUpdate() throws Exception {
        author("Nimra Inam");
        description(
                "More than two base units should not be added in the same group on update");

        variable(UNIT_ID, "59df4d4f0fcdf86d872acdfc");

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile(
                        "/datasets/units/validations/third_base_unit_in_same_group_to_update.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile(
                        "/datasets/units/validations/more_than_two_base_units_after_update_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddSameUnitInADifferentGroupOnAdd() throws Exception {
        author("Nimra Inam");
        description("Same Unit should not be added in a different group on add");

        postRequest(UNITS_URL, //
                readFile(
                        "/datasets/units/validations/same_unit_in_a_different_group.json"));

        verifyResponse(HttpStatus.CONFLICT, //
                readFile(
                        "/datasets/units/validations/same_unit_in_a_different_group_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddSameUnitInADifferentGroupOnUpdate() throws Exception {
        author("Nimra Inam");
        description("Same Unit should not be added in a different group on update");

        variable(UNIT_ID, "59cc914b2a26200bc964e26e");

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile(
                        "/datasets/units/validations/same_unit_in_a_different_group_after_update.json"));

        verifyResponse(HttpStatus.CONFLICT, //
                readFile(
                        "/datasets/units/validations/same_unit_in_a_different_group_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitToAGroupWhichDoesNotExist()
            throws Exception {
        author("Nimra Inam");
        description(
                "Bad request should be returned on adding a unit to a group which does not exist");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile(
                        "/datasets/units/validations/unit_with_group_does_not_exist.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile(
                        "/datasets/units/validations/unit_with_group_does_not_exist_error.json"));
    }
}
