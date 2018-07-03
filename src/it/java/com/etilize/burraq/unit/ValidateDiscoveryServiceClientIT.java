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
import com.consol.citrus.http.message.HttpMessage;
import com.consol.citrus.message.*;
import com.etilize.burraq.unit.test.base.*;

/**
 * This class contains test case to verify Discovery Service Client's Integration. In
 * order to execute this case successfully, the following property need to be added in
 * service's application.yml on stage OR local machine: endpoints: metrics: sensitive:
 * false
 *
 * @author Nimra Inam
 * @see AbstractIT
 * @since 1.0.0
 */

public class ValidateDiscoveryServiceClientIT extends AbstractIT {

    protected final static String METRICS_URL = "/metrics/";

    @Test
    @CitrusTest
    public void shouldVerifyDiscoveryClientProperty() throws Exception {
        author("Nimra Inam");
        description("Discovery client's property should be verified in response");

        variable("propertyName", "gauge.servo.eurekaclient.registry.localregistrysize");

        getRequest(METRICS_URL);

        receive(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage().status(HttpStatus.OK)) //
                .messageType(MessageType.JSON) //
                .validate("$", "@contains(${propertyName})@"));

    }

}
