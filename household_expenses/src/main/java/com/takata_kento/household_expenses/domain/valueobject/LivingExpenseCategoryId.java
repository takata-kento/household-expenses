package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record LivingExpenseCategoryId(UUID value) implements UUIDValueObject {
    public LivingExpenseCategoryId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
