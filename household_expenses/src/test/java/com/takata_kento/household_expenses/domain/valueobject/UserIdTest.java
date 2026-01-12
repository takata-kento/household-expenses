package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserIdTest {

    @Test
    void testCreate() {
        // Given
        long value = 123L;

        // When
        UserId actual = new UserId(value);

        // Then
        assertThat(actual.value()).isEqualTo(123L);
    }

    @Test
    void testCreateWithZero() {
        // Given
        long value = 0L;

        // When & Then
        assertThatThrownBy(() -> new UserId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("UserId must be positive");
    }

    @Test
    void testCreateWithNegative() {
        // Given
        long value = -1L;

        // When & Then
        assertThatThrownBy(() -> new UserId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("UserId must be positive");
    }

    @Test
    void testCreateWithMinPositive() {
        // Given
        long value = 1L;

        // When
        UserId actual = new UserId(value);

        // Then
        assertThat(actual.value()).isEqualTo(1L);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        long value = Long.MAX_VALUE;

        // When
        UserId actual = new UserId(value);

        // Then
        assertThat(actual.value()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void testEquals() {
        // Given
        UserId userId1 = new UserId(123L);
        UserId userId2 = new UserId(123L);
        UserId userId3 = new UserId(456L);

        // When & Then
        assertThat(userId1).isEqualTo(userId2);
        assertThat(userId1).isNotEqualTo(userId3);
    }

    @Test
    void testHashCode() {
        // Given
        UserId userId1 = new UserId(123L);
        UserId userId2 = new UserId(123L);

        // When & Then
        assertThat(userId1.hashCode()).isEqualTo(userId2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        UserId userId = new UserId(123L);

        // When
        String actual = userId.toString();

        // Then
        assertThat(actual).contains("123");
    }
}
