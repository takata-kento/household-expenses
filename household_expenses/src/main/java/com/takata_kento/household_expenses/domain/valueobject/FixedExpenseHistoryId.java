package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record FixedExpenseHistoryId(UUID value) {
    public FixedExpenseHistoryId {
        if (value == null) {
            throw new IllegalArgumentException("FixedExpenseHistoryId must not be null");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
