package com.takata_kento.household_expenses.domain.valueobject;

public record LivingExpenseCategoryId(long value) {
    public LivingExpenseCategoryId {
        if (value <= 0) {
            throw new IllegalArgumentException("LivingExpenseCategoryId must be positive");
        }
    }
}
