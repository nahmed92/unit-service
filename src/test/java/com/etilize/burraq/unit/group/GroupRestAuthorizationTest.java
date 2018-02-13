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
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;

/**
 * Houses rest integration tests for authentication and authorization
 *
 * @author Nasir Ahmed
 * @version 1.0
 *
 */
@UsingDataSet(locations = { "/datasets/groups/groups.bson" })
public class GroupRestAuthorizationTest extends AbstractRestIntegrationTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldReturnUnAuthorizedStatusOnFindingGroupByIdWithoutAuthorizationHeader()
            throws Exception {
        mockMvc.perform(get("/groups/53e9155b5ed24e4c38d60e3c")) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("unauthorized"))) //
                .andExpect(jsonPath("$.error_description",
                        is("Full authentication is required to access this resource")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnCreatingNewGroupWithoutAuthorizationHeader()
            throws Exception {
        final Group group = new Group("pressure", "This is pressure group");
        mockMvc.perform(post("/groups") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("unauthorized"))) //
                .andExpect(jsonPath("$.error_description",
                        is("Full authentication is required to access this resource")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnDeletingGroupWithOutAuthorizationHeader()
            throws Exception {
        mockMvc.perform(delete("/groups/53e9155b5ed24e4c38d60e3c")) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("unauthorized"))) //
                .andExpect(jsonPath("$.error_description",
                        is("Full authentication is required to access this resource")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnUpdatingExistingGroupsWithoutAuthorizationHeader()
            throws Exception {
        final Group group = new Group("distance", "This is distance group");
        mockMvc.perform(put("/groups/{id}", new ObjectId("53e9155b5ed24e4c38d60e3c")) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("unauthorized"))) //
                .andExpect(jsonPath("$.error_description",
                        is("Full authentication is required to access this resource")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOntFindingGroupsByIdWithInvalidAuthorizationHeader()
            throws Exception {
        mockMvc.perform(get("/groups/53e9155b5ed24e4c38d60e3c") //
                .with(revokedToken)) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("invalid_token"))) //
                .andExpect(jsonPath("$.error_description",
                        containsString("Invalid access token:")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnCreatingNewGroupWithInvalidAuthorizationHeader()
            throws Exception {
        final Group group = new Group("pressure", "This is pressure group");
        mockMvc.perform(post("/groups") //
                .with(revokedToken) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("invalid_token"))) //
                .andExpect(jsonPath("$.error_description",
                        containsString("Invalid access token:")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnDeletingGroupWithInvalidAuthorizationHeader()
            throws Exception {
        mockMvc.perform(delete("/groups/53e9155b5ed24e4c38d60e3c") //
                .with(revokedToken)) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("invalid_token"))) //
                .andExpect(jsonPath("$.error_description",
                        containsString("Invalid access token:")));
    }

    @Test
    public void shouldReturnUnAuthorizedStatusOnUpdatingExistingGroupWithInvalidAuthorizationHeader()
            throws Exception {
        final Group group = new Group("distance", "This is distance group");
        mockMvc.perform(put("/group/53e9155b5ed24e4c38d60e3c") //
                .with(revokedToken) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isUnauthorized()) //
                .andExpect(jsonPath("$.error", is("invalid_token"))) //
                .andExpect(jsonPath("$.error_description",
                        containsString("Invalid access token:")));
    }

    public void shouldReturnStatusForbiddenWhenUnAuthorizedUserCreatesNewGroup()
            throws Exception {
        final Group group = new Group("pressure", "This is pressure group");
        mockMvc.perform(post("/groups") //
                .with(unAuthorizedToken) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnStatusForbiddenWhenUnAuthorizedUserDeletesGroup()
            throws Exception {
        mockMvc.perform(delete("/groups/53e9155b5ed24e4c38d60e3c") //
                .with(unAuthorizedToken)) //
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnStatusForbiddenWhenUnAuthorizedUserUpdatesGroup()
            throws Exception {
        final Group group = new Group("distance", "This is distance group");
        mockMvc.perform(put("/groups/59b78f244daf991ecaafa264") //
                .with(unAuthorizedToken) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(group))) //
                .andExpect(status().isForbidden());
    }
}
