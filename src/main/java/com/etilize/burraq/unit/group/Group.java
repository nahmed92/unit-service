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

package com.etilize.burraq.unit.group;

import static com.etilize.burraq.unit.group.Group.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etilize.burraq.unit.base.AbstractMongoEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents group
 *
 * @author Nasir Ahmed
 * @since 1.0
 */
@Document(collection = COLLECTION_NAME)
public class Group extends AbstractMongoEntity<ObjectId> {

    public static final String COLLECTION_NAME = "groups";

    @NotBlank(message = "name is required")
    private final String name;

    private ObjectId baseUnitId;

    @NotBlank(message = "description is required")
    private final String description;

    /**
     * Group Constructor
     * @param name Name Of Group
     * @param description Description Of Group
     */
    @JsonCreator
    public Group(@JsonProperty("name") final String name,
            @JsonProperty("description") final String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    /**
     * get Id of baseUnit
     * @return the baseUnitId
     */
    public ObjectId getBaseUnitId() {
        return baseUnitId;
    }

    /**
     * set Id of baseUnit
     * @param baseUnitId the baseUnitId to set
     */
    public void setBaseUnitId(final ObjectId baseUnitId) {
        this.baseUnitId = baseUnitId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Group)) {
            return false;
        }
        final Group group = (Group) obj;
        return new EqualsBuilder() //
                .append(getName(), group.getName()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getName()) //
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("Id", getId()) //
                .append("Name", name) //
                .append("BaseUnitId", baseUnitId) //
                .append("Description", description) //
                .toString();
    }

}
