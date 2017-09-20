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

import static org.springframework.http.MediaType.*;

import java.io.*;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.etilize.burraq.unit.test.base.AbstractIT;

/**
 * This class contains test case to list and verify exposed API links.
 *
 * @author Nimra Inam
 * @see AbstractIT
 * @since 1.0.0
 */
public class ListApisIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldListApis() throws IOException {
        author("Nimra Inam");
        description("Sends a GET request to base url and expects a list of all the Apis");
        http().client(serviceClient).send().get();
        http().client(serviceClient) //
                .receive().response(HttpStatus.OK) //
                .messageType(MessageType.JSON) //
                .payload(readFile("/datasets/api_list.json"));
    }
}
