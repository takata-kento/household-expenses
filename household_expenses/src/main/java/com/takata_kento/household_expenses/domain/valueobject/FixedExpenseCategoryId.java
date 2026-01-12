package com.takata_kento.household_expenses.domain.valueobject;

public record FixedExpenseCategoryId(long value) {
    public FixedExpenseCategoryId {
        if (value <= 0) {
            throw new IllegalArgumentException("FixedExpenseCategoryId must be positive");
        }
    }
}
