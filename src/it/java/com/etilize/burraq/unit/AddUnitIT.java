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
 * This class contains test cases related Add Unit functionality with different metric
 * systems with unit and base units.
 *
 * @author Nimra Inam
 * @see AbstractIT
 * @since 1.0.0
 */
public class AddUnitIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldAddUnitWithMKSMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with MKS metric system");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, readFile( //
                "/datasets/units/metris_systems/unit/unit_with_mks_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/metris_systems/unit/unit_with_mks_metric_system_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithCGSMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with CGS metric system");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/metris_systems/unit/unit_with_cgs_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/metris_systems/unit/unit_with_cgs_metric_system_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithMTSMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with MTS metric system");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/metris_systems/unit/unit_with_mts_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/metris_systems/unit/unit_with_mts_metric_system_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithNoneMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with CGS metric system");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/metris_systems/unit/unit_with_none_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, readFile( //
                "/datasets/units/metris_systems/unit/unit_with_none_metric_system_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddBaseUnitWithCGSMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A base unit with CGS metric system should be added");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/metris_systems/base_unit/base_unit_with_cgs_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/metris_systems/base_unit/base_unit_with_cgs_metric_system_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddBaseUnitWithMTSMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A base unit with MTS metric system should be added");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/metris_systems/base_unit/base_unit_with_mts_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/metris_systems/base_unit/base_unit_with_mts_metric_system_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddBaseUnitWithNoneMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A base unit with None metric system should be added");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/metris_systems/base_unit/base_unit_with_none_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/metris_systems/base_unit/base_unit_with_none_metric_system_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldNotAddUnitWithoutName() throws Exception {
        author("Nimra Inam");
        description("A unit without name should not be added");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/missing_values/unit_without_name.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/validations/missing_values/unit_without_name_response.json"));
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithoutFormulaWithDefaultValue() throws Exception {
        author("Nimra Inam");
        description(
                "A unit without formula should be added with the default value of [value]");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/missing_values/unit_without_formula.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/missing_values/unit_without_formula_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithoutBaseUnit() throws Exception {
        author("Nimra Inam");
        description("A unit without base unit field should be added");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/missing_values/unit_without_base_unit.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/missing_values/unit_without_base_unit_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithoutMetricSystemAsNone() throws Exception {
        author("Nimra Inam");
        description(
                "A unit without metric system should be added with default value as None");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/missing_values/unit_without_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/missing_values/unit_without_metric_system_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldNotAddUnitWithoutGroupId() throws Exception {
        author("Nimra Inam");
        description("A unit without group id should not be added");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/missing_values/unit_without_group_id.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/validations/missing_values/unit_without_group_id_response.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddUnitWithANonExistingGroup() throws Exception {
        author("Nimra Inam");
        description("A unit with a non existing group should not be added");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/missing_values/unit_with_non_existing_group.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/validations/missing_values/unit_with_non_existing_group_response.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddUnitWithNullInName() throws Exception {
        author("Nimra Inam");
        description("A unit should not be added with null in name");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/null_values/unit_with_null_in_name.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/validations/null_values/unit_with_null_in_name_response.json"));

    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithNullInFormula() throws Exception {
        author("Nimra Inam");
        description("A unit should not be added with null in formula");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/null_values/unit_with_null_in_formula.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/null_values/unit_with_null_in_formula_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithNullInMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A unit should not be added with null in metric system");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/null_values/unit_with_null_in_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/null_values/unit_with_null_in_metric_system_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithNullInBaseUnit() throws Exception {
        author("Nimra Inam");
        description("A unit should not be added with null in base unit");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/null_values/unit_with_null_in_base_unit.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/null_values/unit_with_null_in_base_unit_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldNotAddUnitWithNullInGroupId() throws Exception {
        author("Nimra Inam");
        description("A unit should not be added with null in group id");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/null_values/unit_with_null_in_group_id.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/validations/null_values/unit_with_null_in_group_id_response.json"));
    }

    @Test
    @CitrusTest
    public void shouldNotAddUnitWithEmptyStringInName() throws Exception {
        author("Nimra Inam");
        description("A unit should not be added with empty string in name");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/empty_string/unit_with_empty_string_in_name.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/validations/empty_string/unit_with_empty_string_in_name_response.json"));
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithEmptyStringInFormula() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with empty string in formula");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/empty_string/unit_with_empty_string_in_formula.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/empty_string/unit_with_empty_string_in_formula_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @Ignore
    @CitrusTest
    public void shouldNotAddUnitWithEmptyStringInMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with empty string in formula");

        variable("error",
                "value not one of declared Enum instance names: [NONE, MTS, MKS, CGS]");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/empty_string/unit_with_empty_string_in_metric_system.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/validations/empty_string/unit_with_empty_string_in_metric_system_response.json"));
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithEmptyStringInBaseUnit() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with empty string in base unit");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/empty_string/unit_with_empty_string_in_base_unit.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/empty_string/unit_with_empty_string_in_base_unit_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @Ignore
    @CitrusTest
    public void shouldNotAddUnitWithEmptyStringInGroupId() throws Exception {
        author("Nimra Inam");
        description("A unit should not be added with empty string in group id");

        variable("error", "invalid hexadecimal representation");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/empty_string/unit_with_empty_string_in_group_id.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/validations/empty_string/unit_with_empty_string_in_group_id_response.json"));
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithMixedLetters() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with mixed letters");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/different_types/unit_with_mixed_letters.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/different_types/unit_with_mixed_letters_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithSpecialCharacters() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with special characters");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/different_types/unit_with_special_characters.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/different_types/unit_with_special_characters_response.json"), //
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithAllNumbers() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with all numbers");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/validations/different_types/unit_with_all_numbers.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/validations/different_types/unit_with_all_numbers_response.json"), //
                "${locationHeaderValue}");
    }

}
