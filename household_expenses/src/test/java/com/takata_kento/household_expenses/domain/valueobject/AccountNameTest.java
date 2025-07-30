package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AccountNameTest {

    @Test
    void testCreate() {
        // Given
        String value = "メイン口座";

        // When
        AccountName actual = new AccountName(value);

        // Then
        assertThat(actual.value()).isEqualTo("メイン口座");
    }

    @Test
    void testCreateWithNull() {
        // Given
        String value = null;

        // When & Then
        assertThatThrownBy(() -> new AccountName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("AccountName cannot be null");
    }

    @Test
    void testCreateWithEmpty() {
        // Given
        String value = "";

        // When & Then
        assertThatThrownBy(() -> new AccountName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("AccountName cannot be empty");
    }

    @Test
    void testCreateWithTooLong() {
        // Given
        String value = "a".repeat(256);

        // When & Then
        assertThatThrownBy(() -> new AccountName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("AccountName cannot be longer than 255 characters");
    }

    @Test
    void testCreateWithMaxLength() {
        // Given
        String value = "a".repeat(255);

        // When
        AccountName actual = new AccountName(value);

        // Then
        assertThat(actual.value()).isEqualTo(value);
    }

    @Test
    void testEquals() {
        // Given
        AccountName accountName1 = new AccountName("同じ口座名");
        AccountName accountName2 = new AccountName("同じ口座名");
        AccountName accountName3 = new AccountName("違う口座名");

        // When & Then
        assertThat(accountName1).isEqualTo(accountName2);
        assertThat(accountName1).isNotEqualTo(accountName3);
    }

    @Test
    void testHashCode() {
        // Given
        AccountName accountName1 = new AccountName("口座名");
        AccountName accountName2 = new AccountName("口座名");

        // When & Then
        assertThat(accountName1.hashCode()).isEqualTo(accountName2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        AccountName accountName = new AccountName("口座名");

        // When
        String actual = accountName.toString();

        // Then
        assertThat(actual).contains("口座名");
    }
}
