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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.etilize.burraq.unit.test.AbstractRestIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoFlexibleComparisonStrategy;

@UsingDataSet(locations = { "/datasets/units/units.json",
    "/datasets/groups/groups.bson" })
@CustomComparisonStrategy(comparisonStrategy = MongoFlexibleComparisonStrategy.class)
public class UnitRestIntegrationTest extends AbstractRestIntegrationTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldFindUnitById() throws Exception {
        mockMvc.perform(get("/units/{id}", new ObjectId("59b63ec8e110b21a936c9eed"))) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$._links.self.href", //
                        endsWith("/units/59b63ec8e110b21a936c9eed")))//
                .andExpect(jsonPath("$.name", is("Kilogram"))) //
                .andExpect(jsonPath("$.groupId", is("53e9155b5ed24e4c38d60e3c"))) //
                .andExpect(jsonPath("$.fromBaseFormula", is("[value]"))) //
                .andExpect(jsonPath("$.toBaseFormula", is("1/1000"))) //
                .andExpect(jsonPath("$.measuringSystem", is("METRIC"))) //
                .andExpect(jsonPath("$.description", is("Kilogram Unit")));
    }

    @Test
    public void shouldFindAllUnits() throws Exception {
        mockMvc.perform(get("/units")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$._embedded.units[*]", hasSize(4))) //
                .andExpect(jsonPath("$.page.size", is(20))) //
                .andExpect(jsonPath("$.page.totalElements", is(4))) //
                .andExpect(jsonPath("$._embedded.units[0]._links.self.href", //
                        endsWith("/units/59b63ec8e110b21a936c9eed")))//
                .andExpect(jsonPath("$._embedded.units[0].name", is("Kilogram"))) //
                .andExpect(jsonPath("$._embedded.units[0].groupId",
                        is("53e9155b5ed24e4c38d60e3c"))) //
                .andExpect(jsonPath("$._embedded.units[0].toBaseFormula", is("1/1000"))) //
                .andExpect(
                        jsonPath("$._embedded.units[0].fromBaseFormula", is("[value]"))) //
                .andExpect(jsonPath("$._embedded.units[0].measuringSystem", is("METRIC"))) //
                .andExpect(
                        jsonPath("$._embedded.units[0].description", is("Kilogram Unit"))) //
                .andExpect(jsonPath("$._embedded.units[1].name", is("fahrenheit"))) //
                .andExpect(jsonPath("$._embedded.units[1].groupId",
                        is("74e9155b5ed24e4c38d60e3c"))) //
                .andExpect(jsonPath("$._embedded.units[1]._links.self.href", //
                        endsWith("/units/74e9155b5ed24e4c38d60e5e")))//
                .andExpect(jsonPath("$._embedded.units[1].toBaseFormula", is("1/1000"))) //
                .andExpect(
                        jsonPath("$._embedded.units[1].fromBaseFormula", is("[value]"))) //
                .andExpect(jsonPath("$._embedded.units[1].measuringSystem", is("METRIC"))) //
                .andExpect(jsonPath("$._embedded.units[1].description",
                        is("Temperature Unit"))) //
                .andExpect(jsonPath("$._embedded.units[2].name", is("Degree Celcius"))) //
                .andExpect(jsonPath("$._embedded.units[2]._links.self.href", //
                        endsWith("/units/59b63ec8e110b21a936c9eef")))//
                .andExpect(jsonPath("$._embedded.units[2].toBaseFormula",
                        is("[value]*1000"))) //
                .andExpect(
                        jsonPath("$._embedded.units[2].fromBaseFormula", is("[value]"))) //
                .andExpect(jsonPath("$._embedded.units[2].measuringSystem", is("METRIC"))) //
                .andExpect(jsonPath("$._embedded.units[2].description",
                        is("Degree Celcius Unit"))) //
                .andExpect(jsonPath("$._embedded.units[3].name", is("Kelvin"))) //
                .andExpect(jsonPath("$._embedded.units[3].groupId",
                        is("74e9155b5ed24e4c38d60e3c"))) //
                .andExpect(jsonPath("$._embedded.units[3]._links.self.href", //
                        endsWith("/units/59c8da92e110b26284265711")))//
                .andExpect(jsonPath("$._embedded.units[3].toBaseFormula",
                        is("[value]/1000"))) //
                .andExpect(
                        jsonPath("$._embedded.units[3].fromBaseFormula", is("[value]"))) //
                .andExpect(jsonPath("$._embedded.units[3].measuringSystem", is("METRIC"))) //
                .andExpect(jsonPath("$._embedded.units[3].description",
                        is("Temperature Unit")));

    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create.json")
    public void shouldCreateNewUnit() throws Exception {
        final Unit unit = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Pound Unit");
        unit.setToBaseFormula("1/1000");
        unit.setFromBaseFormula("1 * 1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isCreated()) //
                .andExpect(header().string("Location", containsString("/units")));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_update.json")
    public void shouldUpdateExistingUnit() throws Exception {
        final Unit unit = new Unit("Gram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Gram Unit");
        unit.setToBaseFormula("[value]*1000");
        unit.setFromBaseFormula("[value]");
        unit.setMeasuringSystem(MeasuringSystem.IMPERIAL);
        final String content = mapper.writeValueAsString(unit);
        mockMvc.perform(put("/units/{id}", new ObjectId("59b63ec8e110b21a936c9eed")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isNoContent()) //
                .andExpect(header().string("Location",
                        endsWith("/units/59b63ec8e110b21a936c9eed")));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_update_will_not_change_baseUnit_state.json")
    public void shouldUpdateExistingUnitWillNotChangeBaseUnitState() throws Exception {
        final Unit unit = new Unit("celcius", new ObjectId("74e9155b5ed24e4c38d60e3c"),
                "Temperature Unit");
        unit.setToBaseFormula("1*1000");
        unit.setFromBaseFormula("1/1000");
        unit.setMeasuringSystem(MeasuringSystem.IMPERIAL);
        final String content = mapper.writeValueAsString(unit);
        mockMvc.perform(put("/units/{id}", new ObjectId("74e9155b5ed24e4c38d60e5e")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isNoContent()) //
                .andExpect(header().string("Location",
                        endsWith("/units/74e9155b5ed24e4c38d60e5e")));
    }

    @Test
    public void shouldReturnStatusConflictWhenUnitNameAlreadyExists() throws Exception {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Kilogram Unit");
        unit.setToBaseFormula("1/1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isConflict()) //
                .andExpect(jsonPath("$.message", is("name already exists."))) //
                .andExpect(jsonPath("$.code", is(11000)));
    }

    @Test
    public void shouldFindUnitByName() throws Exception {
        mockMvc.perform(get("/units?name={name}", "Kilogram")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$._embedded.units[*]", hasSize(1))) //
                .andExpect(jsonPath("$.page.size", is(20))) //
                .andExpect(jsonPath("$.page.totalElements", is(1))) //
                .andExpect(jsonPath("$._embedded.units[*]._links.self.href", //
                        contains(endsWith("/units/59b63ec8e110b21a936c9eed"))))//
                .andExpect(jsonPath("$._embedded.units[0].name", is("Kilogram"))) //
                .andExpect(jsonPath("$._embedded.units[0].groupId",
                        is("53e9155b5ed24e4c38d60e3c"))) //
                .andExpect(
                        jsonPath("$._embedded.units[0].fromBaseFormula", is("[value]"))) //
                .andExpect(jsonPath("$._embedded.units[0].toBaseFormula", is("1/1000"))) //
                .andExpect(jsonPath("$._embedded.units[0].measuringSystem", is("METRIC"))) //
                .andExpect(jsonPath("$._embedded.units[0].description",
                        is("Kilogram Unit")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenRequiredFieldsAreEmptyAtCreation()
            throws Exception {
        final Unit unit = new Unit("", new ObjectId("53e9155b5ed24e4c38d60e3c"), "");
        unit.setToBaseFormula("");
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(2))) //
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "name is required", "description is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenFieldsAreEmptyAtUpdate()
            throws Exception {
        final Unit unit = new Unit("", new ObjectId("53e9155b5ed24e4c38d60e3c"), "");
        unit.setToBaseFormula("");
        final String content = mapper.writeValueAsString(unit);
        mockMvc.perform(put("/units/{id}", new ObjectId("59b63ec8e110b21a936c9eed")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(2))) //
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "name is required", "description is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenRequiredFieldsAreNullAtCreation()
            throws Exception {
        final Unit unit = new Unit(null, null, null);
        unit.setToBaseFormula(null);
        unit.setMeasuringSystem(null);
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(3))) //
                .andExpect(
                        jsonPath("$.errors[*].message",
                                containsInAnyOrder("name is required",
                                        "groupId is required",
                                        "description is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenNameGroupIdAndDescriptionAreNullAtUpdate()
            throws Exception {
        final Unit unit = new Unit(null, null, null);
        unit.setToBaseFormula(null);
        unit.setMeasuringSystem(null);
        final String content = mapper.writeValueAsString(unit);
        mockMvc.perform(put("/units/{id}", new ObjectId("59b63ec8e110b21a936c9eed")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(3))) //
                .andExpect(
                        jsonPath("$.errors[*].message",
                                containsInAnyOrder("name is required",
                                        "groupId is required",
                                        "description is required")));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create_with_defaultvalues.json")
    public void shouldCreateNewUnitWithDefaultValues() throws Exception {
        final Unit unit = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Pound Unit");
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isCreated()) //
                .andExpect(header().string("Location", containsString("/units")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenGroupDoesNotExistAtCreate()
            throws Exception {
        final Unit centigradeUnit = new Unit("Centigrade",
                new ObjectId("74e9155b5ed24e4c38d60e3e"), "temperature Unit");
        centigradeUnit.setBaseUnit(true);
        final String content = mapper.writeValueAsString(centigradeUnit);
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(1))) //
                .andExpect(jsonPath("$.errors[0].message", is("Group does not exist.")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenGroupDoesNotExistAtUpdate()
            throws Exception {
        final Unit kevlinUnit = new Unit("Kelvin",
                new ObjectId("74e9155b5ed24e4c38d60e3a"), "Temperature Unit");
        kevlinUnit.setBaseUnit(true);
        final String content = mapper.writeValueAsString(kevlinUnit);
        mockMvc.perform(put("/units/{id}", new ObjectId("59c8da92e110b26284265711")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(1))) //
                .andExpect(jsonPath("$.errors[0].message", is("Group does not exist.")));
    }

    @Test
    public void shouldReturnBadRequestWhenInvalidArithmeticOperatorsInFormula()
            throws Exception {
        Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Kilogram Unit");
        unit.setToBaseFormula("[value]//(10000++23)");
        // Invalid division operator
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message", is("Formula is not valid.")));

    }

    @Test
    public void shouldReturnBadRequestWhenTextExistInFormula() throws Exception {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Kilogram Unit");
        unit.setToBaseFormula("[value]/10000ABC");
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message", is("Formula is not valid.")));
    }

    @Test
    public void shouldThrowBadRequestWhenMeasuringSystemIsEmpty() throws Exception {
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content("{" + "\"name\":\"Kilogram\","
                        + "\"groupId\": \"53e9155b5ed24e4c38d60e3c\","
                        + "\"description\": \"Kilogram Unit " + "\"measuringSystem\":\""
                        + "}")) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.message",
                        startsWith(
                                "JSON parse error: Unexpected character ('m' (code 109)): was expecting "
                                        + "comma to separate Object entries;")));

    }

    @Test
    public void shouldThrowBadRequestWhenGroupIdIsEmpty() throws Exception {
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content("{" + "\"name\":\"Kilogram\"," + "\"groupId\": \"\","
                        + "\"description\": \"Kilogram Unit " + "}")) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.message", startsWith(
                        "JSON parse error: Can not construct instance of org.bson.types.ObjectId, "
                                + "problem: invalid hexadecimal representation of an ObjectId: []; nested exception "
                                + "is com.fasterxml.jackson.databind.JsonMappingException: Can not construct instance of "
                                + "org.bson.types.ObjectId, problem: invalid hexadecimal representation of an ObjectId:")));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create_set_isBaseUnit_true_also_update_group_baseUnitId_when_it_is_null.json")
    public void shouldUpdateBaseUnitIdOfAssociatedGroupWhenItIsNullAtUnitCreation()
            throws Exception {
        final Unit unit = new Unit("Gram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Gram Unit");
        unit.setToBaseFormula("1/1000");
        unit.setFromBaseFormula("1 * 1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isCreated()) //
                .andExpect(header().string("Location", containsString("/units")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenGroupIdUpdate() throws Exception {
        final Unit kevlinUnit = new Unit("Kelvin",
                new ObjectId("53e9155b5ed24e4c38d60e3c"), "Temperature Unit");
        kevlinUnit.setBaseUnit(true);
        final String content = mapper.writeValueAsString(kevlinUnit);
        mockMvc.perform(put("/units/{id}", new ObjectId("59c8da92e110b26284265711")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(1))) //
                .andExpect(jsonPath("$.errors[0].message", //
                        is("update of groupId isn't allowed.")));
    }

    @Test
    public void shouldReturnBadRequestWhenBaseUnitIsDeleted() throws Exception {
        final Unit unit = new Unit("fahrenheit", new ObjectId("74e9155b5ed24e4c38d60e3c"),
                "Temperature Unit");
        unit.setBaseUnit(true);
        final String content = mapper.writeValueAsString(unit);
        mockMvc.perform(delete("/units/{id}", new ObjectId("74e9155b5ed24e4c38d60e5e")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(1))) //
                .andExpect(jsonPath("$.errors[0].message", //
                        is("BaseUnit associated with group, cannot be deleted.")));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_delete.json")
    public void shouldDeleteUnitWhenUnitIsNotBaseUnit() throws Exception {
        final Unit unit = new Unit("Degree Celcius",
                new ObjectId("74e9155b5ed24e4c38d60e3c"), "Temperature Unit");
        unit.setBaseUnit(false);
        final String content = mapper.writeValueAsString(unit);
        mockMvc.perform(delete("/units/{id}", new ObjectId("59b63ec8e110b21a936c9eef")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isNoContent());
    }

}
