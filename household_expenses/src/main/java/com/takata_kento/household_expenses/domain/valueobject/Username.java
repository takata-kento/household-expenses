package com.takata_kento.household_expenses.domain.valueobject;

public record Username(String value) {
    private static final int MAX_USERNAME_LENGTH = 50;

    public Username {
        if (value == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (value.length() > MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException("Username cannot be longer than " + MAX_USERNAME_LENGTH + " characters");
        }
    }
}
