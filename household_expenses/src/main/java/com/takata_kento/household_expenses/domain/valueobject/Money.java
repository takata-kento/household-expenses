package com.takata_kento.household_expenses.domain.valueobject;

public record Money(int amount) {
    public Money add(Money other) {
        return new Money(this.amount + other.amount);
    }

    public Money subtract(Money other) {
        return new Money(this.amount - other.amount);
    }
}
