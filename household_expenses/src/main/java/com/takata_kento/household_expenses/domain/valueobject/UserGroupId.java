package com.takata_kento.household_expenses.domain.valueobject;

public record UserGroupId(long value) {
    public UserGroupId {
        if (value <= 0) {
            throw new IllegalArgumentException("UserGroupId must be positive");
        }
    }
}
