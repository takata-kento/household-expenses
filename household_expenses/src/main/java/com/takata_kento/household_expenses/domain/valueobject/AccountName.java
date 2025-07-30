package com.takata_kento.household_expenses.domain.valueobject;

public record AccountName(String value) {
    public AccountName {
        if (value == null) {
            throw new IllegalArgumentException("AccountName cannot be null");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("AccountName cannot be empty");
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException("AccountName cannot be longer than 255 characters");
        }
    }
}
