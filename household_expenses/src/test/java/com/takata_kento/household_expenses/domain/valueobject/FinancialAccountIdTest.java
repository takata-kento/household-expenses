package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FinancialAccountIdTest {

    @Test
    void testCreate() {
        // Given
        long value = 123L;

        // When
        FinancialAccountId actual = new FinancialAccountId(value);

        // Then
        assertThat(actual.value()).isEqualTo(123L);
    }

    @Test
    void testCreateWithZero() {
        // Given
        long value = 0L;

        // When & Then
        assertThatThrownBy(() -> new FinancialAccountId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("FinancialAccountId must be positive");
    }

    @Test
    void testCreateWithNegative() {
        // Given
        long value = -1L;

        // When & Then
        assertThatThrownBy(() -> new FinancialAccountId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("FinancialAccountId must be positive");
    }

    @Test
    void testCreateWithMinPositive() {
        // Given
        long value = 1L;

        // When
        FinancialAccountId actual = new FinancialAccountId(value);

        // Then
        assertThat(actual.value()).isEqualTo(1L);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        long value = Long.MAX_VALUE;

        // When
        FinancialAccountId actual = new FinancialAccountId(value);

        // Then
        assertThat(actual.value()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void testEquals() {
        // Given
        FinancialAccountId financialAccountId1 = new FinancialAccountId(123L);
        FinancialAccountId financialAccountId2 = new FinancialAccountId(123L);
        FinancialAccountId financialAccountId3 = new FinancialAccountId(456L);

        // When & Then
        assertThat(financialAccountId1).isEqualTo(financialAccountId2);
        assertThat(financialAccountId1).isNotEqualTo(financialAccountId3);
    }

    @Test
    void testHashCode() {
        // Given
        FinancialAccountId financialAccountId1 = new FinancialAccountId(123L);
        FinancialAccountId financialAccountId2 = new FinancialAccountId(123L);

        // When & Then
        assertThat(financialAccountId1.hashCode()).isEqualTo(financialAccountId2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        FinancialAccountId financialAccountId = new FinancialAccountId(123L);

        // When
        String actual = financialAccountId.toString();

        // Then
        assertThat(actual).contains("123");
    }
}
