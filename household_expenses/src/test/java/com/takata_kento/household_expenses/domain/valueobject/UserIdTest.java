package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.BDDAssertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserIdTest {

    @Test
    void testCreate() {
        // Given
        UUID value = UUID.randomUUID();

        // When
        UserId actual = new UserId(value);

        // Then
        then(actual.value()).isEqualTo(value);
    }

    @Test
    void testCreateWithNull() {
        // Given
        UUID value = null;

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() -> new UserId(value));

        // Then
        then(actual).hasMessage(UserId.class.getSimpleName() + " must not be null");
    }

    @Test
    void testToString() {
        // Given
        UUID value = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UserId userId = new UserId(value);

        // When
        String actual = userId.toString();

        // Then
        then(actual).isEqualTo(value.toString());
    }
}
