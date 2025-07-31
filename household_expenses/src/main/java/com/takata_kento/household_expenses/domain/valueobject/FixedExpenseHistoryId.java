package com.takata_kento.household_expenses.domain.valueobject;

public record FixedExpenseHistoryId(long value) {
    public FixedExpenseHistoryId {
        if (value <= 0) {
            throw new IllegalArgumentException("FixedExpenseHistoryId must be positive");
        }
    }
}
