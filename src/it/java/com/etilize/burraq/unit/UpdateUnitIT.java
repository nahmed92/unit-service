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
import com.consol.citrus.context.TestContext;
import com.consol.citrus.annotations.*;
import com.consol.citrus.http.message.HttpMessage;
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
    public void shouldUpdateUnitNameAndDescription(@CitrusResource TestContext context)
            throws Exception {
        author("Nimra Inam");
        description("A unit should be updated");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile(
                        "/datasets/units/update/update_name_and_description_of_unit.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        String unitLocation = parseAndSetVariable(UNITS_URL, //
                context.getVariable("${" + LOCATION_HEADER_VALUE + "}"));
        verifyResponse(HttpStatus.OK, //
                readFile(
                        "/datasets/units/update/update_name_and_description_of_unit_response.json"), //
                unitLocation);
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
    public void shouldUpdateBaseUnitOfUnit(@CitrusResource TestContext context)
            throws Exception {
        author("Nimra Inam");
        description("An update operation should update base unit field of unit");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_base_unit_of_unit.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        String unitLocation = parseAndSetVariable(UNITS_URL, //
                context.getVariable("${" + LOCATION_HEADER_VALUE + "}"));
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/update/update_base_unit_of_unit_response.json"), //
                unitLocation);
    }

    @Test
    @CitrusTest
    public void shouldUpdateMeasuringSystemOfUnit(@CitrusResource TestContext context)
            throws Exception {
        author("Nimra Inam");
        description("An update operation should update measuring system field of unit");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_measuring_system_of_unit.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        String unitLocation = parseAndSetVariable(UNITS_URL, //
                context.getVariable("${" + LOCATION_HEADER_VALUE + "}"));
        verifyResponse(HttpStatus.OK, //
                readFile(
                        "/datasets/units/update/update_measuring_system_of_unit_response.json"), //
                unitLocation);
    }

    @Test
    @CitrusTest
    public void shouldUpdateFormulaOfUnit(@CitrusResource TestContext context)
            throws Exception {
        author("Nimra Inam");
        description("An update operation should update formula field of unit");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_formula_of_unit.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        String unitLocation = parseAndSetVariable(UNITS_URL, //
                context.getVariable("${" + LOCATION_HEADER_VALUE + "}"));
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/update/update_formula_of_unit_response.json"), //
                unitLocation);
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnUpdatingGroupIdOfUnit() throws Exception {
        author("Nimra Inam");
        description("An update operation should not update group id of a unit");

        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_group_id_of_unit.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/update/update_group_id_of_unit_response.json"));
    }

    @Test
    @CitrusTest
    public void shouldUpdateUnitWithDefaultWhenNullInFormula(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "An update operation should update unit with default value in formula when null is added");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile("/datasets/units/update/update_unit_with_null_in_formula.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        String unitLocation = parseAndSetVariable(UNITS_URL, //
                context.getVariable("${" + LOCATION_HEADER_VALUE + "}"));
        verifyResponse(HttpStatus.OK, //
                readFile(
                        "/datasets/units/update/update_unit_with_null_in_formula_response.json"), //
                unitLocation);
    }

    @Test
    @CitrusTest
    public void shouldUpdateUnitWithDefaultWhenNullInMeasuringSystem(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "An update operation should update unit with default value in measuring system when null is added");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile(
                        "/datasets/units/update/update_unit_with_null_in_measuring_system.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        String unitLocation = parseAndSetVariable(UNITS_URL, //
                context.getVariable("${" + LOCATION_HEADER_VALUE + "}"));
        verifyResponse(HttpStatus.OK, //
                readFile(
                        "/datasets/units/update/update_unit_with_null_in_measuring_system_response.json"), //
                unitLocation);
    }

    @Test
    @CitrusTest
    public void shouldUpdateUnitWithDefaultWhenNullInBaseUnit(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "An update operation should update unit with default when null is added in Base Unit");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile(
                        "/datasets/units/update/update_unit_with_null_in_base_unit.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        String unitLocation = parseAndSetVariable(UNITS_URL, //
                context.getVariable("${" + LOCATION_HEADER_VALUE + "}"));
        verifyResponse(HttpStatus.OK, //
                readFile(
                        "/datasets/units/update/update_unit_with_null_in_base_unit_response.json"), //
                unitLocation);
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
                readFile(
                        "/datasets/units/update/update_unit_with_null_in_group_id.json"));

        receive(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage() //
                        .status(HttpStatus.BAD_REQUEST)) //
                .messageType(MessageType.JSON) //
                .validate("$.errors[*].entity", "${entity}") //
                .validate("$.errors[*].property", "${property}") //
                .validate("$.errors[*].message", "${message}"));
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
                readFile(
                        "/datasets/units/update/update_unit_with_empty_string_in_name.json"));

        receive(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage() //
                        .status(HttpStatus.BAD_REQUEST)) //
                .messageType(MessageType.JSON) //
                .validate("$.errors[*].entity", "${entity}") //
                .validate("$.errors[*].property", "${property}") //
                .validate("$.errors[*].message", "${message}"));
    }

    @Test
    @CitrusTest
    public void shouldUpdateUnitWithDefaultWhenEmptyStringInFormula(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "An update operation should update unit with default when empty string is given in formula");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile(
                        "/datasets/units/update/update_unit_with_empty_string_in_formula.json"));

        extractHeader(HttpStatus.OK, HttpHeaders.LOCATION);
        String unitLocation = parseAndSetVariable(UNITS_URL, //
                context.getVariable("${" + LOCATION_HEADER_VALUE + "}"));
        verifyResponse(HttpStatus.OK, //
                readFile(
                        "/datasets/units/update/update_unit_with_empty_string_in_formula_response.json"), //
                unitLocation);
    }

    @Test
    @CitrusTest
    @Ignore
    public void shouldNotUpdateUnitWithEmptyStringInMeasuringSystem() throws Exception {
        author("Nimra Inam");
        description(
                "An update operation should not update unit with empty string in Measuring System");

        variable(LOCATION_HEADER_VALUE, "");
        variable(UNIT_ID, EXISTING_UNIT_ID_TO_UPDATE);

        putRequest(UNITS_URL, //
                "${" + UNIT_ID + "}", //
                readFile(
                        "/datasets/units/update/update_unit_with_empty_string_in_measuring_system.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile(
                        "/datasets/units/update/update_unit_with_empty_string_in_measuring_system_error.json"));
    }
}
