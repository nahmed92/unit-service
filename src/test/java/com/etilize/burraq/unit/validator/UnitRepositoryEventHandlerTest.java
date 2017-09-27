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

package com.etilize.burraq.unit.validator;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;

import com.etilize.burraq.unit.MetricSystem;
import com.etilize.burraq.unit.Unit;
import com.etilize.burraq.unit.test.AbstractIntegrationTest;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
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
                new ObjectId("74e9155b5ed24e4c38d60e3e"), //
                true, "temperature Unit");
        unitEventHandler.handleBeforeUnitCreate(centigradeUnit);
    }

    @Test(expected = RepositoryConstraintViolationException.class)
    public void shouldThrowExceptionWhenGroupDoesNotExistAtUpdate() {
        final Unit kevlinUnit = new Unit("Kelvin",
                new ObjectId("74e9155b5ed24e4c38d60e3a"), //
                true, "Temperature Unit");
        unitEventHandler.handleBeforeUnitUpdate(kevlinUnit);
    }

    @Test(expected = RepositoryConstraintViolationException.class)
    public void shouldThrowExceptionWhenGroupContainMoreThenTwoBaseUnitAtCreate() {
        final Unit centigradeUnit = new Unit("Centigrade",
                new ObjectId("74e9155b5ed24e4c38d60e3c"), //
                true, "temperature Unit");
        unitEventHandler.handleBeforeUnitCreate(centigradeUnit);
    }

    @Test(expected = RepositoryConstraintViolationException.class)
    public void shouldThrowExceptionWhenGroupContainMoreThenTwoBaseUnitAtUpdate() {
        final Unit kevlinUnit = new Unit("Kelvin",
                new ObjectId("74e9155b5ed24e4c38d60e3c"), //
                true, "Temperature Unit");
        kevlinUnit.setId(new ObjectId("59c8da92e110b26284265711"));
        unitEventHandler.handleBeforeUnitUpdate(kevlinUnit);
    }

    @Test
    public void shouldNotThrowExceptionAtCreationWhenGroupIsValidAndTotalNumberOfBaseUnitsIsNotMoreThanMaxBaseUnits() {
        final Unit unit = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"), //
                false, "Pound Unit");
        unitEventHandler.handleBeforeUnitCreate(unit);
    }

    @Test
    public void shouldNotThrowExceptionAtUpdateWhenGroupIsValidAndTotalNumberOfBaseUnitsIsNotMoreThanMaxBaseUnits() {
        final Unit unit = new Unit("Gram", new ObjectId("53e9155b5ed24e4c38d60e3c"), //
                true, "Gram Unit");
        unit.setFormula("ABC");
        unit.setMetricSystem(MetricSystem.CGS);
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitEventHandler.handleBeforeUnitUpdate(unit);
    }
}
