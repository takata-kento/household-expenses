package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MonthTest {

    @Test
    void testCreate() {
        // Given
        int expectedMonth = 6;

        // When
        Month actual = new Month(expectedMonth);

        // Then
        assertThat(actual.value()).isEqualTo(expectedMonth);
    }

    @Test
    void testCreateWithMinValue() {
        // Given
        int expectedMonth = 1;

        // When
        Month actual = new Month(expectedMonth);

        // Then
        assertThat(actual.value()).isEqualTo(expectedMonth);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        int expectedMonth = 12;

        // When
        Month actual = new Month(expectedMonth);

        // Then
        assertThat(actual.value()).isEqualTo(expectedMonth);
    }

    @Test
    void testCreateWithZero() {
        // Given
        int invalidMonth = 0;

        // When & Then
        assertThatThrownBy(() -> new Month(invalidMonth))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Month must be between 1 and 12");
    }

    @Test
    void testCreateWithTooLarge() {
        // Given
        int invalidMonth = 13;

        // When & Then
        assertThatThrownBy(() -> new Month(invalidMonth))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Month must be between 1 and 12");
    }

    @Test
    void testEquals() {
        // Given
        Month month1 = new Month(6);
        Month month2 = new Month(6);
        Month month3 = new Month(7);

        // When & Then
        assertThat(month1).isEqualTo(month2);
        assertThat(month1).isNotEqualTo(month3);
    }

    @Test
    void testHashCode() {
        // Given
        Month month1 = new Month(6);
        Month month2 = new Month(6);

        // When & Then
        assertThat(month1.hashCode()).isEqualTo(month2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        Month month = new Month(6);

        // When
        String actual = month.toString();

        // Then
        assertThat(actual).contains("6");
    }
}
