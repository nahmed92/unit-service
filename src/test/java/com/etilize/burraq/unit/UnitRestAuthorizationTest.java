/*
 * #region
 * unit-service
 * %%
 * Copyright (C) 2017 - 2018 Etilize
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
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;

@UsingDataSet(locations = { "/datasets/units/units.json" })
public class UnitRestAuthorizationTest extends AbstractRestIntegrationTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldReturnUnAuthorizedStatusOnFindingUnitByIdWithoutAuthorizationHeader()
            throws Exception {
        mockMvc.perform(get("/units/{id}", new ObjectId("59b63ec8e110b21a936c9eed"))) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("unauthorized"))) //
                .andExpect(jsonPath("$.error_description",
                        is("Full authentication is required to access this resource")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnCreatingNewUnitWithoutAuthorizationHeader()
            throws Exception {
        final Unit unit = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Pound Unit");
        unit.setFormula("1/1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        mockMvc.perform(post("/units") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("unauthorized"))) //
                .andExpect(jsonPath("$.error_description",
                        is("Full authentication is required to access this resource")));

    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnUpdatingExistingUnitWithoutAuthorizationHeader()
            throws Exception {
        final Unit unit = new Unit("Gram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Gram Unit");
        unit.setFormula("[value]*1000");
        unit.setMeasuringSystem(MeasuringSystem.IMPERIAL);
        final String content = mapper.writeValueAsString(unit);
        mockMvc.perform(put("/units/{id}", new ObjectId("59b63ec8e110b21a936c9eed")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("unauthorized"))) //
                .andExpect(jsonPath("$.error_description",
                        is("Full authentication is required to access this resource")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnDeletingUnitWithoutAuthorizationHeader()
            throws Exception {
        mockMvc.perform(delete("/units/53e9155b5ed24e4c38d60e3c")) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("unauthorized"))) //
                .andExpect(jsonPath("$.error_description",
                        is("Full authentication is required to access this resource")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOntFindingUnitByIdWithInvalidAuthorizationHeader()
            throws Exception {
        mockMvc.perform(get("/units/{id}", new ObjectId("59b63ec8e110b21a936c9eed")) //
                .with(revokedToken)) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("invalid_token"))) //
                .andExpect(jsonPath("$.error_description",
                        containsString("Invalid access token:")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnCreatingNewUnitWithInvalidAuthorizationHeader()
            throws Exception {
        final Unit unit = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Pound Unit");
        unit.setFormula("1/1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        mockMvc.perform(post("/units") //
                .with(revokedToken) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("invalid_token"))) //
                .andExpect(jsonPath("$.error_description",
                        containsString("Invalid access token:")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnUpdatingExistingUnitWithInvalidAuthorizationHeader()
            throws Exception {
        final Unit unit = new Unit("Gram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Gram Unit");
        unit.setFormula("[value]*1000");
        unit.setMeasuringSystem(MeasuringSystem.IMPERIAL);
        mockMvc.perform(put("/units/{id}", new ObjectId("59b63ec8e110b21a936c9eed")) //
                .with(revokedToken) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("invalid_token"))) //
                .andExpect(jsonPath("$.error_description",
                        containsString("Invalid access token:")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnDeletingUnitWithInvalidAuthorizationHeader()
            throws Exception {
        mockMvc.perform(delete("/units/53e9155b5ed24e4c38d60e3c") //
                .with(revokedToken)) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("invalid_token"))) //
                .andExpect(jsonPath("$.error_description",
                        containsString("Invalid access token:")));
    }

    public void shouldReturnStatusForbiddenWhenUnAuthorizedUserCreatesNewUnit()
            throws Exception {
        final Unit unit = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Pound Unit");
        unit.setFormula("1/1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);

        mockMvc.perform(post("/units") //
                .with(unAuthorizedToken) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(unit))) //
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnStatusForbiddenWhenUnAuthorizedUserUpdatesUnit()
            throws Exception {
        final Unit unit = new Unit("Gram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Gram Unit");
        unit.setFormula("[value]*1000");
        unit.setMeasuringSystem(MeasuringSystem.IMPERIAL);
        final String content = mapper.writeValueAsString(unit);
        mockMvc.perform(put("/units/{id}", new ObjectId("59b63ec8e110b21a936c9eed")) //
                .with(unAuthorizedToken) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(content))) //
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnStatusForbiddenWhenUnAuthorizedUserDeletesGroup()
            throws Exception {
        mockMvc.perform(delete("/units/53e9155b5ed24e4c38d60e3c") //
                .with(unAuthorizedToken)) //
                .andExpect(status().isForbidden());
    }
}
