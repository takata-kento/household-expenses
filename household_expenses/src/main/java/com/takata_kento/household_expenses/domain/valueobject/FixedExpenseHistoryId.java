package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record FixedExpenseHistoryId(UUID value) implements UUIDValueObject {
    public FixedExpenseHistoryId {
        ValidateUtil.validUUID(value, getClass());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
