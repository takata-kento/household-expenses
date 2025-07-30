package com.takata_kento.household_expenses.domain.valueobject;

public record Description(String value) {
    public Description {
        if (value == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException("Description cannot be longer than 255 characters");
        }
    }
}
