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

package com.etilize.burraq.unit.validator;

import javax.inject.Inject;

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
 * Unit Event Handler
 *
 * @author Nasir Ahmed
 *
 */
@Component
@RepositoryEventHandler
public class UnitRepositoryEventHandler {

    private static final int MAX_NUMBER_OF_BASE_UNITS_ALLOWED_IN_GROUP = 2;

    private final UnitRepository unitRepository;

    private final GroupRepository groupRepository;

    /**
     * Contructor to initialize UnitRepositoryEventHandler
     *
     * @param unitRepository Unit Repository
     * @param groupRepository Group Repository
     */
    @Inject
    public UnitRepositoryEventHandler(final UnitRepository unitRepository,
            final GroupRepository groupRepository) {
        Assert.notNull(unitRepository, "unitRepository can not be null");
        Assert.notNull(groupRepository, "groupRepository can not be null");
        this.unitRepository = unitRepository;
        this.groupRepository = groupRepository;
    }

    /**
     * Handle create request to validate.
     * validate group exist for associated unit.
     * validate only two base unit in a group allowed.
     *
     * @param unit entity
     */
    @HandleBeforeCreate(Unit.class)
    public void handleBeforeUnitCreate(final Unit unit) {
        // validate group exist for associated unit
        validateGroup(unit);
        // validate only two base unit in a group allowed
        if (unit.isBaseUnit()) {
            validateExistingBaseUnitCount(unit);
        }
    }

    /**
     * Handle update request to validate.
     * validate group exist for associated unit.
     * validate only two base unit in a group allowed
     *
     * @param unit entity
     */
    @HandleBeforeSave(Unit.class)
    public void handleBeforeUnitUpdate(final Unit unit) {
        // validate group exist for associated unit
        validateGroup(unit);
        // validate only two base unit in a group allowed
        final Unit existingUnit = unitRepository.findOne(unit.getId());
        if (unit.isBaseUnit() && !existingUnit.isBaseUnit()) {
            validateExistingBaseUnitCount(unit);
        }
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
                throw new RepositoryConstraintViolationException(new ValidationErrors(
                        "unit", unit.getName(), "Group does not exist."));
            }
        }
    }

    /**
     * Method to validate maxmium two base unit allowed in a group
     *
     * @param unit current unit
     */
    private void validateExistingBaseUnitCount(final Unit unit) {
        final int count = unitRepository.countByGroupIdAndIsBaseUnit(unit.getGroupId(),
                unit.isBaseUnit());
        if (count >= MAX_NUMBER_OF_BASE_UNITS_ALLOWED_IN_GROUP) {
            throw new RepositoryConstraintViolationException(new ValidationErrors("unit",
                    unit.getName(), "Maximum " + MAX_NUMBER_OF_BASE_UNITS_ALLOWED_IN_GROUP
                            + " base unit(s) allowed in one group."));
        }
    }
}
