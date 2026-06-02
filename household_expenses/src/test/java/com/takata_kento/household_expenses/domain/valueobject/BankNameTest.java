package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BankNameTest {

    @Test
    void testCreate() {
        // Given
        String value = "三菱UFJ銀行";

        // When
        BankName actual = new BankName(value);

        // Then
        assertThat(actual.value()).isEqualTo("三菱UFJ銀行");
    }

    @Test
    void testCreateWithNull() {
        // Given
        String value = null;

        // When & Then
        assertThatThrownBy(() -> new BankName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("BankName cannot be null");
    }

    @Test
    void testCreateWithEmpty() {
        // Given
        String value = "";

        // When & Then
        assertThatThrownBy(() -> new BankName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("BankName cannot be empty");
    }

    @Test
    void testCreateWithTooLong() {
        // Given
        String value = "a".repeat(256);

        // When & Then
        assertThatThrownBy(() -> new BankName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("BankName cannot be longer than 255 characters");
    }

    @Test
    void testCreateWithMaxLength() {
        // Given
        String value = "a".repeat(255);

        // When
        BankName actual = new BankName(value);

        // Then
        assertThat(actual.value()).isEqualTo(value);
    }

    @Test
    void testEquals() {
        // Given
        BankName bankName1 = new BankName("同じ銀行");
        BankName bankName2 = new BankName("同じ銀行");
        BankName bankName3 = new BankName("違う銀行");

        // When & Then
        assertThat(bankName1).isEqualTo(bankName2);
        assertThat(bankName1).isNotEqualTo(bankName3);
    }

    @Test
    void testHashCode() {
        // Given
        BankName bankName1 = new BankName("銀行名");
        BankName bankName2 = new BankName("銀行名");

        // When & Then
        assertThat(bankName1.hashCode()).isEqualTo(bankName2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        BankName bankName = new BankName("銀行名");

        // When
        String actual = bankName.toString();

        // Then
        assertThat(actual).contains("銀行名");
    }
}
