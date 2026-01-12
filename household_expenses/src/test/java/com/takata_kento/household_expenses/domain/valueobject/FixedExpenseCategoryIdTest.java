package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FixedExpenseCategoryIdTest {

    @Test
    void testCreate() {
        // Given
        long value = 123L;

        // When
        FixedExpenseCategoryId actual = new FixedExpenseCategoryId(value);

        // Then
        assertThat(actual.value()).isEqualTo(123L);
    }

    @Test
    void testCreateWithZero() {
        // Given
        long value = 0L;

        // When & Then
        assertThatThrownBy(() -> new FixedExpenseCategoryId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("FixedExpenseCategoryId must be positive");
    }

    @Test
    void testCreateWithNegative() {
        // Given
        long value = -1L;

        // When & Then
        assertThatThrownBy(() -> new FixedExpenseCategoryId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("FixedExpenseCategoryId must be positive");
    }

    @Test
    void testCreateWithMinPositive() {
        // Given
        long value = 1L;

        // When
        FixedExpenseCategoryId actual = new FixedExpenseCategoryId(value);

        // Then
        assertThat(actual.value()).isEqualTo(1L);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        long value = Long.MAX_VALUE;

        // When
        FixedExpenseCategoryId actual = new FixedExpenseCategoryId(value);

        // Then
        assertThat(actual.value()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void testEquals() {
        // Given
        FixedExpenseCategoryId categoryId1 = new FixedExpenseCategoryId(123L);
        FixedExpenseCategoryId categoryId2 = new FixedExpenseCategoryId(123L);
        FixedExpenseCategoryId categoryId3 = new FixedExpenseCategoryId(456L);

        // When & Then
        assertThat(categoryId1).isEqualTo(categoryId2);
        assertThat(categoryId1).isNotEqualTo(categoryId3);
    }

    @Test
    void testHashCode() {
        // Given
        FixedExpenseCategoryId categoryId1 = new FixedExpenseCategoryId(123L);
        FixedExpenseCategoryId categoryId2 = new FixedExpenseCategoryId(123L);

        // When & Then
        assertThat(categoryId1.hashCode()).isEqualTo(categoryId2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        FixedExpenseCategoryId categoryId = new FixedExpenseCategoryId(123L);

        // When
        String actual = categoryId.toString();

        // Then
        assertThat(actual).contains("123");
    }
}
