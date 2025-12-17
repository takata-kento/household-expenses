package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

class ValidateUtil {

    static void validUUID(UUID value, Class<? extends UUIDValueObject> type) {
        if (value == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " must not be null");
        }
    }

    private ValidateUtil() {
        throw new AssertionError("Utility class can not be instance");
    }
}
