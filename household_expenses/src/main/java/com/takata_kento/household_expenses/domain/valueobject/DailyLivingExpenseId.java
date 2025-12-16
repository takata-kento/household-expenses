package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record DailyLivingExpenseId(UUID value) implements UUIDValueObject {
    public DailyLivingExpenseId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
