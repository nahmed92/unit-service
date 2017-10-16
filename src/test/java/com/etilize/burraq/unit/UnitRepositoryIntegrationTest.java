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
import javax.validation.ValidationException;

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
        assertThat(units, hasSize(4));
        assertThat(units.get(0).getName(), is("Kilogram"));
        assertThat(units.get(0).getGroupId(),
                is(new ObjectId("53e9155b5ed24e4c38d60e3c")));
        assertThat(units.get(0).isBaseUnit(), is(false));
        assertThat(units.get(0).getFormula(), is("1/1000"));
        assertThat(units.get(0).getMetricSystem(), is(MetricSystem.CGS));
        assertThat(units.get(0).getDescription(), is("Kilogram Unit"));
        assertThat(units.get(1).getName(), is("fahrenheit"));
        assertThat(units.get(1).getGroupId(),
                is(new ObjectId("74e9155b5ed24e4c38d60e3c")));
        assertThat(units.get(1).isBaseUnit(), is(true));
        assertThat(units.get(1).getFormula(), is("1/1000"));
        assertThat(units.get(1).getMetricSystem(), is(MetricSystem.CGS));
        assertThat(units.get(1).getDescription(), is("Temperature Unit"));
        assertThat(units.get(2).getName(), is("Degree Celcius"));
        assertThat(units.get(2).getGroupId(),
                is(new ObjectId("74e9155b5ed24e4c38d60e3c")));
        assertThat(units.get(2).isBaseUnit(), is(true));
        assertThat(units.get(2).getFormula(), is("[value]*1000"));
        assertThat(units.get(2).getMetricSystem(), is(MetricSystem.CGS));
        assertThat(units.get(2).getDescription(), is("Degree Celcius Unit"));
        assertThat(units.get(3).getName(), is("Kelvin"));
        assertThat(units.get(3).getGroupId(),
                is(new ObjectId("74e9155b5ed24e4c38d60e3c")));
        assertThat(units.get(3).isBaseUnit(), is(false));
        assertThat(units.get(3).getFormula(), is("[value]/1000"));
        assertThat(units.get(3).getMetricSystem(), is(MetricSystem.CGS));
        assertThat(units.get(3).getDescription(), is("Temperature Unit"));
    }

    @Test(expected = DuplicateKeyException.class)
    public void shouldThrowDuplicateKeyExceptionWhenUnitNameAlreadyExists() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                false, "Kilogram Unit");
        unit.setFormula("1/1000");
        unit.setMetricSystem(MetricSystem.CGS);
        unitRepository.save(unit);
    }

    @Test
    public void shouldFindUnitById() {
        final Unit unit = unitRepository.findOne(
                new ObjectId("59b63ec8e110b21a936c9eed"));
        assertThat(unit.getName(), is("Kilogram"));
        assertThat(unit.getGroupId(), is(new ObjectId("53e9155b5ed24e4c38d60e3c")));
        assertThat(unit.isBaseUnit(), is(false));
        assertThat(unit.getFormula(), is("1/1000"));
        assertThat(unit.getMetricSystem(), is(MetricSystem.CGS));
        assertThat(unit.getDescription(), is("Kilogram Unit"));
    }

    @Test
    public void shouldFindCountByGroupIdAndIsBaseUnit() {
        final int unitCount = unitRepository.countByGroupIdAndIsBaseUnit(
                new ObjectId("74e9155b5ed24e4c38d60e3c"), true);
        assertThat(unitCount, is(2));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_delete.json")
    public void shouldDeleteUnitById() {
        unitRepository.delete(new ObjectId("59b63ec8e110b21a936c9eef"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create.json")
    public void shouldCreateNewUnit() {
        final Unit unit = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"), //
                false, "Pound Unit");
        unit.setMetricSystem(MetricSystem.CGS);
        unit.setFormula("1/1000");
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_update.json")
    public void shouldUpdateExistingUnit() {
        final Unit unit = new Unit("Gram", new ObjectId("53e9155b5ed24e4c38d60e3c"), //
                true, "Gram Unit");
        unit.setMetricSystem(MetricSystem.CGS);
        unit.setFormula("[value]*1000");
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsNullAtCreation() {
        final Unit unit = new Unit(null, new ObjectId("53e9155b5ed24e4c38d60e3c"), false,
                "Kilogram Unit");
        unit.setMetricSystem(MetricSystem.CGS);
        unit.setFormula("1/1000");
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsNullAtUpdate() {
        final Unit unit = new Unit(null, new ObjectId("53e9155b5ed24e4c38d60e3c"), false,
                "Kilogram Unit");
        unit.setMetricSystem(MetricSystem.CGS);
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsEmptyAtCreation() {
        final Unit unit = new Unit("", new ObjectId("53e9155b5ed24e4c38d60e3c"), false,
                "Kilogram Unit");
        unit.setFormula("1/1000");
        unit.setMetricSystem(MetricSystem.CGS);
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsEmptyAtUpdate() {
        final Unit unit = new Unit("", new ObjectId("53e9155b5ed24e4c38d60e3c"), false,
                "Kilogram Unit");
        unit.setFormula("1/1000");
        unit.setMetricSystem(MetricSystem.CGS);
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsNullAtCreation() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                false, null);
        unit.setMetricSystem(MetricSystem.CGS);
        unit.setFormula("1/1000");
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsNullAtUpdate() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                false, null);
        unit.setMetricSystem(MetricSystem.CGS);
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsEmptyAtCreation() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                false, "");
        unit.setFormula("1/1000");
        unit.setMetricSystem(MetricSystem.CGS);
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsEmptyAtUpdate() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                false, "");
        unit.setFormula("1/1000");
        unit.setMetricSystem(MetricSystem.CGS);
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenInvalidArithmeticOperatorsExistInFormula() {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef"),
                false, "Kilogram Unit");
        unit.setFormula("[value]//1000");
        unitRepository.save(unit);
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionWhenInvalidTextInFormula() {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef"),
                false, "Kilogram Unit");
        unit.setFormula("[value]/1000ABC");
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create_with_defaultvalues.json")
    public void shouldCreateNewUnitWithDefaultValues() {
        final Unit unit = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"), //
                false, "Pound Unit");
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create_when_formula_and_metricSystem_is_null_or_empty.json")
    public void shouldCreateUnitWithDefaultValueWhenMetircSystemAndFormulaAreNull() {
        final Unit unit = new Unit("Ounce", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                false, "Ounce Unit");
        unit.setFormula(null);
        unit.setMetricSystem(null);
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create_when_formula_and_metricSystem_is_null_or_empty.json")
    public void shouldCreateUnitFormulaWithDefaultValueWhenFormulaIsEmpty() {
        final Unit unit = new Unit("Ounce", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                false, "Ounce Unit");
        unit.setFormula("");
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_update_when_formula_and_metricSystem_is_null_or_empty.json")
    public void shouldUpdateUnitFormulaAndMetricSystemWithDefaultValueWhenMetricSystemAndFormulaAreNull() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"), //
                false, "Kilogram Unit");
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unit.setFormula(null);
        unit.setMetricSystem(null);
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_update_when_formula_and_metricSystem_is_null_or_empty.json")
    public void shouldUpdateUnitFormulaWithDefaultValueWhenFormulaIsEmpty() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"), //
                false, "Kilogram Unit");
        unit.setFormula("");
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

}
