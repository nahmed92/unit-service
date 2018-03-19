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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.etilize.burraq.unit.Unit;
import com.etilize.burraq.unit.UnitRepository;
import com.google.common.collect.Lists;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * ConvertService Implementation
 *
 * @author Nasir Ahmed
 *
 */
@Service
public class ConvertServiceImpl implements ConvertService {

    @Autowired
    private UnitRepository unitRepository;

    // unit precision
    private static final int PRECISION = 3;

    /* (non-Javadoc)
     * @see com.etilize.burraq.unit.service.ConvertService#convert(com.etilize.burraq.unit.service.Source)
     */
    @Override
    public Result convert(final Payload payload) {
        Assert.notNull(payload, "payload cannot be null");
        final Source source = payload.getSource();
        List<ConvertedUnit> convertedUnits = Lists.newArrayList();
        final Unit unit = unitRepository.findByName(source.getUnit());
        if (unit == null) {
            throw new UnsupportedOperationException(
                    "Source unit [" + source.getUnit() + "] does not exist.");
        }
        // check if it is baseUnit, simply convert into target unit
        if (unit.isBaseUnit()) {
            convertedUnits = convertBaseUnitValueToTargetUnits(source.getValue(),
                    payload.getTarget(), unit.getGroupId());
        } else {
            // if not baseUnit, then it convert into baseUnit first
            final Double baseUnitValue = executeFormula(unit.getToBaseFormula(),
                    source.getValue());
            // then convert into target units
            convertedUnits = convertBaseUnitValueToTargetUnits(baseUnitValue,
                    payload.getTarget(), unit.getGroupId());
        }
        return new Result(convertedUnits);
    }

    /**
     * Convert value into target unit values of same group.
     * @param sourceValue {@link Double}
     * @param target list of target units
     * @param groupId {@link ObjectId}}
     * @return List of {@link ConvertedUnit}
     */
    private List<ConvertedUnit> convertBaseUnitValueToTargetUnits(
            final double sourceValue, final Set<String> target, final ObjectId groupId) {
        final List<ConvertedUnit> targetUnits = Lists.newArrayList();
        for (final String unitName : target) {
            // get TargetUnit
            final Unit unit = unitRepository.findByName(unitName);
            // check Taget unit exist
            if (unit == null) {
                throw new UnsupportedOperationException(
                        "Target unit [" + unitName + "] does not exist.");
            }
            // check target unit is same group of source unit
            if (!unit.getGroupId().equals(groupId)) {
                throw new UnsupportedOperationException("Target unit [" + unit.getName()
                        + "] not found in source unit group.");
            }
            final BigDecimal value = new BigDecimal(
                    executeFormula(unit.getFromBaseFormula(), sourceValue));
            targetUnits.add(new ConvertedUnit(unitName,
                    value.setScale(PRECISION, RoundingMode.CEILING)));
        }
        return targetUnits;
    }

    /**
     * Execute formula With provided value
     * @param formula input formula
     * @param value value that need to be converted
     * @return formula result
     */
    private Double executeFormula(final String formula, final Double value) {
        final Expression calc = new ExpressionBuilder(formula) //
                .variables("value") //
                .build() //
                .setVariable("value", value);
        return calc.evaluate();
    }
}
