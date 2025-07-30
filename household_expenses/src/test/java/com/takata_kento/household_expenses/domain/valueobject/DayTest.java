package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DayTest {

    @Test
    void testCreate() {
        // Given
        int value = 15;

        // When
        Day actual = new Day(value);

        // Then
        assertThat(actual.value()).isEqualTo(15);
    }

    @Test
    void testCreateWithMinValue() {
        // Given
        int value = 1;

        // When
        Day actual = new Day(value);

        // Then
        assertThat(actual.value()).isEqualTo(1);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        int value = 31;

        // When
        Day actual = new Day(value);

        // Then
        assertThat(actual.value()).isEqualTo(31);
    }

    @Test
    void testCreateWithZero() {
        // Given
        int value = 0;

        // When & Then
        assertThatThrownBy(() -> new Day(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Day must be between 1 and 31");
    }

    @Test
    void testCreateWithNegative() {
        // Given
        int value = -1;

        // When & Then
        assertThatThrownBy(() -> new Day(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Day must be between 1 and 31");
    }

    @Test
    void testCreateWithTooLarge() {
        // Given
        int value = 32;

        // When & Then
        assertThatThrownBy(() -> new Day(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Day must be between 1 and 31");
    }

    @Test
    void testEquals() {
        // Given
        Day day1 = new Day(15);
        Day day2 = new Day(15);
        Day day3 = new Day(20);

        // When & Then
        assertThat(day1).isEqualTo(day2);
        assertThat(day1).isNotEqualTo(day3);
    }

    @Test
    void testHashCode() {
        // Given
        Day day1 = new Day(15);
        Day day2 = new Day(15);

        // When & Then
        assertThat(day1.hashCode()).isEqualTo(day2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        Day day = new Day(15);

        // When
        String actual = day.toString();

        // Then
        assertThat(actual).contains("15");
    }
}
