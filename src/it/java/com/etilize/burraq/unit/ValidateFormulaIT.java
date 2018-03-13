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

public class ValidateFormulaIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithTextInFormula() throws Exception {
        author("Nimra Inam");
        description("Adding a unit with text in formula should return bad request");

        variable("formula", "[value]test/0");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithInvalidOperatorInFormula()
            throws Exception {
        author("Nimra Inam");
        description(
                "Adding a unit with invalid operator in formula should return bad request");

        variable("formula", "[value]#0");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithRepeatedOperatorInFormula()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit with Repeated Operator in formula should return bad request");

        variable("formula", "[value]**0");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithMissingEndingBracketsInFormula()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit with missing ending brackets in formula should return bad request");

        variable("formula", "[value]*(10/2");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithTextAndMissingOperatorInFormula()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit with text and missing operator in formula should return bad request");

        variable("formula", "[value]my test");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithMissingStartingAndEndingBracketsInFormula()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit with missing starting and ending brackets in formula should return bad request");

        variable("formula", "value])1000*(3+4");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithoutPlaceholderBracketsInFormula()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit without placeholder brackets in fromula should return bad request");

        variable("formula", "value)+50");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithOperatorInStartAndEndOfFormula()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit with operator in start and end of fromula should return bad request");

        variable("formula", "*[value]/");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithEmptyBracketsInFormula()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit with empty brackets in fromula should return bad request");

        variable("formula", "([value]+2)/{}");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithTextInFormulaPlaceholder()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit with text in formula placeholder should return bad request");

        variable("formula", "[myvalue]/1000");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithOnlyTextInFormula()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit with only text in formula should return bad request");

        variable("formula", "this is a formula");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithFormulaWithoutSquareBracketsInPlaceholder()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit without square brackets in formula placeholder should return bad request");

        variable("formula", "value*1000");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithIncompleteFormula()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit with incomplete formula should return bad request");

        variable("formula", "1000%");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithSpecialCharactersInFormula()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit with special characters in formula should return bad request");

        variable("formula", "!@#$%^&*()~<>/?;:'|,.=");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithSymbolsInFormula()
            throws Exception {
        author("Nimra Inam");
        description("Adding a unit with symbols in formula should return bad request");

        variable("formula", "°F/Wµft²");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithOnlyOperatorsInFormula()
            throws Exception {
        author("Nimra Inam");
        description("A unit with only operators in formula should return bad request");

        variable("formula", "/*-+-^%");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithAllTypeOfBracketsInFormula(
            @CitrusResource TestContext context) throws Exception {
        author("Nimra Inam");
        description("A unit with all type of brackets in formula should be added");

        variable(LOCATION_HEADER_VALUE, "");
        variable("formula", "[{([value] + 2) * (6 - 4) + 2} * 4]");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        String unitLocation = parseAndSetVariable(UNITS_URL, //
                context.getVariable("${" + LOCATION_HEADER_VALUE + "}"));
        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/formula_validations/formula_with_all_brackets.json"), //
                unitLocation);

        // Cleanup
        deleteRequest(unitLocation,"");
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithLiteralOperatorsInFormula()
            throws Exception {
        author("Nimra Inam");
        description("A unit with literal mathematical operators in formula should return bad request");

        variable("formula", "[{([value] + 2) × (6 ÷ 4) + 2} × 4]");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }

    @Test
    @CitrusTest
    public void shouldReturnBadRequestOnAddingUnitWithMixedCaseFormulaPlaceholder()
            throws Exception {
        author("Nimra Inam");
        description("A unit with mixed case formula operator should return bad request");

        variable("formula", "[vALue]*1000");

        postRequest(UNITS_URL, //
                readFile("/datasets/units/formula_validations/unit_with_formula.json"));

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/formula_validations/invalid_formula_error.json"));
    }
}
