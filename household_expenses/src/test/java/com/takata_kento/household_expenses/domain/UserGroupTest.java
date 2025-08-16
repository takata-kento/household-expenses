package com.takata_kento.household_expenses.domain;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class UserGroupTest {

    @Test
    void testConstructor() {
        // Given
        UserGroupId expectedId = new UserGroupId(1L);
        GroupName expectedGroupName = new GroupName("テストグループ");
        Day expectedMonthStartDay = new Day(25);
        UserId expectedCreatedByUserId = new UserId(100L);
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2025, 8, 7, 10, 0, 0);
        LocalDateTime expectedUpdatedAt = LocalDateTime.of(2025, 8, 7, 10, 0, 0);
        Integer expectedVersion = 1;

        // When
        UserGroup actual = new UserGroup(
            expectedId,
            expectedGroupName,
            expectedMonthStartDay,
            expectedCreatedByUserId,
            expectedCreatedAt,
            expectedUpdatedAt,
            expectedVersion
        );

        // Then
        assertThat(actual.id()).isEqualTo(expectedId);
        assertThat(actual.groupName()).isEqualTo(expectedGroupName);
        assertThat(actual.monthStartDay()).isEqualTo(expectedMonthStartDay);
        assertThat(actual.createdByUserId()).isEqualTo(expectedCreatedByUserId);
        assertThat(actual.createdAt()).isEqualTo(expectedCreatedAt);
        assertThat(actual.updatedAt()).isEqualTo(expectedUpdatedAt);
        assertThat(actual.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testCreate() {
        // Given
        GroupName expectedGroupName = new GroupName("新規グループ");
        Day expectedMonthStartDay = new Day(1);
        UserId expectedCreatedByUserId = new UserId(100L);

        // When
        UserGroup actual = UserGroup.create(expectedGroupName, expectedMonthStartDay, expectedCreatedByUserId);

        // Then
        assertThat(actual.id()).isNotNull();
        assertThat(actual.groupName()).isEqualTo(expectedGroupName);
        assertThat(actual.monthStartDay()).isEqualTo(expectedMonthStartDay);
        assertThat(actual.createdByUserId()).isEqualTo(expectedCreatedByUserId);
        assertThat(actual.createdAt()).isNotNull();
        assertThat(actual.updatedAt()).isNull();
        assertThat(actual.version()).isNull();
    }

    @Test
    void testUpdateGroupName() {
        // Given
        UserGroup userGroup = new UserGroup(
            new UserGroupId(1L),
            new GroupName("元のグループ名"),
            new Day(1),
            new UserId(100L),
            LocalDateTime.of(2025, 8, 7, 10, 0, 0),
            LocalDateTime.of(2025, 8, 7, 10, 0, 0),
            1
        );
        GroupName expectedNewGroupName = new GroupName("新しいグループ名");

        // When
        userGroup.updateGroupName(expectedNewGroupName);

        // Then
        assertThat(userGroup.groupName()).isEqualTo(expectedNewGroupName);
    }

    @Test
    void testUpdateMonthStartDay() {
        // Given
        UserGroup userGroup = new UserGroup(
            new UserGroupId(1L),
            new GroupName("テストグループ"),
            new Day(1),
            new UserId(100L),
            LocalDateTime.of(2025, 8, 7, 10, 0, 0),
            LocalDateTime.of(2025, 8, 7, 10, 0, 0),
            1
        );
        Day expectedNewMonthStartDay = new Day(25);

        // When
        userGroup.updateMonthStartDay(expectedNewMonthStartDay);

        // Then
        assertThat(userGroup.monthStartDay()).isEqualTo(expectedNewMonthStartDay);
    }

    @Test
    void testCanBeModifiedBy() {
        // Given
        UserId createdByUserId = new UserId(100L);
        UserGroup userGroup = new UserGroup(
            new UserGroupId(1L),
            new GroupName("テストグループ"),
            new Day(1),
            createdByUserId,
            LocalDateTime.of(2025, 8, 7, 10, 0, 0),
            LocalDateTime.of(2025, 8, 7, 10, 0, 0),
            1
        );

        // When
        boolean actualCanModify = userGroup.canBeModifiedBy(createdByUserId);
        boolean actualCannotModify = userGroup.canBeModifiedBy(new UserId(200L));

        // Then
        assertThat(actualCanModify).isTrue();
        assertThat(actualCannotModify).isFalse();
    }
}
