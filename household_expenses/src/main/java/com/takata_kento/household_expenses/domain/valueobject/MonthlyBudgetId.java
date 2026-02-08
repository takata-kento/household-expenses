package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record MonthlyBudgetId(UUID value) implements UUIDValueObject {
    public MonthlyBudgetId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
