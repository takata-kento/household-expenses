package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record BalanceEditHistoryId(UUID value) implements UUIDValueObject {
    public BalanceEditHistoryId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
