package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DescriptionTest {

    @Test
    void testCreate() {
        // Given
        String value = "テスト用の説明文";

        // When
        Description actual = new Description(value);

        // Then
        assertThat(actual.value()).isEqualTo("テスト用の説明文");
    }

    @Test
    void testCreateWithEmpty() {
        // Given
        String value = "";

        // When
        Description actual = new Description(value);

        // Then
        assertThat(actual.value()).isEqualTo("");
    }

    @Test
    void testCreateWithNull() {
        // Given
        String value = null;

        // When & Then
        assertThatThrownBy(() -> new Description(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Description cannot be null");
    }

    @Test
    void testCreateWithTooLong() {
        // Given
        String value = "a".repeat(256);

        // When & Then
        assertThatThrownBy(() -> new Description(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Description cannot be longer than 255 characters");
    }

    @Test
    void testCreateWithMaxLength() {
        // Given
        String value = "a".repeat(255);

        // When
        Description actual = new Description(value);

        // Then
        assertThat(actual.value()).isEqualTo(value);
    }

    @Test
    void testEquals() {
        // Given
        Description description1 = new Description("同じ説明");
        Description description2 = new Description("同じ説明");
        Description description3 = new Description("違う説明");

        // When & Then
        assertThat(description1).isEqualTo(description2);
        assertThat(description1).isNotEqualTo(description3);
    }

    @Test
    void testHashCode() {
        // Given
        Description description1 = new Description("説明文");
        Description description2 = new Description("説明文");

        // When & Then
        assertThat(description1.hashCode()).isEqualTo(description2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        Description description = new Description("説明文");

        // When
        String actual = description.toString();

        // Then
        assertThat(actual).contains("説明文");
    }
}
