package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class FixedExpenseHistoryIdTest {

    @Test
    void testCreate() {
        // Given
        UUID value = UUID.randomUUID();

        // When
        FixedExpenseHistoryId actual = new FixedExpenseHistoryId(value);

        // Then
        assertThat(actual.value()).isEqualTo(value);
    }

    @Test
    void testCreateWithNull() {
        // Given
        UUID value = null;

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() -> new FixedExpenseHistoryId(value));

        // Then
        assertThat(actual).hasMessage(FixedExpenseHistoryId.class.getSimpleName() + " must not be null");
    }

    @Test
    void testToString() {
        // Given
        UUID value = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        FixedExpenseHistoryId historyId = new FixedExpenseHistoryId(value);

        // When
        String actual = historyId.toString();

        // Then
        assertThat(actual).contains(value.toString());
    }
}
