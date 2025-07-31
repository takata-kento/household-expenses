package com.takata_kento.household_expenses.domain.valueobject;

public record GroupName(String value) {
    public GroupName {
        if (value == null) {
            throw new IllegalArgumentException("GroupName cannot be null");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("GroupName cannot be empty");
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException("GroupName cannot be longer than 255 characters");
        }
    }
}
