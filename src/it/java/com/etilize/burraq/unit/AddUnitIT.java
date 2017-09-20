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

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import com.consol.citrus.annotations.CitrusTest;
import com.etilize.burraq.unit.test.base.*;

/**
 * This class contains test cases related Add Unit functionality with different metric
 * systems with unit and base units.
 *
 * @author Nimra Inam
 * @see AbstractIT
 * @since 1.0.0
 */
public class AddUnitIT extends AbstractIT {

    @Test
    @CitrusTest
    public void shouldAddUnitWithMKSMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with MKS metric system");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, readFile(
                "/datasets/units/metris_systems/unit/unit_with_mks_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, readFile(
                "/datasets/units/metris_systems/unit/unit_with_mks_metric_system_response.json"),
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithCGSMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with CGS metric system");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, readFile(
                "/datasets/units/metris_systems/unit/unit_with_cgs_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, readFile(
                "/datasets/units/metris_systems/unit/unit_with_cgs_metric_system_response.json"),
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithMTSMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with MTS metric system");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, readFile(
                "/datasets/units/metris_systems/unit/unit_with_mts_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, readFile(
                "/datasets/units/metris_systems/unit/unit_with_mts_metric_system_response.json"),
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddUnitWithNoneMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A unit should be added with CGS metric system");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, readFile(
                "/datasets/units/metris_systems/unit/unit_with_none_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, readFile(
                "/datasets/units/metris_systems/unit/unit_with_none_metric_system_response.json"),
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddBaseUnitWithCGSMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A base unit with CGS metric system should be added");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, readFile(
                "/datasets/units/metris_systems/base_unit/base_unit_with_cgs_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, readFile(
                "/datasets/units/metris_systems/base_unit/base_unit_with_cgs_metric_system_response.json"),
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddBaseUnitWithMTSMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A base unit with MTS metric system should be added");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, readFile(
                "/datasets/units/metris_systems/base_unit/base_unit_with_mts_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, readFile(
                "/datasets/units/metris_systems/base_unit/base_unit_with_mts_metric_system_response.json"),
                "${locationHeaderValue}");
    }

    @Test
    @CitrusTest
    public void shouldAddBaseUnitWithNoneMetricSystem() throws Exception {
        author("Nimra Inam");
        description("A base unit with None metric system should be added");

        variable(LOCATION_HEADER_VALUE, "");

        postRequest(UNITS_URL, readFile(
                "/datasets/units/metris_systems/base_unit/base_unit_with_none_metric_system.json"));

        extractHeader(HttpStatus.CREATED, HttpHeaders.LOCATION);
        parseAndSetVariable(UNITS_URL, LOCATION_HEADER_VALUE);
        verifyResponse(HttpStatus.OK, readFile(
                "/datasets/units/metris_systems/base_unit/base_unit_with_none_metric_system_response.json"),
                "${locationHeaderValue}");
    }
}
