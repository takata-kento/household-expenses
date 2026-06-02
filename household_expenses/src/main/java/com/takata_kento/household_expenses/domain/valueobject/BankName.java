package com.takata_kento.household_expenses.domain.valueobject;

public record BankName(String value) {
    public BankName {
        if (value == null) {
            throw new IllegalArgumentException("BankName cannot be null");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("BankName cannot be empty");
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException("BankName cannot be longer than 255 characters");
        }
    }
}
