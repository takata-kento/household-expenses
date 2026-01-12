package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LivingExpenseCategoryIdTest {

    @Test
    void testCreate() {
        // Given
        long value = 123L;

        // When
        LivingExpenseCategoryId actual = new LivingExpenseCategoryId(value);

        // Then
        assertThat(actual.value()).isEqualTo(123L);
    }

    @Test
    void testCreateWithZero() {
        // Given
        long value = 0L;

        // When & Then
        assertThatThrownBy(() -> new LivingExpenseCategoryId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("LivingExpenseCategoryId must be positive");
    }

    @Test
    void testCreateWithNegative() {
        // Given
        long value = -1L;

        // When & Then
        assertThatThrownBy(() -> new LivingExpenseCategoryId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("LivingExpenseCategoryId must be positive");
    }

    @Test
    void testCreateWithMinPositive() {
        // Given
        long value = 1L;

        // When
        LivingExpenseCategoryId actual = new LivingExpenseCategoryId(value);

        // Then
        assertThat(actual.value()).isEqualTo(1L);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        long value = Long.MAX_VALUE;

        // When
        LivingExpenseCategoryId actual = new LivingExpenseCategoryId(value);

        // Then
        assertThat(actual.value()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void testEquals() {
        // Given
        LivingExpenseCategoryId categoryId1 = new LivingExpenseCategoryId(123L);
        LivingExpenseCategoryId categoryId2 = new LivingExpenseCategoryId(123L);
        LivingExpenseCategoryId categoryId3 = new LivingExpenseCategoryId(456L);

        // When & Then
        assertThat(categoryId1).isEqualTo(categoryId2);
        assertThat(categoryId1).isNotEqualTo(categoryId3);
    }

    @Test
    void testHashCode() {
        // Given
        LivingExpenseCategoryId categoryId1 = new LivingExpenseCategoryId(123L);
        LivingExpenseCategoryId categoryId2 = new LivingExpenseCategoryId(123L);

        // When & Then
        assertThat(categoryId1.hashCode()).isEqualTo(categoryId2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        LivingExpenseCategoryId categoryId = new LivingExpenseCategoryId(123L);

        // When
        String actual = categoryId.toString();

        // Then
        assertThat(actual).contains("123");
    }
}
