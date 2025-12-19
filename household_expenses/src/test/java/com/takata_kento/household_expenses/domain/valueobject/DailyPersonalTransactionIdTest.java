package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.BDDAssertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class DailyPersonalTransactionIdTest {

    @Test
    void testCreate() {
        // Given
        UUID value = UUID.randomUUID();

        // When
        DailyPersonalTransactionId actual = new DailyPersonalTransactionId(value);

        // Then
        then(actual.value()).isEqualTo(value);
    }

    @Test
    void testCreateWithNull() {
        // Given
        UUID value = null;

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() -> new DailyPersonalTransactionId(value));

        // Then
        then(actual).hasMessage(DailyPersonalTransactionId.class.getSimpleName() + " must not be null");
    }

    @Test
    void testToString() {
        // Given
        UUID value = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        DailyPersonalTransactionId transactionId = new DailyPersonalTransactionId(value);

        // When
        String actual = transactionId.toString();

        // Then
        then(actual).isEqualTo(value.toString());
    }
}
