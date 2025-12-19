package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record DailyPersonalTransactionId(UUID value) implements UUIDValueObject {
    public DailyPersonalTransactionId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
