package com.takata_kento.household_expenses.domain.valueobject;

public record SequenceNumber(int value) {
    public SequenceNumber {
        if (value <= 0) {
            throw new IllegalArgumentException("SequenceNumber must be positive");
        }
    }
}
