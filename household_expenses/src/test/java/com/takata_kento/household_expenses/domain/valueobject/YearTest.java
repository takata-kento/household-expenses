package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class YearTest {

    @Test
    void testCreate() {
        // Given
        int expectedYear = 2024;

        // When
        Year actual = new Year(expectedYear);

        // Then
        assertThat(actual.value()).isEqualTo(expectedYear);
    }

    @Test
    void testCreateWithMinValue() {
        // Given
        int expectedYear = 1900;

        // When
        Year actual = new Year(expectedYear);

        // Then
        assertThat(actual.value()).isEqualTo(expectedYear);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        int expectedYear = 2100;

        // When
        Year actual = new Year(expectedYear);

        // Then
        assertThat(actual.value()).isEqualTo(expectedYear);
    }

    @Test
    void testCreateWithTooSmall() {
        // Given
        int invalidYear = 1899;

        // When & Then
        assertThatThrownBy(() -> new Year(invalidYear))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Year must be between 1900 and 2100");
    }

    @Test
    void testCreateWithTooLarge() {
        // Given
        int invalidYear = 2101;

        // When & Then
        assertThatThrownBy(() -> new Year(invalidYear))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Year must be between 1900 and 2100");
    }

    @Test
    void testEquals() {
        // Given
        Year year1 = new Year(2024);
        Year year2 = new Year(2024);
        Year year3 = new Year(2025);

        // When & Then
        assertThat(year1).isEqualTo(year2);
        assertThat(year1).isNotEqualTo(year3);
    }

    @Test
    void testHashCode() {
        // Given
        Year year1 = new Year(2024);
        Year year2 = new Year(2024);

        // When & Then
        assertThat(year1.hashCode()).isEqualTo(year2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        Year year = new Year(2024);

        // When
        String actual = year.toString();

        // Then
        assertThat(actual).contains("2024");
    }
}
