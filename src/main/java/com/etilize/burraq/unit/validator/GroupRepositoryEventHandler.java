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

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
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
 * Group Repository Event Handler
 *
 * @author Nasir Ahmed
 *
 */
@Component
@RepositoryEventHandler
public class GroupRepositoryEventHandler {

    private static final String ENTITY = "group";

    private static final String FIELD_NAME = "baseUnitId";

    private final UnitRepository unitRepository;

    private final GroupRepository groupRepository;

    /**
     * Contructor to initialize GroupRepositoryEventHandler
     *
     * @param unitRepository Unit Repository
     * @param groupRepository Group Repository
     */
    @Autowired
    public GroupRepositoryEventHandler(final UnitRepository unitRepository,
            final GroupRepository groupRepository) {
        Assert.notNull(unitRepository, "unitRepository can not be null");
        Assert.notNull(groupRepository, "groupRepository can not be null");
        this.unitRepository = unitRepository;
        this.groupRepository = groupRepository;
    }

    /**
     * Handle create request.
     * Validate baseUnitId not entered at time of group creation
     *
     * @param group group entity
     */
    @HandleBeforeCreate(Group.class)
    public void handleBeforeCreate(final Group group) {
        if (group.getBaseUnitId() != null) {
            throw new RepositoryConstraintViolationException(validationError(ENTITY,
                    FIELD_NAME, "baseUnitId is not allowed at group creation."));
        }
    }

    /**
     * Handle update request.when updating baseUnitId in group
     * Scenario 1: if group baseUnitId set first time, unit isbaseUnit
     * also set to true
     * Scenario 2 : if group baseUnitId get updated then existing unit
     * isbaseUnit set to false and new unit isbaseUnit set to true(new base unit)
     * Scenario 3 : if baseUnitId set to null at update then throw Exception
     *
     * @param updatedGroup entity
     */
    @HandleBeforeSave(Group.class)
    public void handleBeforeSave(final Group updatedGroup) {
        final Group existingGroup = groupRepository.findOne(updatedGroup.getId());
        if (updatedGroup.getBaseUnitId() != null) {
            if (existingGroup.getBaseUnitId() == null) {
                updateBaseUnit(updatedGroup.getBaseUnitId(), true);
            } else if (!existingGroup.getBaseUnitId().equals(
                    updatedGroup.getBaseUnitId())) {
                updateBaseUnit(existingGroup.getBaseUnitId(), false);
                updateBaseUnit(updatedGroup.getBaseUnitId(), true);
            }
        } else {
            if (existingGroup.getBaseUnitId() != null) {
                throw new RepositoryConstraintViolationException(validationError(ENTITY,
                        FIELD_NAME, "baseUnitId can't be null at update."));
            }
        }
    }

    /**
     * Update isBaseUnit flag of unit associated with group
     * If unit not exist it will throw Exception
     *
     * @param unitId Id of group associated unit
     * @param isBaseUnit flag indicate BaseUnit
     */
    private void updateBaseUnit(final ObjectId unitId, final Boolean isBaseUnit) {
        final Unit unit = unitRepository.findOne(unitId);
        if (unit == null) {
            throw new RepositoryConstraintViolationException(
                    validationError(ENTITY, FIELD_NAME, "Unit does not exist."));
        }
        unit.setBaseUnit(isBaseUnit);
        unitRepository.save(unit);
    }

    /**
     * Build error message
     * @param name Entity name
     * @param field Name of Field
     * @param message Error message
     *
     * @return validationErrors Object
     */
    private ValidationErrors validationError(final String name, final String field,
            final String message) {
        return new ValidationErrors(name, field, message);
    }
}
