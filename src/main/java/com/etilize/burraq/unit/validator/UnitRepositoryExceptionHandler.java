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

package com.etilize.burraq.unit.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mongodb.DuplicateKeyException;

/**
 * This class implements exception handling for duplicates keys.
 *
 * @author Nasir Ahmed
 *
 */
@ControllerAdvice
public class UnitRepositoryExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(
            UnitRepositoryExceptionHandler.class);

    /**
     * Handle repository duplicate key exception.
     *
     * @param ex Excetion {@link DuplicateKeyException}
     * @return {@link ResponseEntity} Response for given entity.
     */
    @ExceptionHandler(value = { DuplicateKeyException.class })
    protected ResponseEntity<Object> handleDuplicateKeyException(
            final DuplicateKeyException ex) {
        final ExceptionMessage errorMessage = new ExceptionMessage("name already exists.",
                ex.getCause(), ex.getCode());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    /**
     * Handle InvalidFormatException exception.
     *
     * @param ex Exception {@link InvalidFormatException}
     * @return {@link ResponseEntity} Response for given entity.
     */
    @ExceptionHandler(value = { InvalidFormatException.class })
    protected ResponseEntity<Object> handleInvalidFormatException(
            final InvalidFormatException ex) {
        final ExceptionMessage errorMessage = new ExceptionMessage(ex.getMessage(),
                ex.getCause(), 0);
        // logging error message
        logger.error("Error during request processing: " + ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle IllegalArgumentException exception.
     *
     * @param ex Exception {@link HttpMessageNotReadableException}
     * @return {@link ResponseEntity} Response for given entity.
     */
    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    protected ResponseEntity<Object> handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException ex) {
        final ExceptionMessage errorMessage = new ExceptionMessage(ex.getMessage(), null,
                0);
        // logging error message
        logger.error("Error during request processing: " + ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }
}
