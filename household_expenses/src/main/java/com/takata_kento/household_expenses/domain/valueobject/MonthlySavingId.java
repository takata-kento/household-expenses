package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record MonthlySavingId(UUID value) implements UUIDValueObject {
    public MonthlySavingId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
