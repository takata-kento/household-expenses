package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserGroupIdTest {

    @Test
    void testCreate() {
        // Given
        UUID value = UUID.randomUUID();

        // When
        UserGroupId actual = new UserGroupId(value);

        // Then
        then(actual.value()).isEqualTo(value);
    }

    @Test
    void testCreateWithNull() {
        // Given
        UUID value = null;

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() -> new UserGroupId(value));

        // Then
        then(actual).hasMessage(UserGroupId.class.getSimpleName() + " must not be null");
    }

    @Test
    void testToString() {
        // Given
        UUID value = UUID.randomUUID();
        UserGroupId id = new UserGroupId(value);

        // When
        String actual = id.toString();

        // Then
        then(actual).isEqualTo(value.toString());
    }
}
