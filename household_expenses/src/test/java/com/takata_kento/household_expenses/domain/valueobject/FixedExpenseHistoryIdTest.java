package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FixedExpenseHistoryIdTest {

    @Test
    void testCreate() {
        // Given
        long value = 123L;

        // When
        FixedExpenseHistoryId actual = new FixedExpenseHistoryId(value);

        // Then
        assertThat(actual.value()).isEqualTo(123L);
    }

    @Test
    void testCreateWithZero() {
        // Given
        long value = 0L;

        // When & Then
        assertThatThrownBy(() -> new FixedExpenseHistoryId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("FixedExpenseHistoryId must be positive");
    }

    @Test
    void testCreateWithNegative() {
        // Given
        long value = -1L;

        // When & Then
        assertThatThrownBy(() -> new FixedExpenseHistoryId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("FixedExpenseHistoryId must be positive");
    }

    @Test
    void testCreateWithMinPositive() {
        // Given
        long value = 1L;

        // When
        FixedExpenseHistoryId actual = new FixedExpenseHistoryId(value);

        // Then
        assertThat(actual.value()).isEqualTo(1L);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        long value = Long.MAX_VALUE;

        // When
        FixedExpenseHistoryId actual = new FixedExpenseHistoryId(value);

        // Then
        assertThat(actual.value()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void testEquals() {
        // Given
        FixedExpenseHistoryId historyId1 = new FixedExpenseHistoryId(123L);
        FixedExpenseHistoryId historyId2 = new FixedExpenseHistoryId(123L);
        FixedExpenseHistoryId historyId3 = new FixedExpenseHistoryId(456L);

        // When & Then
        assertThat(historyId1).isEqualTo(historyId2);
        assertThat(historyId1).isNotEqualTo(historyId3);
    }

    @Test
    void testHashCode() {
        // Given
        FixedExpenseHistoryId historyId1 = new FixedExpenseHistoryId(123L);
        FixedExpenseHistoryId historyId2 = new FixedExpenseHistoryId(123L);

        // When & Then
        assertThat(historyId1.hashCode()).isEqualTo(historyId2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        FixedExpenseHistoryId historyId = new FixedExpenseHistoryId(123L);

        // When
        String actual = historyId.toString();

        // Then
        assertThat(actual).contains("123");
    }
}
