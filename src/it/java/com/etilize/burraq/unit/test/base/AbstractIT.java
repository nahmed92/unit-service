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

package com.etilize.burraq.unit.test.base;

import static java.nio.file.Files.*;
import static java.nio.file.Paths.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;

import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;

import com.consol.citrus.actions.*;
import com.consol.citrus.context.*;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.message.HttpMessage;
import com.consol.citrus.http.message.*;
import com.consol.citrus.message.*;

/**
 * This class should be extended to write IT test cases. It provides methods to read file,
 * put, post, remove and verify responses.
 *
 * @author Nimra Inam
 * @since 1.0.0
 */
public abstract class AbstractIT extends JUnit4CitrusTestRunner {

    protected final static String GROUPS_URL = "/groups/";

    protected final static String UNITS_URL = "/units/";

    protected final static String CONVERT_URL = "/convert/";

    protected final static String LOCATION_HEADER_VALUE = "locationHeaderValue";

    protected final static String GROUP_ID = "groupId";

    protected final static String UNIT_ID = "unitId";

    protected final static String EXISTING_GROUP_ID = "5995843b0fcdf874985e399d";

    protected final static String EXISTING_GROUP_ID_TO_REMOVE = "59afe1125846b8762efc30e1";

    protected final static String EXISTING_UNIT_ID_TO_REMOVE = "59d217150fcdf85577160d58";

    protected final static String EXISTING_UNIT_ID_TO_UPDATE = "59cc914b2a26200bc964e26e";

    @Autowired
    protected HttpClient serviceClient;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Reads contents of the file.
     *
     * @param fileName the file name.
     * @return contents of the file.
     * @throws IOException if file not found.
     */
    protected String readFile(String fileName) throws IOException {
        return new String(readAllBytes(
                get(resourceLoader.getResource(appendClassPath(fileName)).getURI())));
    }

    private String appendClassPath(final String fileName) {
        return String.format("classpath:%s", fileName);
    }

    /**
     * It sends get request to service
     *
     * @param url Url to get request
     */
    public void getRequest(final String url) {
        send(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage() //
                        .path(url) //
                        .method(HttpMethod.GET) //
                        .contentType(APPLICATION_JSON_VALUE) //
                        .accept(APPLICATION_JSON_VALUE)));
    }

    /**
     * It sends post request to service
     *
     * @param url Url to use to send request
     * @param payload to post
     */
    protected void postRequest(final String url, final String payload) {
        send(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage(payload) //
                        .path(url) //
                        .method(HttpMethod.POST) //
                        .contentType(APPLICATION_JSON_UTF8_VALUE) //
                        .accept(APPLICATION_JSON_VALUE)));
    }

    /**
     * It sends pust request to service
     *
     * @param url Url to use to send request
     * @param groupId to update the exact group
     * @param payload group to post
     */
    protected void putRequest(final String url, final String groupId,
            final String payload) {
        // Sends a put request to api
        send(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage(payload) //
                        .path(url + groupId) //
                        .method(HttpMethod.PUT) //
                        .contentType(APPLICATION_JSON_VALUE) //
                        .accept(APPLICATION_JSON_VALUE)));
    }

    /**
     * It sends delete request to service
     *
     * @param url Url to use to send request
     * @param groupId to delete the exact group
     */
    protected void deleteRequest(final String url, final String groupId) {
        // Sends a delete request to api
        if (StringUtils.equals(groupId, "")) {
            send(builder -> builder.endpoint(serviceClient) //
                    .message(new HttpMessage() //
                            .path(url) //
                            .method(HttpMethod.DELETE) //
                            .contentType(APPLICATION_JSON_VALUE) //
                            .accept(APPLICATION_JSON_VALUE)));

        } else {
            send(builder -> builder.endpoint(serviceClient) //
                    .message(new HttpMessage() //
                            .path(url + groupId) //
                            .method(HttpMethod.DELETE) //
                            .contentType(APPLICATION_JSON_VALUE) //
                            .accept(APPLICATION_JSON_VALUE)));
        }
    }

    /**
     * It extracts header location
     *
     * @param httpStatus
     * @param httpHeaderName
     */
    protected void extractHeader(final HttpStatus httpStatus,
            final String httpHeaderName) {
        receive(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage() //
                        .status(httpStatus)) //
                .messageType(MessageType.JSON) //
                .extractFromHeader(httpHeaderName, "${locationHeaderValue}"));
    }

    /**
     * It verifies the response received from service from header location
     *
     * @param httpStatus HttpStatus status code
     * @param payload response data to verify
     * @param path header location
     */
    protected void verifyResponse(final HttpStatus httpStatus, final String payload,
            final String url) {
        getRequest(url);

        receive(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage() //
                        .status(httpStatus)) //
                .messageType(MessageType.JSON).payload(payload));
    }

    /**
     * It verifies the http response
     *
     * @param httpStatus
     */
    protected void verifyResponse(final HttpStatus httpStatus) {
        // Verify Response
        receive(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage() //
                        .status(httpStatus)) //
                .messageType(MessageType.JSON));
    }

    /**
     * It verifies the response status code and response error message received from
     * service.
     *
     * @param httpStatus HttpStatus status code
     * @param payload to verify in response
     */
    protected void verifyResponse(final HttpStatus httpStatus, final String payload) {
        // Verify Response
        receive(builder -> builder.endpoint(serviceClient) //
                .message(new HttpMessage() //
                        .status(httpStatus)) //
                .messageType(MessageType.JSON) //
                .payload(payload));
    }

    /**
     * It replaces resource location with resource id and set it to context variable.
     *
     * @param url this holds api's URL
     * @param header_value this holds the rsource's location to parse
     */
    protected String parseAndSetVariable(final String url, final String headerValue) {
        String headerLocation = headerValue.substring(headerValue.indexOf(url));
        return headerLocation;
    }

    /**
     * It replaces resource url with new resource id and set it to context variable.
     *
     * @param resourceUrl this holds the rsource's url to parse
     */
    protected String parseAndSetResourceId(final String resourceUrl) {
        final String newResourceId = resourceUrl.substring(
                resourceUrl.lastIndexOf('/') + 1);
        return newResourceId;
    }
}
