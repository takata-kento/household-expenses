package com.takata_kento.household_expenses.domain.valueobject;

public record CategoryName(String value) {
    public CategoryName {
        if (value == null) {
            throw new IllegalArgumentException("CategoryName cannot be null");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("CategoryName cannot be empty");
        }
        if (value.length() > 30) {
            throw new IllegalArgumentException("CategoryName cannot be longer than 30 characters");
        }
    }
}
