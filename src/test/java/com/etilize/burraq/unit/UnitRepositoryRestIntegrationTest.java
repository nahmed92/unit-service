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
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;

@UsingDataSet(locations = "/datasets/units/units.json")
public class UnitRepositoryRestIntegrationTest extends AbstractRestIntegrationTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldFindUnitById() throws Exception {
        mockMvc.perform(get("/units/{id}", new ObjectId("53e9155b5ed24e4c38d60e3c"))) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$._links.self.href", //
                        endsWith("/units/53e9155b5ed24e4c38d60e3c")))//
                .andExpect(jsonPath("$.name", is("Kilogram"))) //
                .andExpect(jsonPath("$.groupId", is("59b63ec8e110b21a936c9eed"))) //
                .andExpect(jsonPath("$.isBaseUnit", is(false))) //
                .andExpect(jsonPath("$.formula", is("1/1000"))) //
                .andExpect(jsonPath("$.metricSystem", is("CGS"))) //
                .andExpect(jsonPath("$.description", is("Kilogram Unit")));
    }

    @Test
    public void shouldFindAllUnits() throws Exception {
        mockMvc.perform(get("/units")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$._embedded.units[*]", hasSize(2))) //
                .andExpect(jsonPath("$.page.size", is(20))) //
                .andExpect(jsonPath("$.page.totalElements", is(2))) //
                .andExpect(jsonPath("$._embedded.units[0]._links.self.href", //
                        endsWith("/units/53e9155b5ed24e4c38d60e3c")))//
                .andExpect(jsonPath("$._embedded.units[0].name", is("Kilogram"))) //
                .andExpect(jsonPath("$._embedded.units[0].groupId",
                        is("59b63ec8e110b21a936c9eed"))) //
                .andExpect(jsonPath("$._embedded.units[0].isBaseUnit", is(false))) //
                .andExpect(jsonPath("$._embedded.units[0].formula", is("1/1000"))) //
                .andExpect(jsonPath("$._embedded.units[0].metricSystem", is("CGS"))) //
                .andExpect(
                        jsonPath("$._embedded.units[0].description", is("Kilogram Unit"))) //
                .andExpect(jsonPath("$._embedded.units[1].name", is("Degree Celcius"))) //
                .andExpect(jsonPath("$._embedded.units[1].groupId",
                        is("59b63ec8e110b21a936c9eef"))) //
                .andExpect(jsonPath("$._embedded.units[1]._links.self.href", //
                        endsWith("/units/74e9155b5ed24e4c38d60e3c")))//
                .andExpect(jsonPath("$._embedded.units[1].isBaseUnit", is(true))) //
                .andExpect(jsonPath("$._embedded.units[1].formula", is("A*B"))) //
                .andExpect(jsonPath("$._embedded.units[1].metricSystem", is("CGS"))) //
                .andExpect(jsonPath("$._embedded.units[1].description",
                        is("Degree Celcius Unit")));

    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create.json")
    public void shouldCreateNewUnit() throws Exception {
        final Unit unit = new Unit("Pound", new ObjectId("59b63ec8e110b21a936c9eed"), //
                false, MetricSystem.CGS, "ABC", "Pound Unit");
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isCreated()) //
                .andExpect(header().string("Location", containsString("/units")));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_update.json")
    public void shouldUpdateExistingUnit() throws Exception {
        final Unit unit = new Unit("Gram", new ObjectId("59b63ec8e110b21a936c9eed"), true,
                MetricSystem.CGS, "ABC", "Gram Unit");
        final String content = mapper.writeValueAsString(unit);
        mockMvc.perform(put("/units/{id}", new ObjectId("53e9155b5ed24e4c38d60e3c")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isNoContent()) //
                .andExpect(header().string("Location",
                        endsWith("/units/53e9155b5ed24e4c38d60e3c")));
    }

    @Test
    public void shouldReturnStatusConflictWhenUnitNameAlreadyExists() throws Exception {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef"),
                false, MetricSystem.CGS, "1/1000", "Kilogram Unit");
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldFindUnitByName() throws Exception {
        mockMvc.perform(get("/units?name={name}", "Kilogram")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$._embedded.units[*]", hasSize(1))) //
                .andExpect(jsonPath("$.page.size", is(20))) //
                .andExpect(jsonPath("$.page.totalElements", is(1))) //
                .andExpect(jsonPath("$._embedded.units[*]._links.self.href", //
                        contains(endsWith("/units/53e9155b5ed24e4c38d60e3c"))))//
                .andExpect(jsonPath("$._embedded.units[0].name", is("Kilogram"))) //
                .andExpect(jsonPath("$._embedded.units[0].groupId",
                        is("59b63ec8e110b21a936c9eed"))) //
                .andExpect(jsonPath("$._embedded.units[0].isBaseUnit", is(false))) //
                .andExpect(jsonPath("$._embedded.units[0].formula", is("1/1000"))) //
                .andExpect(jsonPath("$._embedded.units[0].metricSystem", is("CGS"))) //
                .andExpect(jsonPath("$._embedded.units[0].description",
                        is("Kilogram Unit")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenRequiredFieldsAreEmptyAtCreation()
            throws Exception {
        final Unit unit = new Unit("", null, false, null, "1/1000", "");
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(4))) //
                .andExpect(jsonPath("$.errors[*].message", //
                        containsInAnyOrder("name is required", "groupId is required", //
                                "metricSystem is required", "description is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenFieldsAreEmptyAtUpdate()
            throws Exception {
        final Unit unit = new Unit("", null, false, null, "1/1000", "");
        final String content = mapper.writeValueAsString(unit);
        mockMvc.perform(put("/units/{id}", new ObjectId("53e9155b5ed24e4c38d60e3c")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(4))) //
                .andExpect(jsonPath("$.errors[*].message", //
                        containsInAnyOrder("name is required", "groupId is required", //
                                "metricSystem is required", "description is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenRequiredFieldsAreNullAtCreation()
            throws Exception {
        final Unit unit = new Unit(null, null, false, null, "1/1000", null);
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(4))) //
                .andExpect(jsonPath("$.errors[*].message", //
                        containsInAnyOrder("name is required", "groupId is required", //
                                "metricSystem is required", "description is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenFieldsAreNullAtUpdate() throws Exception {
        final Unit unit = new Unit(null, null, false, null, "1/1000", null);
        final String content = mapper.writeValueAsString(unit);
        mockMvc.perform(put("/units/{id}", new ObjectId("53e9155b5ed24e4c38d60e3c")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors", hasSize(4))) //
                .andExpect(jsonPath("$.errors[*].message", //
                        containsInAnyOrder("name is required", "groupId is required", //
                                "metricSystem is required", "description is required")));
    }

}
