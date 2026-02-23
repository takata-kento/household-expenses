package com.takata_kento.household_expenses.domain.expense.history;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseHistoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FixedExpenseHistoryTest {

    static Stream<Arguments> provideFixedExpenseHistoryData() {
        UUID uuid = UUID.randomUUID();
        UUID categoryUUID = UUID.randomUUID();
        return Stream.of(
            Arguments.of(
                new FixedExpenseHistoryId(uuid),
                new FixedExpenseCategoryId(categoryUUID),
                new Year(2026),
                new Month(2),
                new Money(80_000),
                LocalDate.of(2026, 2, 1),
                Optional.of(new Description("毎月の家賃")),
                Integer.valueOf(0),
                new FixedExpenseHistory(
                    new FixedExpenseHistoryId(uuid),
                    new FixedExpenseCategoryId(categoryUUID),
                    new Year(2026),
                    new Month(2),
                    new Money(80_000),
                    LocalDate.of(2026, 2, 1),
                    Optional.of(new Description("毎月の家賃")),
                    Integer.valueOf(0)
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseHistoryData")
    void testId(
        FixedExpenseHistoryId expectedId,
        FixedExpenseCategoryId fixedExpenseCategoryId,
        Year year,
        Month month,
        Money amount,
        LocalDate effectiveDate,
        Optional<Description> memo,
        Integer version,
        FixedExpenseHistory fixedExpenseHistory
    ) {
        // Given
        FixedExpenseHistory expected = new FixedExpenseHistory(
            expectedId,
            fixedExpenseCategoryId,
            year,
            month,
            amount,
            effectiveDate,
            memo,
            version
        );

        // When
        FixedExpenseHistoryId actual = fixedExpenseHistory.id();

        // Then
        then(actual).isEqualTo(expectedId);
        then(fixedExpenseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseHistoryData")
    void testFixedExpenseCategoryId(
        FixedExpenseHistoryId id,
        FixedExpenseCategoryId expectedFixedExpenseCategoryId,
        Year year,
        Month month,
        Money amount,
        LocalDate effectiveDate,
        Optional<Description> memo,
        Integer version,
        FixedExpenseHistory fixedExpenseHistory
    ) {
        // Given
        FixedExpenseHistory expected = new FixedExpenseHistory(
            id,
            expectedFixedExpenseCategoryId,
            year,
            month,
            amount,
            effectiveDate,
            memo,
            version
        );

        // When
        FixedExpenseCategoryId actual = fixedExpenseHistory.fixedExpenseCategoryId();

        // Then
        then(actual).isEqualTo(expectedFixedExpenseCategoryId);
        then(fixedExpenseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseHistoryData")
    void testYear(
        FixedExpenseHistoryId id,
        FixedExpenseCategoryId fixedExpenseCategoryId,
        Year expectedYear,
        Month month,
        Money amount,
        LocalDate effectiveDate,
        Optional<Description> memo,
        Integer version,
        FixedExpenseHistory fixedExpenseHistory
    ) {
        // Given
        FixedExpenseHistory expected = new FixedExpenseHistory(
            id,
            fixedExpenseCategoryId,
            expectedYear,
            month,
            amount,
            effectiveDate,
            memo,
            version
        );

        // When
        Year actual = fixedExpenseHistory.year();

        // Then
        then(actual).isEqualTo(expectedYear);
        then(fixedExpenseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseHistoryData")
    void testMonth(
        FixedExpenseHistoryId id,
        FixedExpenseCategoryId fixedExpenseCategoryId,
        Year year,
        Month expectedMonth,
        Money amount,
        LocalDate effectiveDate,
        Optional<Description> memo,
        Integer version,
        FixedExpenseHistory fixedExpenseHistory
    ) {
        // Given
        FixedExpenseHistory expected = new FixedExpenseHistory(
            id,
            fixedExpenseCategoryId,
            year,
            expectedMonth,
            amount,
            effectiveDate,
            memo,
            version
        );

        // When
        Month actual = fixedExpenseHistory.month();

        // Then
        then(actual).isEqualTo(expectedMonth);
        then(fixedExpenseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseHistoryData")
    void testAmount(
        FixedExpenseHistoryId id,
        FixedExpenseCategoryId fixedExpenseCategoryId,
        Year year,
        Month month,
        Money expectedAmount,
        LocalDate effectiveDate,
        Optional<Description> memo,
        Integer version,
        FixedExpenseHistory fixedExpenseHistory
    ) {
        // Given
        FixedExpenseHistory expected = new FixedExpenseHistory(
            id,
            fixedExpenseCategoryId,
            year,
            month,
            expectedAmount,
            effectiveDate,
            memo,
            version
        );

        // When
        Money actual = fixedExpenseHistory.amount();

        // Then
        then(actual).isEqualTo(expectedAmount);
        then(fixedExpenseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseHistoryData")
    void testEffectiveDate(
        FixedExpenseHistoryId id,
        FixedExpenseCategoryId fixedExpenseCategoryId,
        Year year,
        Month month,
        Money amount,
        LocalDate expectedEffectiveDate,
        Optional<Description> memo,
        Integer version,
        FixedExpenseHistory fixedExpenseHistory
    ) {
        // Given
        FixedExpenseHistory expected = new FixedExpenseHistory(
            id,
            fixedExpenseCategoryId,
            year,
            month,
            amount,
            expectedEffectiveDate,
            memo,
            version
        );

        // When
        LocalDate actual = fixedExpenseHistory.effectiveDate();

        // Then
        then(actual).isEqualTo(expectedEffectiveDate);
        then(fixedExpenseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseHistoryData")
    void testMemo(
        FixedExpenseHistoryId id,
        FixedExpenseCategoryId fixedExpenseCategoryId,
        Year year,
        Month month,
        Money amount,
        LocalDate effectiveDate,
        Optional<Description> expectedMemo,
        Integer version,
        FixedExpenseHistory fixedExpenseHistory
    ) {
        // Given
        FixedExpenseHistory expected = new FixedExpenseHistory(
            id,
            fixedExpenseCategoryId,
            year,
            month,
            amount,
            effectiveDate,
            expectedMemo,
            version
        );

        // When
        Optional<Description> actual = fixedExpenseHistory.memo();

        // Then
        then(actual).isEqualTo(expectedMemo);
        then(fixedExpenseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseHistoryData")
    void testUpdateAmount(
        FixedExpenseHistoryId id,
        FixedExpenseCategoryId fixedExpenseCategoryId,
        Year year,
        Month month,
        Money amount,
        LocalDate effectiveDate,
        Optional<Description> memo,
        Integer version,
        FixedExpenseHistory fixedExpenseHistory
    ) {
        // Given
        Money newAmount = new Money(90_000);
        FixedExpenseHistory expected = new FixedExpenseHistory(
            id,
            fixedExpenseCategoryId,
            year,
            month,
            newAmount,
            effectiveDate,
            memo,
            version
        );

        // When
        fixedExpenseHistory.updateAmount(newAmount);

        // Then
        then(fixedExpenseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseHistoryData")
    void testUpdateEffectiveDate(
        FixedExpenseHistoryId id,
        FixedExpenseCategoryId fixedExpenseCategoryId,
        Year year,
        Month month,
        Money amount,
        LocalDate effectiveDate,
        Optional<Description> memo,
        Integer version,
        FixedExpenseHistory fixedExpenseHistory
    ) {
        // Given
        LocalDate newEffectiveDate = LocalDate.of(2026, 2, 15);
        FixedExpenseHistory expected = new FixedExpenseHistory(
            id,
            fixedExpenseCategoryId,
            year,
            month,
            amount,
            newEffectiveDate,
            memo,
            version
        );

        // When
        fixedExpenseHistory.updateEffectiveDate(newEffectiveDate);

        // Then
        then(fixedExpenseHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseHistoryData")
    void testUpdateMemo(
        FixedExpenseHistoryId id,
        FixedExpenseCategoryId fixedExpenseCategoryId,
        Year year,
        Month month,
        Money amount,
        LocalDate effectiveDate,
        Optional<Description> memo,
        Integer version,
        FixedExpenseHistory fixedExpenseHistory
    ) {
        // Given
        Optional<Description> newMemo = Optional.of(new Description("更新後のメモ"));
        FixedExpenseHistory expected = new FixedExpenseHistory(
            id,
            fixedExpenseCategoryId,
            year,
            month,
            amount,
            effectiveDate,
            newMemo,
            version
        );

        // When
        fixedExpenseHistory.updateMemo(newMemo);

        // Then
        then(fixedExpenseHistory).usingRecursiveComparison().isEqualTo(expected);
    }
}
