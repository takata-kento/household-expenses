package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void testCreate() {
        // Given
        int amount = 1000;

        // When
        Money actual = new Money(amount);

        // Then
        assertThat(actual.amount()).isEqualTo(1000);
    }

    @Test
    void testAdd() {
        // Given
        Money money1 = new Money(1000);
        Money money2 = new Money(500);

        // When
        Money actual = money1.add(money2);

        // Then
        assertThat(actual.amount()).isEqualTo(1500);
    }

    @Test
    void testSubtract() {
        // Given
        Money money1 = new Money(1000);
        Money money2 = new Money(300);

        // When
        Money actual = money1.subtract(money2);

        // Then
        assertThat(actual.amount()).isEqualTo(700);
    }

    @Test
    void testEquals() {
        // Given
        Money money1 = new Money(1000);
        Money money2 = new Money(1000);
        Money money3 = new Money(500);

        // When & Then
        assertThat(money1).isEqualTo(money2);
        assertThat(money1).isNotEqualTo(money3);
    }

    @Test
    void testHashCode() {
        // Given
        Money money1 = new Money(1000);
        Money money2 = new Money(1000);

        // When & Then
        assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
    }

    @Test
    void testToString() {
        // Given
        Money money = new Money(1000);

        // When
        String actual = money.toString();

        // Then
        assertThat(actual).contains("1000");
    }
}
