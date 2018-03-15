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

import com.etilize.burraq.unit.MeasuringSystem;
import com.etilize.burraq.unit.Unit;
import com.etilize.burraq.unit.test.AbstractIntegrationTest;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoFlexibleComparisonStrategy;

@UsingDataSet(locations = { "/datasets/units/units.json",
    "/datasets/groups/groups.bson" })
@CustomComparisonStrategy(comparisonStrategy = MongoFlexibleComparisonStrategy.class)

public class UnitRepositoryEventHandlerTest extends AbstractIntegrationTest {

    @Autowired
    private UnitRepositoryEventHandler unitEventHandler;

    @Test(expected = RepositoryConstraintViolationException.class)
    public void shouldThrowExceptionWhenGroupDoesNotExistAtCreate() {
        final Unit centigradeUnit = new Unit("Centigrade",
                new ObjectId("74e9155b5ed24e4c38d60e3e"), "temperature Unit");
        centigradeUnit.setBaseUnit(true);
        unitEventHandler.handleBeforeUnitCreate(centigradeUnit);
    }

    @Test(expected = RepositoryConstraintViolationException.class)
    public void shouldThrowExceptionWhenGroupDoesNotExistAtUpdate() {
        final Unit kevlinUnit = new Unit("Kelvin",
                new ObjectId("74e9155b5ed24e4c38d60e3a"), "Temperature Unit");
        kevlinUnit.setBaseUnit(true);
        unitEventHandler.handleBeforeUnitCreate(kevlinUnit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create_also_update_group_baseUnitId_when_it_is_null.json")
    public void shouldUpdateBaseUnitIdOfAssociatedGroupWhenItIsNullAtUnitCreation() {
        final Unit unit = new Unit("Gram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Gram Unit");
        unit.setId(new ObjectId("59c8da92e110b26284265711"));
        unit.setToBaseFormula("1/1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        unitEventHandler.handleAfterUnitCreate(unit);
    }

    @Test(expected = RepositoryConstraintViolationException.class)
    public void shouldThrowExceptionWhenGroupIdUpdate() {
        final Unit unit = new Unit("Kelvin", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Temperature Unit");
        unit.setId(new ObjectId("59c8da92e110b26284265711"));
        unit.setBaseUnit(true);
        unitEventHandler.handleBeforeUnitSave(unit);
    }
}
