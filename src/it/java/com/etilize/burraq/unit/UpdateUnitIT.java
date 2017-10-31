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
import com.consol.citrus.message.*;
import com.etilize.burraq.unit.test.base.*;

/**
 * This class contains test cases related to Update Unit functionality.
 *
 * @author Nimra Inam
 * @see AbstractIT
 * @since 1.0.0
 */
public class UpdateUnitIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldUpdateUnitNameAndDescription() throws Exception {
        author("Nimra Inam");
        description("A unit should be updated");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_name_and_description_of_unit.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/update/update_name_and_description_of_unit_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldNotUpdateUnitWhenUpdatedNameAlreadyExists() throws Exception {
        author("Nimra Inam");
        description("An update operation should not produce duplicate units");

        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/duplicate_unit.json"));

        verifyResponse(HttpStatus.CONFLICT, //
                readFile("/datasets/units/update/duplicate_unit_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldUpdateBaseUnitOfUnit() throws Exception {
        author("Nimra Inam");
        description("An update operation should update base unit field of unit");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_base_unit_of_unit.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/update/update_base_unit_of_unit_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldUpdateMetricSystemOfUnit() throws Exception {
        author("Nimra Inam");
        description("An update operation should update metric system field of unit");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_metric_system_of_unit.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/update/update_metric_system_of_unit_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldUpdateFormulaOfUnit() throws Exception {
        author("Nimra Inam");
        description("An update operation should update formula field of unit");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_formula_of_unit.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/update/update_formula_of_unit_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldUpdateGroupIdOfUnit() throws Exception {
        author("Nimra Inam");
        description("An update operation should update group id field of unit");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_group_id_of_unit.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/update/update_group_id_of_unit_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldUpdateUnitWithDefaultWhenNullInFormula() throws Exception {
        author("Nimra Inam");
        description("An update operation should update unit with default value in formula when null is added");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_unit_with_null_in_formula.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/update/update_unit_with_null_in_formula_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldUpdateUnitWithDefaultWhenNullInMetricSystem() throws Exception {
        author("Nimra Inam");
        description("An update operation should update unit with default value in metric system when null is added");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_unit_with_null_in_metric_system.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/update/update_unit_with_null_in_metric_system_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldUpdateUnitWithDefaultWhenNullInBaseUnit() throws Exception {
        author("Nimra Inam");
        description("An update operation should update unit with default when null is added in Base Unit");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_unit_with_null_in_base_unit.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/update/update_unit_with_null_in_base_unit_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldNotUpdateUnitWithNullInGroupId() throws Exception {
        author("Nimra Inam");
        description("An update operation should not update unit with null in Group Id");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        variable("entity", "Unit");
        variable("property", "groupId");
        variable("message", "groupId is required");

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_unit_with_null_in_group_id.json"));

        http().client(serviceClient) //
                .receive() //
                .response(HttpStatus.BAD_REQUEST) //
                .messageType(MessageType.JSON) //
                .validate("$.errors[*].entity", "${entity}") //
                .validate("$.errors[*].property", "${property}") //
                .validate("$.errors[*].message", "${message}");
    }

    @Test
    @CitrusTest
    public void shouldNotUpdateUnitWithEmptyStringInName() throws Exception {
        author("Nimra Inam");
        description(
                "An update operation should not update unit with empty string in name");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        variable("entity", "Unit");
        variable("property", "name");
        variable("message", "name is required");

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_unit_with_empty_string_in_name.json"));

        http().client(serviceClient) //
                .receive() //
                .response(HttpStatus.BAD_REQUEST) //
                .messageType(MessageType.JSON) //
                .validate("$.errors[*].entity", "${entity}") //
                .validate("$.errors[*].property", "${property}") //
                .validate("$.errors[*].message", "${message}");
    }

    @Test
    @CitrusTest
    public void shouldUpdateUnitWithDefaultWhenEmptyStringInFormula() throws Exception {
        author("Nimra Inam");
        description(
                "An update operation should update unit with default when empty string is given in formula");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_unit_with_empty_string_in_formula.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/update/update_unit_with_empty_string_in_formula_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    @Ignore
    public void shouldNotUpdateUnitWithEmptyStringInMetricSystem() throws Exception {
        author("Nimra Inam");
        description(
                "An update operation should not update unit with empty string in Metric System");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_unit_with_empty_string_in_metric_system.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/update/update_unit_with_empty_string_in_metric_system_error.json"));
    }
}