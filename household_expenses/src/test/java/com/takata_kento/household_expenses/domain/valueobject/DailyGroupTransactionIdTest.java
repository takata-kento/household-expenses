package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class DailyGroupTransactionIdTest {

    @Test
    void testCreate() {
        // Given
        UUID value = UUID.randomUUID();

        // When
        DailyGroupTransactionId actual = new DailyGroupTransactionId(value);

        // Then
        assertThat(actual.value()).isEqualTo(value);
    }

    @Test
    void testCreateWithNull() {
        // Given
        UUID value = null;

        // When & Then
        assertThatThrownBy(() -> new DailyGroupTransactionId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(DailyGroupTransactionId.class.getSimpleName() + " must not be null");
    }

    @Test
    void testToString() {
        // Given
        UUID value = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        DailyGroupTransactionId id = new DailyGroupTransactionId(value);

        // When
        String actual = id.toString();

        // Then
        assertThat(actual).isEqualTo(value.toString());
    }
}
