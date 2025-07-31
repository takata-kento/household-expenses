package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DailyLivingExpenseIdTest {

    @Test
    void testCreate() {
        // Given
        long value = 123L;

        // When
        DailyLivingExpenseId actual = new DailyLivingExpenseId(value);

        // Then
        assertThat(actual.value()).isEqualTo(123L);
    }

    @Test
    void testCreateWithZero() {
        // Given
        long value = 0L;

        // When & Then
        assertThatThrownBy(() -> new DailyLivingExpenseId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("DailyLivingExpenseId must be positive");
    }

    @Test
    void testCreateWithNegative() {
        // Given
        long value = -1L;

        // When & Then
        assertThatThrownBy(() -> new DailyLivingExpenseId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("DailyLivingExpenseId must be positive");
    }

    @Test
    void testCreateWithMinPositive() {
        // Given
        long value = 1L;

        // When
        DailyLivingExpenseId actual = new DailyLivingExpenseId(value);

        // Then
        assertThat(actual.value()).isEqualTo(1L);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        long value = Long.MAX_VALUE;

        // When
        DailyLivingExpenseId actual = new DailyLivingExpenseId(value);

        // Then
        assertThat(actual.value()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void testEquals() {
        // Given
        DailyLivingExpenseId expenseId1 = new DailyLivingExpenseId(123L);
        DailyLivingExpenseId expenseId2 = new DailyLivingExpenseId(123L);
        DailyLivingExpenseId expenseId3 = new DailyLivingExpenseId(456L);

        // When & Then
        assertThat(expenseId1).isEqualTo(expenseId2);
        assertThat(expenseId1).isNotEqualTo(expenseId3);
    }

    @Test
    void testHashCode() {
        // Given
        DailyLivingExpenseId expenseId1 = new DailyLivingExpenseId(123L);
        DailyLivingExpenseId expenseId2 = new DailyLivingExpenseId(123L);

        // When & Then
        assertThat(expenseId1.hashCode()).isEqualTo(expenseId2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        DailyLivingExpenseId expenseId = new DailyLivingExpenseId(123L);

        // When
        String actual = expenseId.toString();

        // Then
        assertThat(actual).contains("123");
    }
}
