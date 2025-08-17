package com.takata_kento.household_expenses.domain.valueobject;

public record Month(int value) {
    public Month {
        if (value < 1 || value > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
    }
}
