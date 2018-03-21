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

package com.etilize.burraq.unit.convert;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.etilize.burraq.unit.test.AbstractRestIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;

@UsingDataSet(locations = { "/datasets/units/units_and_groups.json" })
public class ConvertRestIntegrationTest extends AbstractRestIntegrationTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldConvertSourceUnitIntoTargetUnits() throws Exception {
        final Source source = new Source("gram", new Double(2000));
        final Payload payload = new Payload(source, Sets.newHashSet("Kilogram", "Pound"));
        final String content = mapper.writeValueAsString(payload);
        mockMvc.perform(post("/convert") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(jsonPath("$.result[*]", hasSize(2))) //
                .andExpect(jsonPath("$.result[0].unit", is("Kilogram"))) //
                .andExpect(jsonPath("$.result[0].value", is(2.0))) //
                .andExpect(jsonPath("$.result[1].unit", is("Pound"))) //
                .andExpect(jsonPath("$.result[1].value", is(4.410)));
    }

    @Test
    public void shouldReturnBadRequestWhenSourceIsNull() throws Exception {
        final Payload payload = new Payload(null, Sets.newHashSet("Kilogram", "Pound"));
        final String content = mapper.writeValueAsString(payload);
        mockMvc.perform(post("/convert") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message", is("source cannot be null.")));
    }

    @Test
    public void shouldReturnBadRequestWhenTargetIsEmpty() throws Exception {
        final Source source = new Source("gram", new Double(2000));
        final Payload payload = new Payload(source, Sets.newHashSet());
        final String content = mapper.writeValueAsString(payload);
        mockMvc.perform(post("/convert") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(
                        jsonPath("$.errors[0].message", is("target cannot be empty.")));
    }

    @Test
    public void shouldReturnBadRequestWhenSourceUnitIsNull() throws Exception {
        final Source source = new Source(null, new Double(2000));
        final Payload payload = new Payload(source, Sets.newHashSet("Kilogram", "Pound"));
        final String content = mapper.writeValueAsString(payload);
        mockMvc.perform(post("/convert") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message",
                        is("source unit cannot be null or empty.")));
    }

    @Test
    public void shouldReturnBadRequestWhenSourceValueIsNull() throws Exception {
        final Source source = new Source("gram", null);
        final Payload payload = new Payload(source, Sets.newHashSet("Kilogram", "Pound"));
        final String content = mapper.writeValueAsString(payload);
        mockMvc.perform(post("/convert") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message",
                        is("source value cannot be null.")));
    }

    @Test
    public void shouldReturnUnprocessableEntityWhenSourceUnitNotExist() throws Exception {
        final Source source = new Source("gram_not_exist", new Double(2000));
        final Payload payload = new Payload(source, Sets.newHashSet("Kilogram", "Pound"));
        final String content = mapper.writeValueAsString(payload);
        mockMvc.perform(post("/convert") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isUnprocessableEntity()) //
                .andExpect(jsonPath("$.message",
                        is("Source unit [gram_not_exist] does not exist.")));
    }

    @Test
    public void shouldReturnUnprocessableEntityWhenAnyTargetUnitNotExist()
            throws Exception {
        final Source source = new Source("gram", new Double(2000));
        final Payload payload = new Payload(source, Sets.newHashSet("Kilogram", "Km/h"));
        final String content = mapper.writeValueAsString(payload);
        mockMvc.perform(post("/convert") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isUnprocessableEntity()) //
                .andExpect(
                        jsonPath("$.message", is("Target unit [Km/h] does not exist.")));
    }

    @Test
    public void shouldReturnUnprocessableEntityRequestWhenAnyTargetUnitIsNotInSourceUnitGroup()
            throws Exception {
        final Source source = new Source("gram", new Double(2000));
        final Payload payload = new Payload(source, Sets.newHashSet("Kilogram", "Km"));
        final String content = mapper.writeValueAsString(payload);
        mockMvc.perform(post("/convert") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isUnprocessableEntity()) //
                .andExpect(jsonPath("$.message",
                        is("Target unit [Km] not found in source unit group.")));
    }

    @Test
    public void shouldReturnBadRequestWhenAnyFormulaCalulationFail() throws Exception {
        final Source source = new Source("gram", new Double(2000));
        final Payload payload = new Payload(source, Sets.newHashSet("Kilogram", "tons"));
        final String content = mapper.writeValueAsString(payload);
        mockMvc.perform(post("/convert") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.message", is("Division by zero!")));
    }
}
