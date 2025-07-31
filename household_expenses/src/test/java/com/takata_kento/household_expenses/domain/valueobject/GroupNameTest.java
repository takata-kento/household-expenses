package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GroupNameTest {

    @Test
    void testCreate() {
        // Given
        String value = "家族グループ";

        // When
        GroupName actual = new GroupName(value);

        // Then
        assertThat(actual.value()).isEqualTo("家族グループ");
    }

    @Test
    void testCreateWithNull() {
        // Given
        String value = null;

        // When & Then
        assertThatThrownBy(() -> new GroupName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("GroupName cannot be null");
    }

    @Test
    void testCreateWithEmpty() {
        // Given
        String value = "";

        // When & Then
        assertThatThrownBy(() -> new GroupName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("GroupName cannot be empty");
    }

    @Test
    void testCreateWithTooLong() {
        // Given
        String value = "a".repeat(256);

        // When & Then
        assertThatThrownBy(() -> new GroupName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("GroupName cannot be longer than 255 characters");
    }

    @Test
    void testCreateWithMaxLength() {
        // Given
        String value = "a".repeat(255);

        // When
        GroupName actual = new GroupName(value);

        // Then
        assertThat(actual.value()).isEqualTo(value);
    }

    @Test
    void testEquals() {
        // Given
        GroupName groupName1 = new GroupName("同じグループ名");
        GroupName groupName2 = new GroupName("同じグループ名");
        GroupName groupName3 = new GroupName("違うグループ名");

        // When & Then
        assertThat(groupName1).isEqualTo(groupName2);
        assertThat(groupName1).isNotEqualTo(groupName3);
    }

    @Test
    void testHashCode() {
        // Given
        GroupName groupName1 = new GroupName("グループ名");
        GroupName groupName2 = new GroupName("グループ名");

        // When & Then
        assertThat(groupName1.hashCode()).isEqualTo(groupName2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        GroupName groupName = new GroupName("グループ名");

        // When
        String actual = groupName.toString();

        // Then
        assertThat(actual).contains("グループ名");
    }
}
