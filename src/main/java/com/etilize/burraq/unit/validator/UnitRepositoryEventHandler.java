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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.etilize.burraq.unit.Unit;
import com.etilize.burraq.unit.group.Group;
import com.etilize.burraq.unit.group.GroupRepository;

/**
 * Unit Event Handler
 *
 * @author Nasir Ahmed
 *
 */
@Component
@RepositoryEventHandler
public class UnitRepositoryEventHandler {

    private final GroupRepository groupRepository;

    /**
     * Contructor to initialize UnitRepositoryEventHandler
     *
     * @param groupRepository Group Repository
     */
    @Autowired
    public UnitRepositoryEventHandler(final GroupRepository groupRepository) {
        Assert.notNull(groupRepository, "groupRepository can not be null");
        this.groupRepository = groupRepository;
    }

    /**
     * Handle create and update request to validate.
     *
     * validate group exist for associated unit.
     *
     * @param unit entity
     */
    @HandleBeforeCreate(Unit.class)
    @HandleBeforeSave(Unit.class)
    public void handleBeforeUnitSave(final Unit unit) {
        // validate group exist for associated unit
        validateGroup(unit);
    }

    /**
     * Method to validate Group Exists
     *
     * @param unit unit associated with group
     */
    private void validateGroup(final Unit unit) {
        if (unit.getGroupId() != null) {
            final Group group = groupRepository.findOne(unit.getGroupId());
            if (group == null) {
                throw new RepositoryConstraintViolationException(
                        new ValidationErrors("unit", "groupId", "Group does not exist."));
            }
        }
    }
}
