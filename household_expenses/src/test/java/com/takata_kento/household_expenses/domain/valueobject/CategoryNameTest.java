package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CategoryNameTest {

    @Test
    void testCreate() {
        // Given
        String value = "食費";

        // When
        CategoryName actual = new CategoryName(value);

        // Then
        assertThat(actual.value()).isEqualTo("食費");
    }

    @Test
    void testCreateWithNull() {
        // Given
        String value = null;

        // When & Then
        assertThatThrownBy(() -> new CategoryName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("CategoryName cannot be null");
    }

    @Test
    void testCreateWithEmpty() {
        // Given
        String value = "";

        // When & Then
        assertThatThrownBy(() -> new CategoryName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("CategoryName cannot be empty");
    }

    @Test
    void testCreateWithTooLong() {
        // Given
        String value = "a".repeat(31);

        // When & Then
        assertThatThrownBy(() -> new CategoryName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("CategoryName cannot be longer than 30 characters");
    }

    @Test
    void testCreateWithMaxLength() {
        // Given
        String value = "a".repeat(30);

        // When
        CategoryName actual = new CategoryName(value);

        // Then
        assertThat(actual.value()).isEqualTo(value);
    }

    @Test
    void testEquals() {
        // Given
        CategoryName categoryName1 = new CategoryName("食費");
        CategoryName categoryName2 = new CategoryName("食費");
        CategoryName categoryName3 = new CategoryName("交通費");

        // When & Then
        assertThat(categoryName1).isEqualTo(categoryName2);
        assertThat(categoryName1).isNotEqualTo(categoryName3);
    }

    @Test
    void testHashCode() {
        // Given
        CategoryName categoryName1 = new CategoryName("食費");
        CategoryName categoryName2 = new CategoryName("食費");

        // When & Then
        assertThat(categoryName1.hashCode()).isEqualTo(categoryName2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        CategoryName categoryName = new CategoryName("食費");

        // When
        String actual = categoryName.toString();

        // Then
        assertThat(actual).contains("食費");
    }
}
