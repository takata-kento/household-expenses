package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.BDDAssertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FinancialAccountIdTest {

    @ParameterizedTest
    @ValueSource(strings = { "123456", "1234567", "12345678", "0001234", "00000000" })
    void testCreateWithValidValue(String value) {
        // Given
        String expected = value;

        // When
        FinancialAccountId actual = new FinancialAccountId(value);

        // Then
        then(actual.value()).isEqualTo(expected);
    }

    @Test
    void testCreateWithNull() {
        // Given
        String value = null;

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() -> new FinancialAccountId(value));

        // Then
        then(actual).hasMessage(FinancialAccountId.class.getSimpleName() + " must not be null");
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "12345", "123456789", "12345a", " 123456", "123456 ", "abcdef", "-123456" })
    void testCreateWithInvalidValue(String value) {
        // Given
        String invalidValue = value;

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() -> new FinancialAccountId(invalidValue));

        // Then
        then(actual).hasMessage(FinancialAccountId.class.getSimpleName() + " must be 6-8 digit numeric string");
    }

    @Test
    void testToString() {
        // Given
        String value = "1234567";
        FinancialAccountId financialAccountId = new FinancialAccountId(value);

        // When
        String actual = financialAccountId.toString();

        // Then
        then(actual).isEqualTo(value);
    }
}
