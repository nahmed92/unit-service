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

import org.junit.*;
import org.springframework.http.*;

import com.consol.citrus.annotations.*;
import com.etilize.burraq.unit.test.base.*;

/**
 * This class contains test cases related to Delete Unit functionality.
 *
 * @author Nimra Inam
 * @see AbstractIT
 * @since 1.0.0
 */
public class DeleteUnitIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldDeleteUnit() throws Exception {
        author("Nimra Inam");
        description("A unit should be deleted");

        variable(UNIT_ID, EXISTING_UNIT_ID_TO_REMOVE);

        deleteRequest(UNITS_URL, "${" + UNIT_ID + "}");

        verifyResponse(HttpStatus.NO_CONTENT);

        getRequest("/units/${" + UNIT_ID + "}");

        verifyResponse(HttpStatus.NOT_FOUND);
    }

    @Test
    @CitrusTest
    public void shouldNotDeleteAUnitWhichDoesNotExist() throws Exception {
        author("Nimra Inam");
        description("A unit should not be deleted if it does not exist");

        variable(UNIT_ID, "59afe1125846b8762efc30e2");

        deleteRequest(UNITS_URL, "${" + UNIT_ID + "}");

        verifyResponse(HttpStatus.NOT_FOUND);
    }

    @Test
    @CitrusTest
    public void shouldNotDeleteUnitWhichIsNotBaseUnit() throws Exception {
        author("Nimra Inam");
        description("A base unit should not be deleted");

        variable(UNIT_ID, EXISTING_BASE_UNIT_ID_TO_REMOVE);

        deleteRequest(UNITS_URL, "${" + UNIT_ID + "}");

        verifyResponse(HttpStatus.BAD_REQUEST, //
                readFile("/datasets/units/validations/delete/delete_base_unit_response.json"));
    }

}
