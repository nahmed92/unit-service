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

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;
import javax.validation.ConstraintViolationException;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.unit.group.Group;
import com.etilize.burraq.unit.group.GroupRepository;
import com.etilize.burraq.unit.test.AbstractIntegrationTest;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.querydsl.core.types.Predicate;
import org.springframework.dao.DuplicateKeyException;

@UsingDataSet(locations = "/datasets/groups/groups.bson")
public class GroupRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void shouldFindAllGroups() {
        final List<Group> groups = groupRepository.findAll();
        assertThat(groups, is(notNullValue()));
        assertThat(groups, hasSize(2));
        assertThat(groups.get(0).getName(), is("weight"));
        assertThat(groups.get(0).getDescription(), is("This is weight unit"));
        assertThat(groups.get(1).getName(), is("temperature"));
        assertThat(groups.get(1).getDescription(), is("This is Temperature unit"));
    }

    @Test(expected = DuplicateKeyException.class)
    public void shouldThrowDuplicateKeyExceptionWhenGroupNameAlreadyExists() {
        final Group group = new Group("weight", "This is weight unit");
        groupRepository.save(group);
    }

    @Test(expected = DuplicateKeyException.class)
    public void shouldThrowDuplicateKeyExceptionWhenGroupNameAlreadyExistsCaseInSensitively() {
        final Group group = new Group("WEIGHT", "This is weight unit");
        groupRepository.save(group);
    }

    @Test
    public void shouldFindGroupById() {
        final Group group = groupRepository.findOne(
                new ObjectId("53e9155b5ed24e4c38d60e3c"));
        assertThat(group.getName(), is("weight"));
        assertThat(group.getDescription(), is("This is weight unit"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/groups/group_after_delete.bson")
    public void shouldDeleteGroupById() {
        groupRepository.delete(new ObjectId("53e9155b5ed24e4c38d60e3c"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/groups/group_after_create.bson")
    public void shouldCreateNewGroup() {
        final Group group = new Group("pressure", "This is pressure group");
        groupRepository.save(group);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/groups/group_after_update.bson")
    public void shouldUpdateExistingGroup() {
        final Group group = new Group("distance", "This is distance group");
        group.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        groupRepository.save(group);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsNullAtCreation() {
        final Group group = new Group(null, "This is pressure group");
        groupRepository.save(group);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsNullAtUpdate() {
        final Group group = new Group(null, "This is distance group");
        group.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        groupRepository.save(group);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsEmptyAtCreation() {
        final Group group = new Group("", "This is pressure group");
        groupRepository.save(group);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenNameIsEmptyAtUpdate() {
        final Group group = new Group("", "This is distance group");
        group.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        groupRepository.save(group);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsNullAtCreation() {
        final Group group = new Group("wight", null);
        groupRepository.save(group);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsNullAtUpdate() {
        final Group group = new Group("weight", null);
        group.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        groupRepository.save(group);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsEmptyAtCreation() {
        final Group group = new Group("weight", "");
        groupRepository.save(group);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowExceptionWhenDescriptionIsEmptyAtUpdate() {
        final Group group = new Group("weight", "");
        group.setId(new ObjectId("53e9155b5ed24e4c38d60e3c"));
        groupRepository.save(group);
    }

    @Test
    public void shouldFindGroupByNameUsingQueryDslPredicate() {
        QGroup qGroup = new QGroup("name");
        Predicate predicate = qGroup.name.startsWith("weight");
        List<Group> group = (List<Group>) groupRepository.findAll(predicate);
        assertThat(group.size(), is(1));
        assertThat(group.get(0).getName(), is("weight"));
        assertThat(group.get(0).getDescription(), is("This is weight unit"));
    }

}
