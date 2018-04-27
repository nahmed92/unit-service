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

package com.etilize.burraq.unit.convert;

import org.junit.*;
import org.springframework.http.*;

import com.consol.citrus.annotations.*;
import com.consol.citrus.context.*;
import com.etilize.burraq.unit.test.base.*;

/**
 * This class contains test cases related to unit conversion
 *
 * @author Nimra Inam
 * @see AbstractIT
 * @since 1.0.0
 */
public class ConvertUnitIT extends AbstractIT {

    /*
     * There are 3 units used in a group named "time" for testing. Hours, Minutes and
     * seconds. Here Minutes is a set as a base unit.
     */
    @Test
    @CitrusTest
    public void shouldConvertNonBaseUnitToTargetNonBaseUnit(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description("A unit should be converted to target units i.e Hours to Seconds");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(CONVERT_URL, readFile( //
                "/datasets/units/convert/non_base_units_conversion_request.json"));

        verifyResponse(HttpStatus.OK, //
                readFile(
                        "/datasets/units/convert/non_base_units_conversion_reponse.json"));

    }

    @Test
    @CitrusTest
    public void shouldConvertNonBaseUnitToTargetBaseUnit(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "A unit should be converted to target base unit i.e. Hours to Minutes");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(CONVERT_URL, readFile( //
                "/datasets/units/convert/non_base_unit_to_target_base_unit_conversion_request.json"));

        verifyResponse(HttpStatus.OK, //
                readFile(
                        "/datasets/units/convert/non_base_unit_to_target_base_unit_conversion_response.json"));

    }

    @Test
    @CitrusTest
    public void shouldConvertBaseUnitToTargetNonBaseUnit(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "A unit should be converted from base unit to target non base unit i.e. Minutes to Seconds");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(CONVERT_URL, readFile( //
                "/datasets/units/convert/base_unit_to_target_non_base_unit_conversion_request.json"));

        verifyResponse(HttpStatus.OK, //
                readFile(
                        "/datasets/units/convert/base_unit_to_target_non_base_unit_conversion_response.json"));

    }

    @Test
    @CitrusTest
    public void shouldConvertSourceUnitToMultipleTargetUnits(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "A unit should be converted to multiple target units i.e Seconds to Hours and Minutes");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(CONVERT_URL, readFile( //
                "/datasets/units/convert/source_unit_to_multiple_target_units_conversion_request.json"));

        verifyResponse(HttpStatus.OK, //
                readFile(
                        "/datasets/units/convert/source_unit_to_multiple_target_units_conversion_response.json"));

    }

    @Test
    @CitrusTest
    public void shouldReturnUnprocessableEntityOnMissingTargets(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "Unprocessable entity message should be returned when empty targets are provided");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(CONVERT_URL, readFile( //
                "/datasets/units/convert/unit_conversion_with_empty_targets_request.json"));

        verifyResponse(HttpStatus.UNPROCESSABLE_ENTITY, //
                readFile(
                        "/datasets/units/convert/unit_conversion_with_empty_targets_response.json"));

    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnMissingSourceUnitName(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "Bad request should be returned when no value provided against source unit's name");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(CONVERT_URL, readFile( //
                "/datasets/units/convert/unit_conversion_with_empty_source_unit_name_request.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile(
                        "/datasets/units/convert/unit_conversion_with_empty_source_unit_name_response.json"));

    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnMissingSourceUnitValue(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "Bad request should be returned when no value provided against source unit's value");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(CONVERT_URL, readFile( //
                "/datasets/units/convert/unit_conversion_with_empty_source_unit_value_request.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile(
                        "/datasets/units/convert/unit_conversion_with_empty_source_unit_value_response.json"));

    }

    @Test
    @CitrusTest
    public void shouldReturnUnprocessableEntityWhenUnitProvidedInSourceIsNotFoundInTargetUnitGroup(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "Unprocessable entity should be returned when target unit isn't found in source unit's group");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(CONVERT_URL, readFile( //
                "/datasets/units/convert/target_unit_not_found_in_source_unit_group_request.json"));

        verifyResponse(HttpStatus.UNPROCESSABLE_ENTITY, //
                readFile(
                        "/datasets/units/convert/target_unit_not_found_in_source_unit_group_response.json"));

    }

    @Test
    @CitrusTest
    public void shouldReturnUnprocessableEntityWhenUnitProvidedInTargetIsNotFoundInTargetUnitGroup(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "Unprocessable entity should be returned when target unit isn't found in target unit's group");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(CONVERT_URL, readFile( //
                "/datasets/units/convert/target_unit_not_found_in_target_unit_group_request.json"));

        verifyResponse(HttpStatus.UNPROCESSABLE_ENTITY, //
                readFile(
                        "/datasets/units/convert/target_unit_not_found_in_target_unit_group_response.json"));

    }

    @Test
    @CitrusTest
    public void shouldReturnUnprocessableEntityWhenUnitProvidedInSourceIsInvalid(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "Unprocessable entity should be returned when source unit is invalid OR does not exist");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(CONVERT_URL, readFile( //
                "/datasets/units/convert/invalid_source_unit_request.json"));

        verifyResponse(HttpStatus.UNPROCESSABLE_ENTITY, //
                readFile(
                        "/datasets/units/convert/invalid_source_unit_response.json"));

    }


    @Test
    @CitrusTest
    public void shouldReturnUnprocessableEntityWhenUnitProvidedInTargetIsInvalid(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description(
                "Unprocessable entity should be returned when target unit is invalid OR does not exist");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(CONVERT_URL, readFile( //
                "/datasets/units/convert/invalid_target_unit_request.json"));

        verifyResponse(HttpStatus.UNPROCESSABLE_ENTITY, //
                readFile(
                        "/datasets/units/convert/invalid_target_unit_response.json"));

    }


}