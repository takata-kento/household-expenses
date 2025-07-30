package com.takata_kento.household_expenses.domain.valueobject;

public record UserId(long value) {
    public UserId {
        if (value <= 0) {
            throw new IllegalArgumentException("UserId must be positive");
        }
    }
}
