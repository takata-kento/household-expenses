package com.takata_kento.household_expenses.domain.valueobject;

public record Year(int value) {
    public Year {
        if (value < 1900 || value > 2100) {
            throw new IllegalArgumentException("Year must be between 1900 and 2100");
        }
    }
}
