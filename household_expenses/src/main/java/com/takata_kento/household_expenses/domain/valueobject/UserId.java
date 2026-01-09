package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record UserId(UUID value) implements UUIDValueObject {
    public UserId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
