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

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.etilize.burraq.unit.test.AbstractIntegrationTest;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;

@UsingDataSet(locations = "/datasets/units/units.json")
public class UnitRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UnitRepository unitRepository;

    @Test
    public void shouldFindAllUnits() {
        final List<Unit> units = unitRepository.findAll();
        assertThat(units, is(notNullValue()));
        assertThat(units, hasSize(2));
        assertThat(units.get(0).getName(), is("Kilogram"));
        assertThat(units.get(0).getGroupId(),
                is(new ObjectId("59b63ec8e110b21a936c9eed")));
        assertThat(units.get(0).isBaseUnit(), is(false));
        assertThat(units.get(0).getFormula(), is("1/1000"));
        assertThat(units.get(0).getMetricSystem(), is(MetricSystem.CGS));
        assertThat(units.get(0).getDescription(), is("Kilogram Unit"));
        assertThat(units.get(1).getName(), is("Degree Celcius"));
        assertThat(units.get(1).getGroupId(),
                is(new ObjectId("59b63ec8e110b21a936c9eef")));
        assertThat(units.get(1).isBaseUnit(), is(true));
        assertThat(units.get(1).getFormula(), is("A*B"));
        assertThat(units.get(1).getMetricSystem(), is(MetricSystem.CGS));
        assertThat(units.get(1).getDescription(), is("Degree Celcius Unit"));
    }

    @Test(expected = DuplicateKeyException.class)
    public void shouldThrowDuplicateKeyExceptionWhenUnitNameAlreadyExists() {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef"),
                false, MetricSystem.CGS, "1/1000", "Kilogram Unit");
        unitRepository.save(unit);
    }

    @Test
    public void shouldFindUnitById() {
        final Unit unit = unitRepository.findOne(
                new ObjectId("53e9155b5ed24e4c38d60e3c"));
        assertThat(unit.getName(), is("Kilogram"));
        assertThat(unit.getGroupId(), is(new ObjectId("59b63ec8e110b21a936c9eed")));
        assertThat(unit.isBaseUnit(), is(false));
        assertThat(unit.getFormula(), is("1/1000"));
        assertThat(unit.getMetricSystem(), is(MetricSystem.CGS));
        assertThat(unit.getDescription(), is("Kilogram Unit"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_delete.json")
    public void shouldDeleteUnitById() {
        unitRepository.delete(new ObjectId("74e9155b5ed24e4c38d60e3c"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create.json")
    public void shouldCreateNewUnit() {
        final Unit unit = new Unit("Pound", new ObjectId("59b63ec8e110b21a936c9eed"), //
                false, MetricSystem.CGS, "ABC", "Pound Unit");
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_update.json")
    public void shouldUpdateExistingUnit() {
        final Unit unit = new Unit("Gram", new ObjectId("59b63ec8e110b21a936c9eed"), //
                true, MetricSystem.CGS, "ABC", "Gram Unit");
        unit.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsNullAtCreation() {
        final Unit unit = new Unit(null, new ObjectId("59b63ec8e110b21a936c9eef"), false,
                MetricSystem.CGS, "1/1000", "Kilogram Unit");
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsNullAtUpdate() {
        final Unit unit = new Unit(null, new ObjectId("59b63ec8e110b21a936c9eef"), false,
                MetricSystem.CGS, "1/1000", "Kilogram Unit");
        ;
        unit.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsEmptyAtCreation() {
        final Unit unit = new Unit("", new ObjectId("59b63ec8e110b21a936c9eef"), false,
                MetricSystem.CGS, "1/1000", "Kilogram Unit");
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsEmptyAtUpdate() {
        final Unit unit = new Unit("", new ObjectId("59b63ec8e110b21a936c9eef"), false,
                MetricSystem.CGS, "1/1000", "Kilogram Unit");
        unit.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenMetricSystemIsNullAtCreation() {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef") //
                , false, null, "1/1000", "Kilogram Unit");
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenMetricSystemIsNullAtUpdate() {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef") //
                , false, null, "1/1000", "Kilogram Unit");
        unit.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsNullAtCreation() {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef"),
                false, MetricSystem.CGS, "1/1000", null);
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsNullAtUpdate() {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef"),
                false, MetricSystem.CGS, "1/1000", null);
        unit.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsEmptylAtCreation() {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef"),
                false, MetricSystem.CGS, "1/1000", "");
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsEmptyAtUpdate() {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef"),
                false, MetricSystem.CGS, "1/1000", "");
        unit.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        unitRepository.save(unit);
    }
}
