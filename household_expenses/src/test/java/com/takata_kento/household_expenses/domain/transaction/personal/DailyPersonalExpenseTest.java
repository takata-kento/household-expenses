package com.takata_kento.household_expenses.domain.transaction.personal;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DailyPersonalExpenseTest {

    static Stream<Arguments> provideDailyPersonalExpenseData() {
        UUID dailyPersonalExpenseUUID = UUID.randomUUID();
        UUID dailyPersonalTransactionUUID = UUID.randomUUID();
        return Stream.of(
            Arguments.of(
                new DailyPersonalExpenseId(dailyPersonalExpenseUUID),
                new DailyPersonalTransactionId(dailyPersonalTransactionUUID),
                new Money(10_000),
                new Description("Lunch expenses"),
                Integer.valueOf(1),
                new DailyPersonalExpense(
                    new DailyPersonalExpenseId(dailyPersonalExpenseUUID),
                    new DailyPersonalTransactionId(dailyPersonalTransactionUUID),
                    new Money(10_000),
                    new Description("Lunch expenses"),
                    Integer.valueOf(1)
                )
            )
        );
    }

    static Stream<DailyPersonalExpense> provideDailyPersonalExpenseInstance() {
        return Stream.of(
            new DailyPersonalExpense(
                new DailyPersonalExpenseId(UUID.randomUUID()),
                new DailyPersonalTransactionId(UUID.randomUUID()),
                new Money(10_000),
                new Description("Lunch expenses"),
                Integer.valueOf(1)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalExpenseData")
    void testId(
        DailyPersonalExpenseId expectedId,
        DailyPersonalTransactionId dailyPersonalTransactionId,
        Money amount,
        Description memo,
        Integer version,
        DailyPersonalExpense dailyPersonalExpense
    ) {
        // Given
        DailyPersonalExpense expected = new DailyPersonalExpense(
            expectedId,
            dailyPersonalTransactionId,
            amount,
            memo,
            version
        );

        // When
        DailyPersonalExpenseId actual = dailyPersonalExpense.id();

        // Then
        then(actual).isEqualTo(expectedId);
        then(dailyPersonalExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalExpenseData")
    void testDailyPersonalTransactionId(
        DailyPersonalExpenseId id,
        DailyPersonalTransactionId expectedDailyPersonalTransactionId,
        Money amount,
        Description memo,
        Integer version,
        DailyPersonalExpense dailyPersonalExpense
    ) {
        // Given
        DailyPersonalExpense expected = new DailyPersonalExpense(
            id,
            expectedDailyPersonalTransactionId,
            amount,
            memo,
            version
        );

        // When
        DailyPersonalTransactionId actual = dailyPersonalExpense.dailyPersonalTransactionId();

        // Then
        then(actual).isEqualTo(expectedDailyPersonalTransactionId);
        then(dailyPersonalExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalExpenseData")
    void testAmount(
        DailyPersonalExpenseId id,
        DailyPersonalTransactionId dailyPersonalTransactionId,
        Money expectedAmount,
        Description memo,
        Integer version,
        DailyPersonalExpense dailyPersonalExpense
    ) {
        // Given
        DailyPersonalExpense expected = new DailyPersonalExpense(
            id,
            dailyPersonalTransactionId,
            expectedAmount,
            memo,
            version
        );

        // When
        Money actual = dailyPersonalExpense.amount();

        // Then
        then(actual).isEqualTo(expectedAmount);
        then(dailyPersonalExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalExpenseData")
    void testMemo(
        DailyPersonalExpenseId id,
        DailyPersonalTransactionId dailyPersonalTransactionId,
        Money amount,
        Description expectedMemo,
        Integer version,
        DailyPersonalExpense dailyPersonalExpense
    ) {
        // Given
        DailyPersonalExpense expected = new DailyPersonalExpense(
            id,
            dailyPersonalTransactionId,
            amount,
            expectedMemo,
            version
        );

        // When
        Description actual = dailyPersonalExpense.memo();

        // Then
        then(actual).isEqualTo(expectedMemo);
        then(dailyPersonalExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalExpenseData")
    void testUpdateAmount(
        DailyPersonalExpenseId id,
        DailyPersonalTransactionId dailyPersonalTransactionId,
        Money previousAmount,
        Description memo,
        Integer version,
        DailyPersonalExpense dailyPersonalExpense
    ) {
        // Given
        Money expectedAmount = new Money(20_000);

        DailyPersonalExpense expected = new DailyPersonalExpense(
            id,
            dailyPersonalTransactionId,
            expectedAmount,
            memo,
            version
        );

        // When
        dailyPersonalExpense.updateAmount(expectedAmount);

        // Then
        then(dailyPersonalExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalExpenseInstance")
    void testThrowSameAmountErrorWhenUpdate(DailyPersonalExpense dailyPersonalExpense) {
        // Given
        Money sameAmount = new Money(10_000);

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyPersonalExpense.updateAmount(sameAmount)
        );

        // Then
        then(actual).hasMessage("same parameter is detected when update amount of DailyPersonalExpense");
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalExpenseInstance")
    void testNullCheckWhenUpdateAmount(DailyPersonalExpense dailyPersonalExpense) {
        // When
        IllegalArgumentException exception = catchIllegalArgumentException(() ->
            dailyPersonalExpense.updateAmount(null)
        );

        // Then
        then(exception).hasMessage("amount must not be null");
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalExpenseData")
    void testUpdateMemo(
        DailyPersonalExpenseId id,
        DailyPersonalTransactionId dailyPersonalTransactionId,
        Money amount,
        Description previousMemo,
        Integer version,
        DailyPersonalExpense dailyPersonalExpense
    ) {
        // Given
        Description expectedMemo = new Description("Updated dinner expenses");

        DailyPersonalExpense expected = new DailyPersonalExpense(
            id,
            dailyPersonalTransactionId,
            amount,
            expectedMemo,
            version
        );

        // When
        dailyPersonalExpense.updateMemo(expectedMemo);

        // Then
        then(dailyPersonalExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalExpenseInstance")
    void testThrowSameMemoErrorWhenUpdateMemo(DailyPersonalExpense dailyPersonalExpense) {
        // Given
        Description sameMemo = new Description("Lunch expenses");

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyPersonalExpense.updateMemo(sameMemo)
        );

        // Then
        then(actual).hasMessage("same parameter is detected when update memo of DailyPersonalExpense");
    }

    @ParameterizedTest
    @MethodSource("provideDailyPersonalExpenseInstance")
    void testNullCheckWhenUpdateMemo(DailyPersonalExpense dailyPersonalExpense) {
        // When
        IllegalArgumentException exception = catchIllegalArgumentException(() -> dailyPersonalExpense.updateMemo(null));

        // Then
        then(exception).hasMessage("memo must not be null");
    }
}
