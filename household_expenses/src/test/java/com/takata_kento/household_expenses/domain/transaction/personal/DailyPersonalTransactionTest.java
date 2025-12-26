package com.takata_kento.household_expenses.domain.transaction.personal;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DailyPersonalTransactionTest {

    static Stream<Arguments> provideDailyPersonalTransactionData() {
        UUID uuid = UUID.randomUUID();
        return Stream.of(
            Arguments.of(
                new DailyPersonalTransactionId(uuid),
                new UserId(1L),
                LocalDate.of(2025, 12, 26),
                new Money(10_000),
                new ArrayList<DailyPersonalExpense>(),
                Integer.valueOf(1),
                new DailyPersonalTransaction(
                    new DailyPersonalTransactionId(uuid),
                    new UserId(1L),
                    LocalDate.of(2025, 12, 26),
                    new Money(10_000),
                    new ArrayList<DailyPersonalExpense>(),
                    Integer.valueOf(1)
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalTransactionData")
    void testId(
        DailyPersonalTransactionId expectedId,
        UserId userId,
        LocalDate transactionDate,
        Money income,
        List<DailyPersonalExpense> personalExpenses,
        Integer version,
        DailyPersonalTransaction dailyPersonalTransaction
    ) {
        // Given
        DailyPersonalTransaction expected = new DailyPersonalTransaction(
            expectedId,
            userId,
            transactionDate,
            income,
            personalExpenses,
            version
        );

        // When
        DailyPersonalTransactionId actual = dailyPersonalTransaction.id();

        // Then
        then(actual).isEqualTo(expectedId);
        then(dailyPersonalTransaction).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalTransactionData")
    void testUserId(
        DailyPersonalTransactionId id,
        UserId expectedUserId,
        LocalDate transactionDate,
        Money income,
        List<DailyPersonalExpense> personalExpenses,
        Integer version,
        DailyPersonalTransaction dailyPersonalTransaction
    ) {
        // Given
        DailyPersonalTransaction expected = new DailyPersonalTransaction(
            id,
            expectedUserId,
            transactionDate,
            income,
            personalExpenses,
            version
        );

        // When
        UserId actual = dailyPersonalTransaction.userId();

        // Then
        then(actual).isEqualTo(expectedUserId);
        then(dailyPersonalTransaction).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalTransactionData")
    void testTransactionDate(
        DailyPersonalTransactionId id,
        UserId userId,
        LocalDate expectedTransactionDate,
        Money income,
        List<DailyPersonalExpense> personalExpenses,
        Integer version,
        DailyPersonalTransaction dailyPersonalTransaction
    ) {
        // Given
        DailyPersonalTransaction expected = new DailyPersonalTransaction(
            id,
            userId,
            expectedTransactionDate,
            income,
            personalExpenses,
            version
        );

        // When
        LocalDate actual = dailyPersonalTransaction.transactionDate();

        // Then
        then(actual).isEqualTo(expectedTransactionDate);
        then(dailyPersonalTransaction).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalTransactionData")
    void testIncome(
        DailyPersonalTransactionId id,
        UserId userId,
        LocalDate transactionDate,
        Money expectedIncome,
        List<DailyPersonalExpense> personalExpenses,
        Integer version,
        DailyPersonalTransaction dailyPersonalTransaction
    ) {
        // Given
        DailyPersonalTransaction expected = new DailyPersonalTransaction(
            id,
            userId,
            transactionDate,
            expectedIncome,
            personalExpenses,
            version
        );

        // When
        Money actual = dailyPersonalTransaction.income();

        // Then
        then(actual).isEqualTo(expectedIncome);
        then(dailyPersonalTransaction).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalTransactionData")
    void testPersonalExpenses(
        DailyPersonalTransactionId id,
        UserId userId,
        LocalDate transactionDate,
        Money income,
        List<DailyPersonalExpense> expectedPersonalExpenses,
        Integer version,
        DailyPersonalTransaction dailyPersonalTransaction
    ) {
        // Given
        DailyPersonalTransaction expected = new DailyPersonalTransaction(
            id,
            userId,
            transactionDate,
            income,
            expectedPersonalExpenses,
            version
        );

        // When
        List<DailyPersonalExpense> actual = dailyPersonalTransaction.personalExpenses();

        // Then
        then(actual).isEqualTo(expectedPersonalExpenses);
        then(dailyPersonalTransaction).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalTransactionData")
    void testAddPersonalExpense(
        DailyPersonalTransactionId id,
        UserId userId,
        LocalDate transactionDate,
        Money income,
        List<DailyPersonalExpense> personalExpenses,
        Integer version,
        DailyPersonalTransaction dailyPersonalTransaction
    ) {
        // Given
        Money amount = new Money(1_000);
        Description memo = new Description("test memo");
        int expectedSize = personalExpenses.size() + 1;

        // When
        dailyPersonalTransaction.addPersonalExpense(amount, memo);

        // Then
        List<DailyPersonalExpense> actualPersonalExpenses = dailyPersonalTransaction.personalExpenses();
        then(actualPersonalExpenses).hasSize(expectedSize);

        DailyPersonalExpense addedExpense = actualPersonalExpenses.get(actualPersonalExpenses.size() - 1);
        then(addedExpense.amount()).isEqualTo(amount);
        then(addedExpense.memo()).isEqualTo(memo);
        then(addedExpense.dailyPersonalTransactionId()).isEqualTo(id);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalTransactionData")
    void testUpdateIncome(
        DailyPersonalTransactionId id,
        UserId userId,
        LocalDate transactionDate,
        Money income,
        List<DailyPersonalExpense> personalExpenses,
        Integer version,
        DailyPersonalTransaction dailyPersonalTransaction
    ) {
        // Given
        Money newIncome = new Money(20_000);

        // When
        dailyPersonalTransaction.updateIncome(newIncome);

        // Then
        Money actual = dailyPersonalTransaction.income();
        then(actual).isEqualTo(newIncome);

        then(dailyPersonalTransaction.id()).isEqualTo(id);
        then(dailyPersonalTransaction.userId()).isEqualTo(userId);
        then(dailyPersonalTransaction.transactionDate()).isEqualTo(transactionDate);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalTransactionData")
    void testUpdateIncomeWithNull(
        DailyPersonalTransactionId id,
        UserId userId,
        LocalDate transactionDate,
        Money income,
        List<DailyPersonalExpense> personalExpenses,
        Integer version,
        DailyPersonalTransaction dailyPersonalTransaction
    ) {
        // Given
        Money newIncome = null;

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyPersonalTransaction.updateIncome(newIncome)
        );

        // Then
        then(actual).hasMessage("income must not be null");
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalTransactionData")
    void testUpdateIncomeWithSameValue(
        DailyPersonalTransactionId id,
        UserId userId,
        LocalDate transactionDate,
        Money income,
        List<DailyPersonalExpense> personalExpenses,
        Integer version,
        DailyPersonalTransaction dailyPersonalTransaction
    ) {
        // Given
        Money newIncome = income;

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyPersonalTransaction.updateIncome(newIncome)
        );

        // Then
        then(actual).hasMessage("same parameter is detected when update income of DailyPersonalTransaction");
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalTransactionData")
    void testCalculateTotalPersonalExpense(
        DailyPersonalTransactionId id,
        UserId userId,
        LocalDate transactionDate,
        Money income,
        List<DailyPersonalExpense> personalExpenses,
        Integer version,
        DailyPersonalTransaction dailyPersonalTransaction
    ) {
        // Given
        Money amount1 = new Money(1_000);
        Description memo1 = new Description("test memo 1");

        Money amount2 = new Money(2_000);
        Description memo2 = new Description("test memo 2");

        Money expectedTotal = amount1.add(amount2);

        dailyPersonalTransaction.addPersonalExpense(amount1, memo1);
        dailyPersonalTransaction.addPersonalExpense(amount2, memo2);

        // When
        Money actual = dailyPersonalTransaction.calculateTotalPersonalExpense();

        // Then
        then(actual).isEqualTo(expectedTotal);
    }
}
