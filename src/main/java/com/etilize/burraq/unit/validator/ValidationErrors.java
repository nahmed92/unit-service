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

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * This class define unit Error
 *
 * @author Nasir Ahmed
 *
 */
public class ValidationErrors implements Errors {

    private final String objectName;

    private final String field;

    private final String defaultMessage;

    /**
     * Create a new UnitError instance.
     * @param objectName the name of the affected object
     * @param field the affected field of the object
     * @param defaultMessage the default message to be used to resolve this message
     */
    public ValidationErrors(final String objectName, final String field,
            final String defaultMessage) {
        this.objectName = objectName;
        this.field = field;
        this.defaultMessage = defaultMessage;
    }

    /**
     * @return the objectName
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @return the defaultMessage
     */
    public String getDefaultMessage() {
        return defaultMessage;
    }

    @Override
    public void setNestedPath(final String nestedPath) {

    }

    @Override
    public String getNestedPath() {

        return null;
    }

    @Override
    public void pushNestedPath(final String subPath) {

    }

    @Override
    public void popNestedPath() {

    }

    @Override
    public void reject(final String errorCode) {

    }

    @Override
    public void reject(final String errorCode, final String defaultMessage) {

    }

    @Override
    public void reject(final String errorCode, final Object[] errorArgs,
            final String defaultMessage) {

    }

    @Override
    public void rejectValue(final String field, final String errorCode) {

    }

    @Override
    public void rejectValue(final String field, final String errorCode,
            final String defaultMessage) {

    }

    @Override
    public void rejectValue(final String field, final String errorCode,
            final Object[] errorArgs, String defaultMessage) {

    }

    @Override
    public void addAllErrors(final Errors errors) {

    }

    @Override
    public boolean hasErrors() {

        return false;
    }

    @Override
    public int getErrorCount() {

        return 0;
    }

    @Override
    public List<ObjectError> getAllErrors() {
        return null;
    }

    @Override
    public boolean hasGlobalErrors() {
        return false;
    }

    @Override
    public int getGlobalErrorCount() {
        return 0;
    }

    @Override
    public List<ObjectError> getGlobalErrors() {
        return null;
    }

    @Override
    public ObjectError getGlobalError() {
        return null;
    }

    @Override
    public boolean hasFieldErrors() {
        return false;
    }

    @Override
    public int getFieldErrorCount() {
        return 0;
    }

    @Override
    public List<FieldError> getFieldErrors() {
        final List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError(objectName, field, defaultMessage));
        return fieldErrors;
    }

    @Override
    public FieldError getFieldError() {
        return null;
    }

    @Override
    public boolean hasFieldErrors(final String field) {
        return false;
    }

    @Override
    public int getFieldErrorCount(final String field) {
        return 0;
    }

    @Override
    public List<FieldError> getFieldErrors(final String field) {
        return null;
    }

    @Override
    public FieldError getFieldError(final String field) {
        return null;
    }

    @Override
    public Object getFieldValue(final String field) {
        return null;
    }

    @Override
    public Class<?> getFieldType(final String field) {
        return null;
    }

}
