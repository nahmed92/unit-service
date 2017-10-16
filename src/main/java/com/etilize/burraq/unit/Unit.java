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

package com.etilize.burraq.unit;

import static com.etilize.burraq.unit.Unit.*;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etilize.burraq.unit.base.AbstractMongoEntity;
import com.etilize.burraq.unit.validator.Formula;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents Unit
 *
 * @author Nasir Ahmed
 * @since 1.0
 */
@Document(collection = COLLECTION_NAME)
public class Unit extends AbstractMongoEntity<ObjectId> {

    public static final String COLLECTION_NAME = "units";

    @NotBlank(message = "name is required")
    @Indexed(unique = true)
    private final String name;

    @NotNull(message = "groupId is required")
    private final ObjectId groupId;

    private final boolean isBaseUnit;

    private MetricSystem metricSystem = MetricSystem.NONE;

    @Formula
    private String formula = "[value]";

    @NotBlank(message = "description is required")
    private final String description;

    /**
     * Unit Constructor
     * @param name Name of Unit
     * @param groupId Unit GroupId
     * @param isBaseUnit Base unit of Unit
     * @param description Unit description
     */
    @JsonCreator
    public Unit(@JsonProperty("name") final String name,
            @JsonProperty("groupId") final ObjectId groupId,
            @JsonProperty("isBaseUnit") final boolean isBaseUnit,
            @JsonProperty("description") final String description) {
        this.name = name;
        this.groupId = groupId;
        this.isBaseUnit = isBaseUnit;
        this.description = description;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the groupId
     */
    public ObjectId getGroupId() {
        return groupId;
    }

    /**
     * Annotation JsonProperty to access field level
     * property value
     * @return the isBaseUnit
     */
    @JsonProperty("isBaseUnit")
    public boolean isBaseUnit() {
        return isBaseUnit;
    }

    /**
    * Metric System (NONE,CGS,MKS,MTS)
    * @param metricSystem unit metic System
    */
    public void setMetricSystem(final MetricSystem metricSystem) {
        this.metricSystem = metricSystem == null ? MetricSystem.NONE : metricSystem;
    }

    /**
     * @return metricSystem
     */
    public MetricSystem getMetricSystem() {
        return metricSystem;
    }

    /**
     * Set unit formula
     * @param formula It's the formula to convert value
     */
    public void setFormula(final String formula) {
        this.formula = StringUtils.isBlank(formula) ? "[value]" : formula;
    }

    /**
     * @return formula
     */
    public String getFormula() {
        return formula;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getName()) //
                .hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Unit)) {
            return false;
        }
        final Unit unit = (Unit) obj;
        return new EqualsBuilder() //
                .append(getName(), unit.getName()) //
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("Id", getId()) //
                .append("Name", name) //
                .append("GroupId", groupId) //
                .append("IsBaseUnit", isBaseUnit) //
                .append("MetricSystem", metricSystem) //
                .append("Description", description) //
                .toString();
    }

}
