package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record DailyGroupTransactionId(UUID value) {
    public DailyGroupTransactionId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
