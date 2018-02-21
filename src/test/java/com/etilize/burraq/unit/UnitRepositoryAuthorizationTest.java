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

package com.etilize.burraq.unit;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import com.etilize.burraq.unit.test.AbstractIntegrationTest;
import com.etilize.burraq.unit.test.security.WithOAuth2Authentication;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;

@UsingDataSet(locations = "/datasets/units/units.json")
@WithOAuth2Authentication(username = "ROLE_PTM", clientId = "unauthorized")
public class UnitRepositoryAuthorizationTest extends AbstractIntegrationTest {

    @Autowired
    private UnitRepository repository;

    @Test(expected = AccessDeniedException.class)
    public void shouldThrowAccessDeniedExceptionExceptionWhenUnAuthorizedUserCreatesNewUnit()
            throws Exception {
        final Unit unit = new Unit("Pound", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Pound Unit");
        unit.setBaseUnit(true);
        unit.setMeasuringSystem(MeasuringSystem.METRIC);
        repository.save(unit);
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldThrowAccessDeniedExceptionWhenUnAuthorizedUserUpdatesUnit()
            throws Exception {
        final Unit unit = new Unit("Kilogram", new ObjectId("53e9155b5ed24e4c38d60e3c"),
                "Kilogram Unit");
        unit.setId(new ObjectId("59b63ec8e110b21a936c9eed"));
        unit.setFormula(null);
        unit.setMeasuringSystem(null);
        repository.save(unit);
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldThrowAccessDeniedExceptionExceptionWhenUnAuthorizedUserDeletesUnit()
            throws Exception {
        Unit unit = repository.findOne(new ObjectId("59b63ec8e110b21a936c9eed"));
        repository.delete(unit);
    }
}
