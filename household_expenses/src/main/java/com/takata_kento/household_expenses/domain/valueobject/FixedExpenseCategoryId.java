package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record FixedExpenseCategoryId(UUID value) implements UUIDValueObject {
    public FixedExpenseCategoryId {
        ValidateUtil.validUUID(value, getClass());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
