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

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.Before;
import org.junit.Test;

import com.etilize.burraq.unit.test.AbstractTest;
import com.etilize.burraq.unit.validator.FormulaValidator;

public class FormulaValidatorTest extends AbstractTest {

    private ConstraintValidatorContext context;

    private FormulaValidator formulaValidator;

    @Before
    public void setup() {
        formulaValidator = new FormulaValidator();
        final PathImpl path = PathImpl.createRootPath();
        path.addBeanNode();
        context = new ConstraintValidatorContextImpl(null, null, path, null);
    }

    @Test
    public void shouldReturnFalseWhenFormulaContainsRepeatedOperator() {
        assertThat(formulaValidator.isValid("1**2", context), is(false));
        assertThat(formulaValidator.isValid("1//2", context), is(false));
        assertThat(formulaValidator.isValid("1000/(1++2)", context), is(false));
        assertThat(formulaValidator.isValid("1000/(1+2--3)", context), is(false));
    }

    @Test
    public void shouldReturnFalseWhenFormulaContainsTextOtherThenPlaceHolder() {
        assertThat(formulaValidator.isValid("[value]/1000 Kilogram", context), is(false));
    }

    @Test
    public void shouldReturnFalseWhenFormulaConstainInvalidOperatorOrFormat() {
        assertThat(formulaValidator.isValid("[value]/(1000", context), is(false));
        // this case throw java.util.EmptyStackException
        assertThat(formulaValidator.isValid("[value]/1000)", context), is(false));
        assertThat(formulaValidator.isValid("[value]/((1000)", context), is(false));
        assertThat(formulaValidator.isValid("[value]/(1000))", context), is(false));
        assertThat(formulaValidator.isValid("/[value]/(1000", context), is(false));
        // this case throw java.util.IllegalArgumentException
        assertThat(formulaValidator.isValid("%10/1000", context), is(false));
        assertThat(formulaValidator.isValid("10/()(1000+32)", context), is(false));
        assertThat(formulaValidator.isValid("10/(abc+34)", context), is(false));
        assertThat(formulaValidator.isValid("abc", context), is(false));
        assertThat(formulaValidator.isValid("1%", context), is(false));
        assertThat(formulaValidator.isValid("[value]/(2@34)", context), is(false));
        assertThat(formulaValidator.isValid("1000/(20+30)+", context), is(false));
        assertThat(formulaValidator.isValid("[value])1000(", context), is(false));
        assertThat(formulaValidator.isValid("value)1000(", context), is(false));
        assertThat(formulaValidator.isValid("[VALUE]/(1000+1)+(34+43)", context),
                is(false));
        assertThat(formulaValidator.isValid("[value]/(1000++1)+(34+43)", context),
                is(false));
        assertThat(formulaValidator.isValid("[value]/(1000+1)+(34++43)", context),
                is(false));
    }

    @Test
    public void shouldReturnTrueWhenFormulaContainsPlaceHolder() {
        assertThat(formulaValidator.isValid("[value]/1000", context), is(true));
    }

    @Test
    public void shouldReturnTrueWhenFormulaContainsNoPlaceHolder() {
        assertThat(formulaValidator.isValid("1000", context), is(true));
    }

    @Test
    public void shouldReturnTrueWhenFormulaContainsValidOperator() {
        assertThat(formulaValidator.isValid("1", context), is(true));
        assertThat(formulaValidator.isValid("1*2", context), is(true));
        assertThat(formulaValidator.isValid("1/2", context), is(true));
        assertThat(formulaValidator.isValid("1000/(1+2)", context), is(true));
        assertThat(formulaValidator.isValid("[value]/1000+13", context), is(true));
        assertThat(formulaValidator.isValid("[value]/(10+13)", context), is(true));
        assertThat(formulaValidator.isValid("[value]/(100)", context), is(true));
        assertThat(formulaValidator.isValid("-1000/(1000+23)", context), is(true));
        assertThat(formulaValidator.isValid("-10.23/10.23", context), is(true));
        assertThat(formulaValidator.isValid("-10.23/1000", context), is(true));
        assertThat(formulaValidator.isValid("1023/10.00", context), is(true));
        assertThat(formulaValidator.isValid("1000/(20+30)+(20+40)", context), is(true));
        assertThat(formulaValidator.isValid("(1+23)+(32+23)/(20+30)+(20+40)", context),
                is(true));
        assertThat(formulaValidator.isValid("1000/(20+30)+1", context), is(true));
        assertThat(formulaValidator.isValid("1/(20+30)+(20+40)+(23+1)", context),
                is(true));
    }

    @Test
    public void shouldReturnTrueWhenFormulaContainsDefaultValue() {
        assertThat(formulaValidator.isValid("[value]", context), is(true));
    }

}
