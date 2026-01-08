package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record FinancialAccountId(UUID value) {
    public FinancialAccountId {
        if (value == null) {
            throw new IllegalArgumentException("FinancialAccountId must not be null");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
