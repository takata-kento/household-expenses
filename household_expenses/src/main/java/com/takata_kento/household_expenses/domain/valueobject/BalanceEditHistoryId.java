package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record BalanceEditHistoryId(UUID value) {
    public BalanceEditHistoryId {
        if (value == null) {
            throw new IllegalArgumentException("BalanceEditHistoryId must not be null");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
