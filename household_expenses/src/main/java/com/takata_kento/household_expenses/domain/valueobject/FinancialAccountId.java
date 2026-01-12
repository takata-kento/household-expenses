package com.takata_kento.household_expenses.domain.valueobject;

public record FinancialAccountId(long value) {
    public FinancialAccountId {
        if (value <= 0) {
            throw new IllegalArgumentException("FinancialAccountId must be positive");
        }
    }
}
