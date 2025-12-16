package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.BDDAssertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class DailyLivingExpenseIdTest {

    @Test
    void testCreate() {
        // Given
        UUID value = UUID.randomUUID();

        // When
        DailyLivingExpenseId actual = new DailyLivingExpenseId(value);

        // Then
        then(actual.value()).isEqualTo(value);
    }

    @Test
    void testCreateWithNull() {
        // Given
        UUID value = null;

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() -> new DailyLivingExpenseId(value));

        // Then
        then(actual).hasMessage(DailyLivingExpenseId.class.getSimpleName() + " must not be null or empty");
    }

    @Test
    void testToString() {
        // Given
        UUID value = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        DailyLivingExpenseId expenseId = new DailyLivingExpenseId(value);

        // When
        String actual = expenseId.toString();

        // Then
        then(actual).isEqualTo(value);
    }
}
