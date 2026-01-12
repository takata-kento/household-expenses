package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserGroupIdTest {

    @Test
    void testCreate() {
        // Given
        long value = 123L;

        // When
        UserGroupId actual = new UserGroupId(value);

        // Then
        assertThat(actual.value()).isEqualTo(123L);
    }

    @Test
    void testCreateWithZero() {
        // Given
        long value = 0L;

        // When & Then
        assertThatThrownBy(() -> new UserGroupId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("UserGroupId must be positive");
    }

    @Test
    void testCreateWithNegative() {
        // Given
        long value = -1L;

        // When & Then
        assertThatThrownBy(() -> new UserGroupId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("UserGroupId must be positive");
    }

    @Test
    void testCreateWithMinPositive() {
        // Given
        long value = 1L;

        // When
        UserGroupId actual = new UserGroupId(value);

        // Then
        assertThat(actual.value()).isEqualTo(1L);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        long value = Long.MAX_VALUE;

        // When
        UserGroupId actual = new UserGroupId(value);

        // Then
        assertThat(actual.value()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void testEquals() {
        // Given
        UserGroupId userGroupId1 = new UserGroupId(123L);
        UserGroupId userGroupId2 = new UserGroupId(123L);
        UserGroupId userGroupId3 = new UserGroupId(456L);

        // When & Then
        assertThat(userGroupId1).isEqualTo(userGroupId2);
        assertThat(userGroupId1).isNotEqualTo(userGroupId3);
    }

    @Test
    void testHashCode() {
        // Given
        UserGroupId userGroupId1 = new UserGroupId(123L);
        UserGroupId userGroupId2 = new UserGroupId(123L);

        // When & Then
        assertThat(userGroupId1.hashCode()).isEqualTo(userGroupId2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        UserGroupId userGroupId = new UserGroupId(123L);

        // When
        String actual = userGroupId.toString();

        // Then
        assertThat(actual).contains("123");
    }
}
