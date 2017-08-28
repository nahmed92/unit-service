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

package com.etilize.burraq.unit.group;

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

@UsingDataSet(locations = "/datasets/groups/groups.bson")
public class GroupRepositoryRestIntegrationTest extends AbstractRestIntegrationTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldFindGroupById() throws Exception {
        mockMvc.perform(get("/groups/{id}", new ObjectId("53e9155b5ed24e4c38d60e3c"))) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.name", is("weight"))) //
                .andExpect(jsonPath("$.description", is("This is weight unit")));
    }

    @Test
    public void shouldFindAllGroups() throws Exception {
        mockMvc.perform(get("/groups")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$._embedded.groups[*]", hasSize(2))) //
                .andExpect(jsonPath("$.page.size", is(20))) //
                .andExpect(jsonPath("$.page.totalElements", is(2))) //
                .andExpect(jsonPath("$._embedded.groups[0].name", is("weight"))) //
                .andExpect(jsonPath("$._embedded.groups[0].description", //
                        is("This is weight unit"))) //
                .andExpect(jsonPath("$._embedded.groups[1].name", is("temperature"))) //
                .andExpect(jsonPath("$._embedded.groups[1].description", //
                        is("This is Temperature unit")));

    }

    @ShouldMatchDataSet(location = "/datasets/groups/group_after_create.bson")
    @Test
    public void shouldCreateNewGroup() throws Exception {
        final Group group = new Group("pressure", "This is pressure group");
        mockMvc.perform(post("/groups") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isCreated()) //
                .andExpect(header().string("Location", containsString("/groups")));
    }

    @ShouldMatchDataSet(location = "/datasets/groups/group_after_update.bson")
    @Test
    public void shouldUpdateExistingGroup() throws Exception {
        final Group group = new Group("distance", "This is distance group");
        final String content = mapper.writeValueAsString(group);
        mockMvc.perform(put("/groups/{id}", new ObjectId("53e9155b5ed24e4c38d60e3c")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isNoContent()) //
                .andExpect(header().string("Location",
                        endsWith("/groups/53e9155b5ed24e4c38d60e3c")));
    }

    @Test
    public void shouldReturnStatusConflictWhenGroupNameAlreadyExists() throws Exception {
        final Group group = new Group("weight", "this is weight unit");
        mockMvc.perform(post("/groups") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturnStatusNotFoundWhenGroupDoesnotExist() throws Exception {
        mockMvc.perform(get("/groups/{id}", "53e9155b5ed24e4c38d60e3d")) //
                .andExpect(status().isNotFound()); //
    }

    @Test
    public void shouldReturnStatusBadRequestWhenNameIsNullAtCreation() throws Exception {
        final Group group = new Group(null, "this is weight unit");
        mockMvc.perform(post("/groups") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message", is("name is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenNameIsNullAtUpdate() throws Exception {
        final Group group = new Group(null, "this is weight unit");
        final String content = mapper.writeValueAsString(group);
        mockMvc.perform(put("/groups/{id}", new ObjectId("53e9155b5ed24e4c38d60e3c")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message", is("name is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenNameIsEmptyAtCreation() throws Exception {
        final Group group = new Group("", "this is weight unit");
        mockMvc.perform(post("/groups") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message", is("name is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenNameIsEmptyAtUpdate() throws Exception {
        final Group group = new Group("", "this is weight unit");
        final String content = mapper.writeValueAsString(group);
        mockMvc.perform(put("/groups/{id}", new ObjectId("53e9155b5ed24e4c38d60e3c")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message", is("name is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenDescriptionIsNullAtCreation()
            throws Exception {
        final Group group = new Group("weight", null);
        mockMvc.perform(post("/groups") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message", //
                        is("description is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenDescriptionIsNullAtUpdate()
            throws Exception {
        final Group group = new Group("weight", null);
        final String content = mapper.writeValueAsString(group);
        mockMvc.perform(put("/groups/{id}", new ObjectId("53e9155b5ed24e4c38d60e3c")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message", //
                        is("description is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenDescriptionIsEmptyAtCreation()
            throws Exception {
        final Group group = new Group("weight", "");
        mockMvc.perform(post("/groups") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message", //
                        is("description is required")));
    }

    @Test
    public void shouldReturnStatusBadRequestWhenDescriptionIsEmptyAtUpdate()
            throws Exception {
        final Group group = new Group("weight", "");
        final String content = mapper.writeValueAsString(group);
        mockMvc.perform(put("/groups/{id}", new ObjectId("53e9155b5ed24e4c38d60e3c")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(content)) //
                .andExpect(status().isBadRequest()) //
                .andExpect(jsonPath("$.errors[0].message", //
                        is("description is required")));
    }

    @Test
    public void shouldFindGroupByName() throws Exception {
        mockMvc.perform(get("/groups?name={name}", "weight")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$._embedded.groups[*]", hasSize(1))) //
                .andExpect(jsonPath("$._embedded.groups[*]._links.self.href", //
                        contains(endsWith("/groups/53e9155b5ed24e4c38d60e3c"))))//
                .andExpect(jsonPath("$.page.size", is(20))) //
                .andExpect(jsonPath("$.page.totalElements", is(1))) //
                .andExpect(jsonPath("$._embedded.groups[0].name", is("weight"))) //
                .andExpect(jsonPath("$._embedded.groups[0].description", //
                        is("This is weight unit"))); //

    }

    @Test
    public void shouldFindByNameCaseInSensitively() throws Exception {
        mockMvc.perform(get("/groups?name={name}", "Weight")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$._embedded.groups[*]", hasSize(1))) //
                .andExpect(jsonPath("$._embedded.groups[*]._links.self.href", //
                        contains(endsWith("/groups/53e9155b5ed24e4c38d60e3c"))))//
                .andExpect(jsonPath("$.page.size", is(20))) //
                .andExpect(jsonPath("$.page.totalElements", is(1))) //
                .andExpect(jsonPath("$._embedded.groups[0].name", is("weight"))) //
                .andExpect(jsonPath("$._embedded.groups[0].description", //
                        is("This is weight unit"))); //
    }
}
