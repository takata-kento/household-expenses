package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class FinancialAccountIdTest {

    @Test
    void testCreate() {
        // Given
        UUID value = UUID.randomUUID();

        // When
        FinancialAccountId actual = new FinancialAccountId(value);

        // Then
        then(actual.value()).isEqualTo(value);
    }

    @Test
    void testCreateWithNull() {
        // Given
        UUID value = null;

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() -> new FinancialAccountId(value));

        // Then
        then(actual).hasMessage(FinancialAccountId.class.getSimpleName() + " must not be null");
    }

    @Test
    void testToString() {
        // Given
        UUID value = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        FinancialAccountId id = new FinancialAccountId(value);

        // When
        String actual = id.toString();

        // Then
        then(actual).isEqualTo(value.toString());
    }
}
