package com.takata_kento.household_expenses.domain.valueobject;

public record Day(int value) {
    public Day {
        if (value < 1 || value > 31) {
            throw new IllegalArgumentException("Day must be between 1 and 31");
        }
    }
}
