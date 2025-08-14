package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GroupInvitationIdTest {

    @Test
    void testConstructor() {
        // Given
        long expectedValue = 1L;

        // When
        GroupInvitationId actual = new GroupInvitationId(expectedValue);

        // Then
        assertThat(actual.value()).isEqualTo(expectedValue);
    }

    @Test
    void testConstructor_withValidValue() {
        // Given
        long validValue = 100L;

        // When
        GroupInvitationId actual = new GroupInvitationId(validValue);

        // Then
        assertThat(actual.value()).isEqualTo(validValue);
    }

    @Test
    void testConstructor_withZero_shouldThrowException() {
        // Given
        long invalidValue = 0L;

        // When / Then
        assertThatThrownBy(() -> new GroupInvitationId(invalidValue))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("GroupInvitationId must be positive");
    }

    @Test
    void testConstructor_withNegativeValue_shouldThrowException() {
        // Given
        long invalidValue = -1L;

        // When / Then
        assertThatThrownBy(() -> new GroupInvitationId(invalidValue))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("GroupInvitationId must be positive");
    }

    @Test
    void testEquals() {
        // Given
        GroupInvitationId id1 = new GroupInvitationId(1L);
        GroupInvitationId id2 = new GroupInvitationId(1L);
        GroupInvitationId id3 = new GroupInvitationId(2L);

        // When / Then
        assertThat(id1).isEqualTo(id2);
        assertThat(id1).isNotEqualTo(id3);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }
}
