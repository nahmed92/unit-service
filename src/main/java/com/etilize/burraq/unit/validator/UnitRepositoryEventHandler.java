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
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.etilize.burraq.unit.Unit;
import com.etilize.burraq.unit.UnitRepository;
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

    private final UnitRepository unitRepository;

    /**
     * Contructor to initialize UnitRepositoryEventHandler
     *
     * @param groupRepository Group Repository
     * @param unitRepository Unit Repository
     */
    @Autowired
    public UnitRepositoryEventHandler(final GroupRepository groupRepository,
            final UnitRepository unitRepository) {
        Assert.notNull(groupRepository, "groupRepository can not be null");
        Assert.notNull(unitRepository, "unitRepository can not be null");
        this.groupRepository = groupRepository;
        this.unitRepository = unitRepository;
    }

    /**
     * Handle create request to validate.
     * Set isbaseUnit flag to true if associated group has no BaseUnit
     *
     * validate group exist for associated unit.
     *
     * @param unit entity
     */
    @HandleBeforeCreate(Unit.class)
    public void handleBeforeUnitCreate(final Unit unit) {
        if (unit.getGroupId() != null) {
            final Group group = groupRepository.findOne(unit.getGroupId());
            // validate group exist for associated unit
            validateGroup(group);
            // set baseUnitId to true if no baseUnit is set in group
            if (group.getBaseUnitId() == null) {
                unit.setBaseUnit(true);
            }
        }
    }

    /**
     * Handle update request to validate.
     *
     * Validate group exist for associated unit.
     * Validate unit for update groupId
     *
     * @param unit entity
     */
    @HandleBeforeSave(Unit.class)
    public void handleBeforeUnitSave(final Unit unit) {
        if (unit.getGroupId() != null) {
            final Group existingGroup = groupRepository.findOne(unit.getGroupId());
            final Unit existingUnit = unitRepository.findOne(unit.getId());
            // validate group exist for associated unit
            validateGroup(existingGroup);
            // validate unit
            if (!existingUnit.getGroupId().equals(unit.getGroupId())) {
                throw new RepositoryConstraintViolationException(new ValidationErrors(
                        "unit", "groupId", "update of groupId isn't allowed."));
            }
        }
    }

    /**
     * Handle after create unit.
     *
     * Set unitId as baseUnitId to group is not Exist Set isbaseUnitId to true
     *
     * @param unit entity
     */
    @HandleAfterCreate(Unit.class)
    public void handleAfterUnitCreate(final Unit unit) {
        final Group group = groupRepository.findOne(unit.getGroupId());
        if (group.getBaseUnitId() == null) {
            group.setBaseUnitId(unit.getId());
            groupRepository.save(group);
        }
    }

    /**
     * Method to validate Group Exists
     *
     * @param group associated with unit
     */
    private void validateGroup(final Group group) {
        if (group == null) {
            throw new RepositoryConstraintViolationException(
                    new ValidationErrors("unit", "groupId", "Group does not exist."));
        }
    }
}
