package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record FinancialAccountId(UUID value) implements UUIDValueObject {
    public FinancialAccountId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
