package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SequenceNumberTest {

    @Test
    void testCreate() {
        // Given
        int value = 5;

        // When
        SequenceNumber actual = new SequenceNumber(value);

        // Then
        assertThat(actual.value()).isEqualTo(5);
    }

    @Test
    void testCreateWithMinValue() {
        // Given
        int value = 1;

        // When
        SequenceNumber actual = new SequenceNumber(value);

        // Then
        assertThat(actual.value()).isEqualTo(1);
    }

    @Test
    void testCreateWithMaxValue() {
        // Given
        int value = Integer.MAX_VALUE;

        // When
        SequenceNumber actual = new SequenceNumber(value);

        // Then
        assertThat(actual.value()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void testCreateWithZero() {
        // Given
        int value = 0;

        // When & Then
        assertThatThrownBy(() -> new SequenceNumber(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("SequenceNumber must be positive");
    }

    @Test
    void testCreateWithNegative() {
        // Given
        int value = -1;

        // When & Then
        assertThatThrownBy(() -> new SequenceNumber(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("SequenceNumber must be positive");
    }

    @Test
    void testEquals() {
        // Given
        SequenceNumber sequenceNumber1 = new SequenceNumber(5);
        SequenceNumber sequenceNumber2 = new SequenceNumber(5);
        SequenceNumber sequenceNumber3 = new SequenceNumber(10);

        // When & Then
        assertThat(sequenceNumber1).isEqualTo(sequenceNumber2);
        assertThat(sequenceNumber1).isNotEqualTo(sequenceNumber3);
    }

    @Test
    void testHashCode() {
        // Given
        SequenceNumber sequenceNumber1 = new SequenceNumber(5);
        SequenceNumber sequenceNumber2 = new SequenceNumber(5);

        // When & Then
        assertThat(sequenceNumber1.hashCode()).isEqualTo(sequenceNumber2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        SequenceNumber sequenceNumber = new SequenceNumber(5);

        // When
        String actual = sequenceNumber.toString();

        // Then
        assertThat(actual).contains("5");
    }
}
