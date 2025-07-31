package com.takata_kento.household_expenses.domain.valueobject;

public record DailyLivingExpenseId(long value) {
    public DailyLivingExpenseId {
        if (value <= 0) {
            throw new IllegalArgumentException("DailyLivingExpenseId must be positive");
        }
    }
}
