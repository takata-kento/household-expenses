package com.takata_kento.household_expenses.domain.valueobject;

public record Username(String value) {
    
    public Username {
        if (value == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("Username cannot be longer than 50 characters");
        }
    }
}
