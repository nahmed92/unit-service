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

import java.util.EmptyStackException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

/**
 * Custom validator interface implementation for unit Formula
 *
 * @author Nasir Ahmed
 * @since 1.0
 */
public class FormulaValidator implements ConstraintValidator<Formula, String> {

    private final Logger logger = Logger.getLogger(FormulaValidator.class);

    /**
     * Regular expression for the formula expression
     *
     *  Regex to find operator multiple time like 1++2 valid is 1+2 or (1/(1000+2))
     */
    private static final Pattern INVALID_FORMULA_EXPRESSION = Pattern.compile(
            "\\d+(\\.\\d+)?[-+*/]{2,}\\d+(\\.\\d+)?");

    @Override
    public void initialize(final Formula formula) {

    }

    @Override
    public boolean isValid(final String formula,
            final ConstraintValidatorContext context) {
        // return false if placeHolder define more than one time
        if (StringUtils.countOccurrencesOf(formula, "[value]") > 1) {
            return false;
        }

        // Replace placeholder[value] by 1 to avoid character search of
        // allowable formula  expression [value],[] is reserved keyword of regex.
        final String formulaWithoutPlaceHolder = formula.replace("[value]", "1");

        // check for Invalid operator of formula
        final Matcher matcher = INVALID_FORMULA_EXPRESSION.matcher(
                formulaWithoutPlaceHolder);
        if (matcher.find()) {
            return false;
        }

        // exp4J api used to validate mathematical expression of formula
        // exp4J Expression throw Exceptions when expression is invalid
        // therefore catching and return boolean false when exception took place.
        boolean isValid = true;
        try {
            final Expression expressionBuilder = new ExpressionBuilder(
                    formulaWithoutPlaceHolder).build();
            final ValidationResult res = expressionBuilder.validate();
            isValid = res.isValid();

            //Adding NOSONAR to skip below sonar error
            //Exception handlers should preserve the original exceptions
            //Exception message provides reason of occurence in validation
        } catch (final IllegalArgumentException | EmptyStackException ex) { //NOSONAR
            logger.info(ex.getMessage());
            isValid = false;
        }

        return isValid;
    }
}
