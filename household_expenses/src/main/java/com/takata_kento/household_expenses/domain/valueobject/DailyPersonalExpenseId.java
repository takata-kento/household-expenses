package com.takata_kento.household_expenses.domain.valueobject;

public record DailyPersonalExpenseId(long value) {
    public DailyPersonalExpenseId {
        if (value <= 0) {
            throw new IllegalArgumentException("DailyPersonalExpenseId must be positive");
        }
    }
}
