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
import com.consol.citrus.http.message.HttpMessage;
import com.consol.citrus.message.*;
import com.etilize.burraq.unit.test.base.*;

/**
 * This class contains test cases related Get Unit(s) functionality.
 *
 * @author Nimra Inam
 * @see AbstractIT
 * @since 1.0.0
 */
public class FindUnitsIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldFindAllUnits() throws Exception {
        author("Nimra Inam");
        description("Should return all existing units");

        variable("urlToCheck", "/units");
        variable("pageSize", "20");
        variable("totalElements", "8");
        variable("totalPages", "1");
        variable("pageNumber", "0");

        getRequest(UNITS_URL);

        receive(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage() //
                        .status(HttpStatus.OK)) //
                .messageType(MessageType.JSON) //
                .validate("$._embedded.units[*].name",
                        "@assertThat(not(isEmptyString())@") //
                .validate("$._embedded.units[*].description",
                        "@assertThat(not(isEmptyString())@") //
                .validate("$._embedded.units[*].groupId",
                        "@assertThat(not(isEmptyString())@") //
                .validate("$._embedded.units[*].isBaseUnit",
                        "@assertThat(not(isEmptyString())@") //
                .validate("$._embedded.units[*]._links.self.href",
                        "@contains(${urlToCheck})@") //
                .validate("$._embedded.units[*]._links.unit.href",
                        "@contains(${urlToCheck})@") //
                .validate("$.page.size", "${pageSize}") //
                .validate("$.page.totalElements",
                        "@assertThat(lessThanOrEqualTo(${totalElements}))@") //
                .validate("$.page.totalPages",
                        "@assertThat(greaterThanOrEqualTo(${totalPages}))@") //
                .validate("$.page.number", "${pageNumber}"));
    }

    @Test
    @CitrusTest
    public void shouldFindUnitById() {
        author("Nimra Inam");
        description("Should return unit with matching unit id");

        variable("unitId", "59df4d4f0fcdf86d872acdfc");
        variable("name", "bit");
        variable("description", "bit Unit");
        variable("groupId", "59df33dc0fcdf86d872acd27");
        variable("measuringSystem", "NONE");
        variable("formula", "[value] / 1000");

        getRequest(UNITS_URL + "${unitId}");

        receive(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage() //
                        .status(HttpStatus.OK)) //
                .messageType(MessageType.JSON) //
                .validate("$.name", "${name}") //
                .validate("$.description", "${description}") //
                .validate("$.groupId", "${groupId}") //
                .validate("$.measuringSystem", "${measuringSystem}") //
                .validate("$.formula", "${formula}") //
                .validate("$._links.self.href", "@endsWith(${unitId})@") //
                .validate("$._links.unit.href", "@endsWith(${unitId})@"));
    }

    @Test
    @CitrusTest
    public void shouldReturnStatusNotFoundWhenUnitDoesNotExist() {
        author("Nimra Inam");
        description("Validate the response when unit id does not exist");

        variable("unitId", "59b7ef3b74e8ef91a9cfb1ef");

        getRequest(UNITS_URL + "${unitId}");

        verifyResponse(HttpStatus.NOT_FOUND);
    }

    @Test
    @CitrusTest
    public void shouldFindUnitByName() throws Exception {
        author("Nimra Inam");
        description("Validate response by finding unit by name");

        variable("unitId", "59df4d4f0fcdf86d872acdfc");
        variable("name", "bit");

        getRequest(UNITS_URL + "?name=bit");

        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/search/find_unit_by_name.json"));
    }

    @Test
    @CitrusTest
    public void shouldFindUnitByDescription() throws Exception {
        author("Nimra Inam");
        description("Validate response by finding unit by description");

        variable("description", "bit Unit");

        getRequest(UNITS_URL + "?description=bit Unit");

        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/search/find_unit_by_description.json"));
    }

    @Test
    @CitrusTest
    public void shouldFindUnitsByBaseUnit() throws Exception {
        author("Nimra Inam");
        description("Validate response by finding unit by base unit");

        variable("baseUnit", true);

        getRequest(UNITS_URL + "?isBaseUnit=true");

        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/search/find_unit_by_base_unit.json"));
    }

    @Test
    @CitrusTest
    public void shouldFindUnitsByGroupId() throws Exception {
        author("Nimra Inam");
        description("Validate response by finding unit by group id");

        variable("groupId", "59df33dc0fcdf86d872acd27");

        getRequest(UNITS_URL + "?groupId=59df33dc0fcdf86d872acd27");

        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/search/find_unit_by_group_id.json"));
    }

    @Test
    @CitrusTest
    public void shouldFindUnitsByMeasuringSystem() throws Exception {
        author("Nimra Inam");
        description("Validate response by finding unit by metric system");

        variable("measuringSystem", "NONE");

        getRequest(UNITS_URL + "?measuringSystem=IMPERIAL");

        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/search/find_unit_by_measuring_system.json"));
    }

    @Test
    @CitrusTest
    public void shouldFindUnitsByFormula() throws Exception {
        author("Nimra Inam");
        description("Validate response by finding unit by formula");

        variable("formula", "[value] * 1000");

        getRequest(UNITS_URL + "?formula=[value] * 1000");

        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/search/find_unit_by_formula.json"));
    }

    @Test
    @CitrusTest
    public void shouldFindByNameDescriptionBaseUnitGroupIdMeasuringSystemAndFormulaDoesNotExist()
            throws Exception {
        author("Nimra Inam");
        description(
                "Validate response by finding unit by name, description, base unit, group id, measuring system and formula");

        variable("name", "bit");
        variable("description", "bit Unit");
        variable("baseUnit", "false");
        variable("groupId", "59df33dc0fcdf86d872acd27");
        variable("metricSystem", "NONE");
        variable("formula", "[value] / 1000");

        getRequest(UNITS_URL
                + "?name=bit&groupId=59df33dc0fcdf86d872acd27&isBaseUnit=false&description=bit Unit&measuringSystem=NONE&formula=[value] / 1000");

        verifyResponse(HttpStatus.OK, //
                readFile(
                        "/datasets/units/search/find_unit_by_name_description_baseUnit_groupId_measuringSystem_formula.json"));
    }

    @Test
    @CitrusTest
    public void shouldRetunNoRecordWhenMatchingNameDoesNotExist() throws Exception {
        author("Nimra Inam");
        description(
                "Validate response having no record while finding a unit by name which does not exist");

        getRequest(UNITS_URL + "?name=UnitNoFound");

        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/search/find_unit_which_does_not_exist.json"));
    }

    @Test
    @CitrusTest
    public void shouldRetunNoRecordWhenMatchingDescriptionDoesNotExist()
            throws Exception {
        author("Nimra Inam");
        description(
                "Validate response having no record while finding a unit by description which does not exist");

        getRequest(UNITS_URL + "?description=UnitNoFound");

        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/search/find_unit_which_does_not_exist.json"));
    }

    @Test
    @CitrusTest
    public void shouldRetunNoRecordWhenMatchingFormulaDoesNotExist() throws Exception {
        author("Nimra Inam");
        description(
                "Validate response having no record while finding a unit by formula which does not exist");

        getRequest(UNITS_URL + "?formula=UnitNoFound");

        verifyResponse(HttpStatus.OK, //
                readFile("/datasets/units/search/find_unit_which_does_not_exist.json"));
    }
}
