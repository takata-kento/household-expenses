package com.takata_kento.household_expenses.domain;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DailyPersonalExpenseTest {

    @Test
    void testConstructor() {
        // Given
        DailyPersonalExpenseId expectedId = new DailyPersonalExpenseId(1L);
        UserId expectedUserId = new UserId(1L);
        LocalDate expectedTransactionDate = LocalDate.of(2024, 1, 1);
        Money expectedAmount = new Money(1000);
        Description expectedDescription = new Description("test description");
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime expectedUpdatedAt = LocalDateTime.of(2024, 1, 1, 11, 0);
        Integer expectedVersion = 1;

        // When
        DailyPersonalExpense actual = new DailyPersonalExpense(
            expectedId,
            expectedUserId,
            expectedTransactionDate,
            expectedAmount,
            expectedDescription,
            expectedCreatedAt,
            expectedUpdatedAt,
            expectedVersion
        );

        // Then
        assertThat(actual.id()).isEqualTo(expectedId);
        assertThat(actual.userId()).isEqualTo(expectedUserId);
        assertThat(actual.transactionDate()).isEqualTo(expectedTransactionDate);
        assertThat(actual.amount()).isEqualTo(expectedAmount);
        assertThat(actual.description()).isEqualTo(expectedDescription);
        assertThat(actual.createdAt()).isEqualTo(expectedCreatedAt);
        assertThat(actual.updatedAt()).isEqualTo(expectedUpdatedAt);
        assertThat(actual.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testId() {
        // Given
        DailyPersonalExpenseId expectedId = new DailyPersonalExpenseId(1L);
        DailyPersonalExpense dailyPersonalExpense = new DailyPersonalExpense(
            expectedId,
            new UserId(1L),
            LocalDate.of(2024, 1, 1),
            new Money(1000),
            new Description("test description"),
            LocalDateTime.of(2024, 1, 1, 10, 0),
            LocalDateTime.of(2024, 1, 1, 11, 0),
            1
        );

        // When
        DailyPersonalExpenseId actual = dailyPersonalExpense.id();

        // Then
        assertThat(actual).isEqualTo(expectedId);
    }

    @Test
    void testUserId() {
        // Given
        UserId expectedUserId = new UserId(1L);
        DailyPersonalExpense dailyPersonalExpense = new DailyPersonalExpense(
            new DailyPersonalExpenseId(1L),
            expectedUserId,
            LocalDate.of(2024, 1, 1),
            new Money(1000),
            new Description("test description"),
            LocalDateTime.of(2024, 1, 1, 10, 0),
            LocalDateTime.of(2024, 1, 1, 11, 0),
            1
        );

        // When
        UserId actual = dailyPersonalExpense.userId();

        // Then
        assertThat(actual).isEqualTo(expectedUserId);
    }

    @Test
    void testTransactionDate() {
        // Given
        LocalDate expectedTransactionDate = LocalDate.of(2024, 1, 1);
        DailyPersonalExpense dailyPersonalExpense = new DailyPersonalExpense(
            new DailyPersonalExpenseId(1L),
            new UserId(1L),
            expectedTransactionDate,
            new Money(1000),
            new Description("test description"),
            LocalDateTime.of(2024, 1, 1, 10, 0),
            LocalDateTime.of(2024, 1, 1, 11, 0),
            1
        );

        // When
        LocalDate actual = dailyPersonalExpense.transactionDate();

        // Then
        assertThat(actual).isEqualTo(expectedTransactionDate);
    }

    @Test
    void testAmount() {
        // Given
        Money expectedAmount = new Money(1000);
        DailyPersonalExpense dailyPersonalExpense = new DailyPersonalExpense(
            new DailyPersonalExpenseId(1L),
            new UserId(1L),
            LocalDate.of(2024, 1, 1),
            expectedAmount,
            new Description("test description"),
            LocalDateTime.of(2024, 1, 1, 10, 0),
            LocalDateTime.of(2024, 1, 1, 11, 0),
            1
        );

        // When
        Money actual = dailyPersonalExpense.amount();

        // Then
        assertThat(actual).isEqualTo(expectedAmount);
    }

    @Test
    void testDescription() {
        // Given
        Description expectedDescription = new Description("test description");
        DailyPersonalExpense dailyPersonalExpense = new DailyPersonalExpense(
            new DailyPersonalExpenseId(1L),
            new UserId(1L),
            LocalDate.of(2024, 1, 1),
            new Money(1000),
            expectedDescription,
            LocalDateTime.of(2024, 1, 1, 10, 0),
            LocalDateTime.of(2024, 1, 1, 11, 0),
            1
        );

        // When
        Description actual = dailyPersonalExpense.description();

        // Then
        assertThat(actual).isEqualTo(expectedDescription);
    }

    @Test
    void testCreatedAt() {
        // Given
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        DailyPersonalExpense dailyPersonalExpense = new DailyPersonalExpense(
            new DailyPersonalExpenseId(1L),
            new UserId(1L),
            LocalDate.of(2024, 1, 1),
            new Money(1000),
            new Description("test description"),
            expectedCreatedAt,
            LocalDateTime.of(2024, 1, 1, 11, 0),
            1
        );

        // When
        LocalDateTime actual = dailyPersonalExpense.createdAt();

        // Then
        assertThat(actual).isEqualTo(expectedCreatedAt);
    }

    @Test
    void testUpdatedAt() {
        // Given
        LocalDateTime expectedUpdatedAt = LocalDateTime.of(2024, 1, 1, 11, 0);
        DailyPersonalExpense dailyPersonalExpense = new DailyPersonalExpense(
            new DailyPersonalExpenseId(1L),
            new UserId(1L),
            LocalDate.of(2024, 1, 1),
            new Money(1000),
            new Description("test description"),
            LocalDateTime.of(2024, 1, 1, 10, 0),
            expectedUpdatedAt,
            1
        );

        // When
        LocalDateTime actual = dailyPersonalExpense.updatedAt();

        // Then
        assertThat(actual).isEqualTo(expectedUpdatedAt);
    }

    @Test
    void testVersion() {
        // Given
        Integer expectedVersion = 1;
        DailyPersonalExpense dailyPersonalExpense = new DailyPersonalExpense(
            new DailyPersonalExpenseId(1L),
            new UserId(1L),
            LocalDate.of(2024, 1, 1),
            new Money(1000),
            new Description("test description"),
            LocalDateTime.of(2024, 1, 1, 10, 0),
            LocalDateTime.of(2024, 1, 1, 11, 0),
            expectedVersion
        );

        // When
        Integer actual = dailyPersonalExpense.version();

        // Then
        assertThat(actual).isEqualTo(expectedVersion);
    }

    @Test
    void testOf() {
        // Given
        UserId expectedUserId = new UserId(1L);
        LocalDate expectedTransactionDate = LocalDate.of(2024, 1, 1);
        Money expectedAmount = new Money(1000);
        Description expectedDescription = new Description("test description");

        // When
        DailyPersonalExpense actual = DailyPersonalExpense.of(
            expectedUserId,
            expectedTransactionDate,
            expectedAmount,
            expectedDescription
        );

        // Then
        assertThat(actual.userId()).isEqualTo(expectedUserId);
        assertThat(actual.transactionDate()).isEqualTo(expectedTransactionDate);
        assertThat(actual.amount()).isEqualTo(expectedAmount);
        assertThat(actual.description()).isEqualTo(expectedDescription);
        assertThat(actual.id()).isNotNull();
        assertThat(actual.createdAt()).isNotNull();
        assertThat(actual.updatedAt()).isNull();
        assertThat(actual.version()).isNull();
    }
}
