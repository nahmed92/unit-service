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
        assertThat(units.get(0).getToBaseFormula(), is("1/1000"));
        assertThat(units.get(0).getFromBaseFormula(), is("[value]"));
        assertThat(units.get(0).getMeasuringSystem(), is(MeasuringSystem.METRIC));
        assertThat(units.get(0).getDescription(), is("Kilogram Unit"));
        assertThat(units.get(1).getName(), is("fahrenheit"));
        assertThat(units.get(1).getGroupId(),
                is(new ObjectId("74e9155b5ed24e4c38d60e3c")));
        assertThat(units.get(1).getToBaseFormula(), is("1/1000"));
        assertThat(units.get(1).getFromBaseFormula(), is("[value]"));
        assertThat(units.get(1).getMeasuringSystem(), is(MeasuringSystem.METRIC));
        assertThat(units.get(1).getDescription(), is("Temperature Unit"));
        assertThat(units.get(2).getName(), is("Degree Celcius"));
        assertThat(units.get(2).getGroupId(),
                is(new ObjectId("74e9155b5ed24e4c38d60e3c")));
        assertThat(units.get(2).getToBaseFormula(), is("[value]*1000"));
        assertThat(units.get(2).getFromBaseFormula(), is("[value]"));
        assertThat(units.get(2).getMeasuringSystem(), is(MeasuringSystem.METRIC));
        assertThat(units.get(2).getDescription(), is("Degree Celcius Unit"));
        assertThat(units.get(3).getName(), is("Kelvin"));
        assertThat(units.get(3).getGroupId(),
                is(new ObjectId("74e9155b5ed24e4c38d60e3c")));
        assertThat(units.get(3).getToBaseFormula(), is("[value]/1000"));
        assertThat(units.get(3).getFromBaseFormula(), is("[value]"));
        assertThat(units.get(3).getMeasuringSystem(), is(MeasuringSystem.METRIC));
        assertThat(units.get(3).getDescription(), is("Temperature Unit"));
    }

    @Test(expected = DuplicateKeyException.class)
    public void shouldThrowDuplicateKeyExceptionWhenUnitNameAlreadyExists() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Kilogram Unit");
        unit.setToBaseFormula("1/1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        unitRepository.save(unit);
    }

    @Test
    public void shouldFindUnitById() {
        final Unit unit = unitRepository.findOne(
                new ObjectId("59b63ec8e110b21a936c9eed"));
        assertThat(unit.getName(), is("Kilogram"));
        assertThat(unit.getGroupId(), is(new ObjectId("53e9155b5ed24e4c38d60e3c")));
        assertThat(unit.getToBaseFormula(), is("1/1000"));
        assertThat(unit.getFromBaseFormula(), is("[value]"));
        assertThat(unit.getMeasuringSystem(), is(MeasuringSystem.METRIC));
        assertThat(unit.getDescription(), is("Kilogram Unit"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_delete.json")
    public void shouldDeleteUnit() {
        final Unit unit = new Unit("Degree Celcius",
                new ObjectId("74e9155b5ed24e4c38d60e3c"), "Temperature Unit");
        unit.setBaseUnit(false);
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eef"));
        unitRepository.delete(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_delete.json")
    public void shouldDeleteUnitById() {
        unitRepository.delete(new ObjectId("59b63ec8e110b21a936c9eef"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_delete_baseunit.json")
    public void shouldDeleteBaseUnitByID() {
        unitRepository.delete(new ObjectId("74e9155b5ed24e4c38d60e5e"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create.json")
    public void shouldCreateNewUnit() {
        final Unit unit = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Pound Unit");
        unit.setBaseUnit(true);
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        unit.setToBaseFormula("1/1000");
        unit.setFromBaseFormula("1 * 1000");
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_update.json")
    public void shouldUpdateExistingUnit() {
        final Unit unit = new Unit("Gram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Gram Unit");
        unit.setToBaseFormula("[value]*1000");
        unit.setMeasuringSystem(MeasuringSystem.IMPERIAL);
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsNullAtCreation() {
        final Unit unit = new Unit(null, new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Kilogram Unit");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        unit.setToBaseFormula("1/1000");
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsNullAtUpdate() {
        final Unit unit = new Unit(null, new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Kilogram Unit");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsEmptyAtCreation() {
        final Unit unit = new Unit("", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Kilogram Unit");
        unit.setToBaseFormula("1/1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsEmptyAtUpdate() {
        final Unit unit = new Unit("", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Kilogram Unit");
        unit.setToBaseFormula("1/1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsNullAtCreation() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                null);
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        unit.setToBaseFormula("1/1000");
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsNullAtUpdate() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                null);
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsEmptyAtCreation() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "");
        unit.setToBaseFormula("1/1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsEmptyAtUpdate() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "");
        unit.setToBaseFormula("1/1000");
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenInvalidArithmeticOperatorsExistInFormula() {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef"),
                "Kilogram Unit");
        unit.setToBaseFormula("[value]//1000");
        unitRepository.save(unit);
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionWhenInvalidTextInFormula() {
        final Unit unit = new Unit("Kilogram", new ObjectId("59b63ec8e110b21a936c9eef"),
                "Kilogram Unit");
        unit.setToBaseFormula("[value]/1000ABC");
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create_with_defaultvalues.json")
    public void shouldCreateNewUnitWithDefaultValues() {
        final Unit unit = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Pound Unit");
        unit.setBaseUnit(true);
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create_when_formula_and_measuringSystem_is_null_or_empty.json")
    public void shouldCreateUnitWithDefaultValuesWhenMeasuringSystemAndFormulaAreNull() {
        final Unit unit = new Unit("Ounce", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Ounce Unit");
        unit.setToBaseFormula(null);
        unit.setMeasuringSystem(null);
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_create_when_formula_and_measuringSystem_is_null_or_empty.json")
    public void shouldCreateUnitFormulaWithDefaultValueWhenFormulaIsEmpty() {
        final Unit unit = new Unit("Ounce", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Ounce Unit");
        unit.setToBaseFormula("");
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_update_when_formula_and_measuringSystem_is_null_or_empty.json")
    public void shouldUpdateUnitFormulaAndMeasuringSystemWithDefaultValueWhenMeasuringSystemAndFormulaAreNull() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Kilogram Unit");
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unit.setToBaseFormula(null);
        unit.setFromBaseFormula(null);
        unit.setMeasuringSystem(null);
        unitRepository.save(unit);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/units/unit_after_update_when_formula_and_measuringSystem_is_null_or_empty.json")
    public void shouldUpdateUnitFormulaWithDefaultValueWhenFormulaIsEmpty() {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Kilogram Unit");
        unit.setToBaseFormula("");
        unit.setFromBaseFormula("");
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unitRepository.save(unit);
    }

    @Test
    public void shouldFindUnitsByGroupId() {
        final List<Unit> units = unitRepository.findAllByGroupId(
                new ObjectId("74e9155b5ed24e4c38d60e3c"));
        assertThat(units, is(notNullValue()));
        assertThat(units.get(0).getName(), is("fahrenheit"));
        assertThat(units.get(0).getGroupId(),
                is(new ObjectId("74e9155b5ed24e4c38d60e3c")));
        assertThat(units.get(0).getToBaseFormula(), is("1/1000"));
        assertThat(units.get(0).getMeasuringSystem(), is(MeasuringSystem.METRIC));
        assertThat(units.get(0).getDescription(), is("Temperature Unit"));
        assertThat(units.get(1).getName(), is("Degree Celcius"));
        assertThat(units.get(1).getGroupId(),
                is(new ObjectId("74e9155b5ed24e4c38d60e3c")));
        assertThat(units.get(1).getToBaseFormula(), is("[value]*1000"));
        assertThat(units.get(1).getMeasuringSystem(), is(MeasuringSystem.METRIC));
        assertThat(units.get(1).getDescription(), is("Degree Celcius Unit"));
        assertThat(units.get(2).getName(), is("Kelvin"));
        assertThat(units.get(2).getGroupId(),
                is(new ObjectId("74e9155b5ed24e4c38d60e3c")));
        assertThat(units.get(2).getToBaseFormula(), is("[value]/1000"));
        assertThat(units.get(2).getMeasuringSystem(), is(MeasuringSystem.METRIC));
        assertThat(units.get(2).getDescription(), is("Temperature Unit"));
    }

    @Test
    public void shouldFindByName() {
        final Unit unit = unitRepository.findByName("Kilogram");
        assertThat(unit.getGroupId(), is(new ObjectId("53e9155b5ed24e4c38d60e3c")));
        assertThat(unit.getToBaseFormula(), is("1/1000"));
        assertThat(unit.getFromBaseFormula(), is("[value]"));
        assertThat(unit.getMeasuringSystem(), is(MeasuringSystem.METRIC));
        assertThat(unit.getDescription(), is("Kilogram Unit"));
    }
}
