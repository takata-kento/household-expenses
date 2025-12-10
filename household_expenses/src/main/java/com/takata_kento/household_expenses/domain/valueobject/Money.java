package com.takata_kento.household_expenses.domain.valueobject;

public record Money(int amount) {
    public Money(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Money must not be negative value");
        this.amount = amount;
    }

    public Money add(Money other) {
        return new Money(this.amount + other.amount);
    }

    public Money subtract(Money other) {
        return new Money(this.amount - other.amount);
    }
}
