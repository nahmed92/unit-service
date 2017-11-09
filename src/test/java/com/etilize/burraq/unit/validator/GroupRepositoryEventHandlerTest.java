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

package com.etilize.burraq.unit.validator;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;

import com.etilize.burraq.unit.group.Group;
import com.etilize.burraq.unit.test.AbstractIntegrationTest;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoFlexibleComparisonStrategy;

@UsingDataSet(locations = { "/datasets/units/units_update_baseUnit.json",
    "/datasets/groups/groups.bson" })
@CustomComparisonStrategy(comparisonStrategy = MongoFlexibleComparisonStrategy.class)
public class GroupRepositoryEventHandlerTest extends AbstractIntegrationTest {

    @Autowired
    private GroupRepositoryEventHandler groupEventHandler;

    @Test
    @ShouldMatchDataSet(location = "/datasets/groups/group_after_update_baseUnitId_reset_isBaseUnit_flag_of_unit.json")
    public void shouldUpdateUnitIsBaseUnitToTrueAndFalseTopreviousUnitIsBaseUnitWhenGroupBaseUnitIdUpdated() {
        final Group group = new Group("temperature", "Temperature unit");
        group.setBaseUnitId(new ObjectId("59c8da92e110b26284265711"));
        group.setId(new ObjectId("74e9155b5ed24e4c38d60e3c"));
        groupEventHandler.handleBeforeSave(group);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/groups/group_after_update_baseUnitId_when_null_also_reset_unit_isBaseUnit_flag_true.json")
    public void shouldUpdateUnitIsBaseUnitToTrueWhenGroupHavingBaseUnitIDNullBeforeUpdate() {
        final Group group = new Group("Weight", "Weight Unit");
        group.setBaseUnitId(new ObjectId("59b63ec8e110b21a936c9eed"));
        group.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        groupEventHandler.handleBeforeSave(group);
    }

    @Test(expected = RepositoryConstraintViolationException.class)
    public void shouldThrowExceptionWhenBaseUnitIdIsEnteredAtGroupCreation() {
        final Group group = new Group("Sound", "Sound Unit");
        group.setBaseUnitId(new ObjectId("59b63ec8e110b21a936c9eed"));
        groupEventHandler.handleBeforeCreate(group);
    }

    @Test(expected = RepositoryConstraintViolationException.class)
    public void shouldThrowExceptionWhenBaseUnitIdIsUpdatedToNullAtGroupUpdate() {
        final Group group = new Group("temperature", "temperature Unit");
        group.setBaseUnitId(null);
        group.setId(new ObjectId("74e9155b5ed24e4c38d60e3c"));
        groupEventHandler.handleBeforeSave(group);
    }

    @Test(expected = RepositoryConstraintViolationException.class)
    public void shouldThrowExceptionWhenBaseUnitDoesNotExistAtGroupUpdate() {
        final Group group = new Group("temperature", "temperature Unit");
        group.setBaseUnitId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        group.setId(new ObjectId("74e9155b5ed24e4c38d60e3c"));
        groupEventHandler.handleBeforeSave(group);
    }
}
