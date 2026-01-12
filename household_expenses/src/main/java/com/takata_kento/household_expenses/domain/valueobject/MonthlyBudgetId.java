package com.takata_kento.household_expenses.domain.valueobject;

public record MonthlyBudgetId(long value) {
    public MonthlyBudgetId {
        if (value <= 0) {
            throw new IllegalArgumentException("MonthlyBudgetId must be positive");
        }
    }
}
