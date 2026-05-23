package com.takata_kento.household_expenses.domain.valueobject;

import java.util.regex.Pattern;

public record FinancialAccountId(String value) {
    private static final Pattern PATTERN = Pattern.compile("\\d{6,8}");

    public FinancialAccountId {
        if (value == null) {
            throw new IllegalArgumentException(FinancialAccountId.class.getSimpleName() + " must not be null");
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                FinancialAccountId.class.getSimpleName() + " must be 6-8 digit numeric string"
            );
        }
    }

    public String toString() {
        return value;
    }
}
