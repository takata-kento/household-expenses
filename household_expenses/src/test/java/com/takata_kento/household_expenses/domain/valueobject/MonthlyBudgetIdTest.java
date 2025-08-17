package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MonthlyBudgetIdTest {

    @Test
    void testCreate() {
        // Given
        long value = 123L;

        // When
        MonthlyBudgetId actual = new MonthlyBudgetId(value);

        // Then
        assertThat(actual.value()).isEqualTo(123L);
    }

    @Test
    void testCreateWithZero() {
        // Given
        long value = 0L;

        // When & Then
        assertThatThrownBy(() -> new MonthlyBudgetId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("MonthlyBudgetId must be positive");
    }

    @Test
    void testCreateWithNegative() {
        // Given
        long value = -1L;

        // When & Then
        assertThatThrownBy(() -> new MonthlyBudgetId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("MonthlyBudgetId must be positive");
    }

    @Test
    void testCreateWithMinPositive() {
        // Given
        long value = 1L;

        // When
        MonthlyBudgetId actual = new MonthlyBudgetId(value);

        // Then
        assertThat(actual.value()).isEqualTo(1L);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        long value = Long.MAX_VALUE;

        // When
        MonthlyBudgetId actual = new MonthlyBudgetId(value);

        // Then
        assertThat(actual.value()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void testEquals() {
        // Given
        MonthlyBudgetId monthlyBudgetId1 = new MonthlyBudgetId(123L);
        MonthlyBudgetId monthlyBudgetId2 = new MonthlyBudgetId(123L);
        MonthlyBudgetId monthlyBudgetId3 = new MonthlyBudgetId(456L);

        // When & Then
        assertThat(monthlyBudgetId1).isEqualTo(monthlyBudgetId2);
        assertThat(monthlyBudgetId1).isNotEqualTo(monthlyBudgetId3);
    }

    @Test
    void testHashCode() {
        // Given
        MonthlyBudgetId monthlyBudgetId1 = new MonthlyBudgetId(123L);
        MonthlyBudgetId monthlyBudgetId2 = new MonthlyBudgetId(123L);

        // When & Then
        assertThat(monthlyBudgetId1.hashCode()).isEqualTo(monthlyBudgetId2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        MonthlyBudgetId monthlyBudgetId = new MonthlyBudgetId(123L);

        // When
        String actual = monthlyBudgetId.toString();

        // Then
        assertThat(actual).contains("123");
    }
}
