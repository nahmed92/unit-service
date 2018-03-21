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

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.etilize.burraq.unit.Unit;
import com.etilize.burraq.unit.UnitRepository;
import com.etilize.burraq.unit.test.AbstractTest;
import com.google.common.collect.Sets;

public class ConvertServiceTest extends AbstractTest {

    @InjectMocks
    private ConvertServiceImpl unitConvert;

    @Mock
    private UnitRepository unitRepository;

    @Before
    public void init() {
        final Unit gram = new Unit("gram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "gram Unit");
        gram.setBaseUnit(true);
        gram.setFromBaseFormula("[value]");
        gram.setToBaseFormula("[value]");

        final Unit kilogram = new Unit("Kilogram",
                new ObjectId("53e9155b5ed24e4c38d60e3c"), "Kilogram Unit");
        kilogram.setBaseUnit(false);
        kilogram.setFromBaseFormula("[value]/1000");
        kilogram.setToBaseFormula("[value]*1000");

        final Unit pound = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Pound Unit");
        pound.setBaseUnit(false);
        pound.setFromBaseFormula("[value]/453.59");
        pound.setToBaseFormula("[value]*453.59");

        final Unit degree = new Unit("Degree", new ObjectId("73e9155b5ed24e4c38d60e3f"),
                "Degree Unit");
        degree.setBaseUnit(false);
        degree.setFromBaseFormula("[value]");
        degree.setToBaseFormula("[value]");

        final Unit ounce = new Unit("Ounce", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Ounce Unit");
        ounce.setBaseUnit(false);
        ounce.setFromBaseFormula("[value]/0");
        ounce.setToBaseFormula("[value]*0");

        when(unitRepository.findByName("degree")).thenReturn(degree);
        when(unitRepository.findByName("gram")).thenReturn(gram);
        when(unitRepository.findByName("Kilogram")).thenReturn(kilogram);
        when(unitRepository.findByName("Pound")).thenReturn(pound);
        when(unitRepository.findByName("Ounce")).thenReturn(ounce);
    }

    @Test
    public void shouldConvertWhenSourceIsBaseUnit() {

        final Source sourceUnit = new Source("gram", new Double(2000));
        final Payload payload = new Payload(sourceUnit,
                Sets.newHashSet("Kilogram", "Pound"));
        final List<ConvertedUnit> convertedUnit = unitConvert.convert(
                payload).getResult();
        assertThat(convertedUnit.get(0).getUnit(), is("Kilogram"));
        assertThat(convertedUnit.get(0).getValue(), is(new BigDecimal("2.000")));
        assertThat(convertedUnit.get(1).getUnit(), is("Pound"));
        assertThat(convertedUnit.get(1).getValue(), is(new BigDecimal("4.410")));
    }

    @Test
    public void shouldConvertWhenSourceIsNotBaseUnit() {
        final Source sourceUnit = new Source("Kilogram", new Double(2));
        final Payload payload = new Payload(sourceUnit, Sets.newHashSet("gram", "Pound"));
        final List<ConvertedUnit> convertedUnit = unitConvert.convert(
                payload).getResult();
        assertThat(convertedUnit.get(0).getUnit(), is("gram"));
        assertThat(convertedUnit.get(0).getValue(), is(new BigDecimal("2000.000")));
        assertThat(convertedUnit.get(1).getUnit(), is("Pound"));
        assertThat(convertedUnit.get(1).getValue(), is(new BigDecimal("4.410")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowUnsupportedOperationExceptionWhenAnyTargetUnitIsNotInSourceUnitGroup() {
        final Source sourceUnit = new Source("gram", new Double(2000));
        final Payload payload = new Payload(sourceUnit,
                Sets.newHashSet("Kilogram", "Degree"));
        final List<ConvertedUnit> convertedUnit = unitConvert.convert(
                payload).getResult();
        assertThat(convertedUnit.get(0).getUnit(), is("Kilogram"));
        assertThat(convertedUnit.get(0).getValue(), is(new BigDecimal("2.000")));
        assertThat(convertedUnit.get(1).getUnit(), is("Degree"));
        assertThat(convertedUnit.get(1).getValue(), is(new BigDecimal("4.410")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowUnsupportedOperationExceptionWhenTargetUnitDoesNotExists() {
        final Source sourceUnit = new Source("gram", new Double(2000));
        final Payload payload = new Payload(sourceUnit,
                Sets.newHashSet("Kilogram", "km/h"));
        unitConvert.convert(payload);
    }

    @Test
    public void shouldConvertValueWithPrecisionThree() {
        final Source sourceUnit = new Source("gram", new Double(2000));
        final Payload payload = new Payload(sourceUnit, Sets.newHashSet("Pound"));
        final List<ConvertedUnit> convertUnit = unitConvert.convert(payload).getResult();
        assertThat(convertUnit.get(0).getUnit(), is("Pound"));
        assertThat(convertUnit.get(0).getValue(), is(not(new BigDecimal("4.409245"))));
        assertThat(convertUnit.get(0).getValue(), is(new BigDecimal("4.410")));
    }

    @Test(expected = ArithmeticException.class)
    public void shouldThrowArithmeticExceptionOnFormulaExecutionWhenSourceValueIsNotValid() {
        final Source sourceUnit = new Source("gram", new Double("2"));
        final Payload payload = new Payload(sourceUnit, Sets.newHashSet("Ounce"));
        unitConvert.convert(payload);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenPayloadIsNull() {
        unitConvert.convert(null);
    }
}
