package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record DailyPersonalExpenseId(UUID value) implements UUIDValueObject {
    public DailyPersonalExpenseId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
