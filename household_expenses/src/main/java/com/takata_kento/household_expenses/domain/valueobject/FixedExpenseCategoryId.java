package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record FixedExpenseCategoryId(UUID value) {
    public FixedExpenseCategoryId {
        if (value == null) {
            throw new IllegalArgumentException("FixedExpenseCategoryId must not be null");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
