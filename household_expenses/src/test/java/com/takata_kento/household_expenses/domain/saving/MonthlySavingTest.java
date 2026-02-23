package com.takata_kento.household_expenses.domain.saving;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.MonthlySavingId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MonthlySavingTest {

    static Stream<Arguments> provideMonthlySavingData() {
        MonthlySavingId id = new MonthlySavingId(UUID.randomUUID());
        UserId userId = new UserId(UUID.randomUUID());
        Year year = new Year(2024);
        Month month = new Month(6);
        Money savingAmount = new Money(50_000);
        FinancialAccountId financialAccountId = new FinancialAccountId(UUID.randomUUID());
        Optional<Description> memo = Optional.of(new Description("6月の貯金"));
        Integer version = Integer.valueOf(1);

        return Stream.of(
            Arguments.of(
                id,
                userId,
                year,
                month,
                savingAmount,
                financialAccountId,
                memo,
                version,
                new MonthlySaving(id, userId, year, month, savingAmount, financialAccountId, memo, version)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testConstructor(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money expectedSavingAmount,
        FinancialAccountId expectedFinancialAccountId,
        Optional<Description> expectedMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // When
        MonthlySaving actual = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo,
            expectedVersion
        );

        // Then
        then(actual).usingRecursiveComparison().isEqualTo(monthlySaving);
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testId(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money expectedSavingAmount,
        FinancialAccountId expectedFinancialAccountId,
        Optional<Description> expectedMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // Given
        MonthlySaving saving = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo,
            expectedVersion
        );

        // When
        MonthlySavingId actual = monthlySaving.id();

        // Then
        then(actual).isEqualTo(expectedId);
        then(monthlySaving).usingRecursiveComparison().isEqualTo(saving);
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testUserId(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money expectedSavingAmount,
        FinancialAccountId expectedFinancialAccountId,
        Optional<Description> expectedMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // Given
        MonthlySaving saving = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo,
            expectedVersion
        );

        // When
        UserId actual = monthlySaving.userId();

        // Then
        then(actual).isEqualTo(expectedUserId);
        then(monthlySaving).usingRecursiveComparison().isEqualTo(saving);
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testYear(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money expectedSavingAmount,
        FinancialAccountId expectedFinancialAccountId,
        Optional<Description> expectedMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // Given
        MonthlySaving saving = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo,
            expectedVersion
        );

        // When
        Year actual = monthlySaving.year();

        // Then
        then(actual).isEqualTo(expectedYear);
        then(monthlySaving).usingRecursiveComparison().isEqualTo(saving);
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testMonth(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money expectedSavingAmount,
        FinancialAccountId expectedFinancialAccountId,
        Optional<Description> expectedMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // Given
        MonthlySaving saving = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo,
            expectedVersion
        );

        // When
        Month actual = monthlySaving.month();

        // Then
        then(actual).isEqualTo(expectedMonth);
        then(monthlySaving).usingRecursiveComparison().isEqualTo(saving);
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testSavingAmount(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money expectedSavingAmount,
        FinancialAccountId expectedFinancialAccountId,
        Optional<Description> expectedMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // Given
        MonthlySaving saving = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo,
            expectedVersion
        );

        // When
        Money actual = monthlySaving.savingAmount();

        // Then
        then(actual).isEqualTo(expectedSavingAmount);
        then(monthlySaving).usingRecursiveComparison().isEqualTo(saving);
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testFinancialAccountId(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money expectedSavingAmount,
        FinancialAccountId expectedFinancialAccountId,
        Optional<Description> expectedMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // Given
        MonthlySaving saving = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo,
            expectedVersion
        );

        // When
        FinancialAccountId actual = monthlySaving.financialAccountId();

        // Then
        then(actual).isEqualTo(expectedFinancialAccountId);
        then(monthlySaving).usingRecursiveComparison().isEqualTo(saving);
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testMemo(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money expectedSavingAmount,
        FinancialAccountId expectedFinancialAccountId,
        Optional<Description> expectedMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // Given
        MonthlySaving saving = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo,
            expectedVersion
        );

        // When
        Optional<Description> actual = monthlySaving.memo();

        // Then
        then(actual).isEqualTo(expectedMemo);
        then(monthlySaving).usingRecursiveComparison().isEqualTo(saving);
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testVersion(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money expectedSavingAmount,
        FinancialAccountId expectedFinancialAccountId,
        Optional<Description> expectedMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // Given
        MonthlySaving saving = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo,
            expectedVersion
        );

        // When
        Integer actual = monthlySaving.version();

        // Then
        then(actual).isEqualTo(expectedVersion);
        then(monthlySaving).usingRecursiveComparison().isEqualTo(saving);
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testUpdateSavingAmount(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money currentSavingAmount,
        FinancialAccountId expectedFinancialAccountId,
        Optional<Description> expectedMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // Given
        Money expectedNewSavingAmount = new Money(80_000);
        MonthlySaving expected = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedNewSavingAmount,
            expectedFinancialAccountId,
            expectedMemo,
            expectedVersion
        );

        // When
        monthlySaving.updateSavingAmount(expectedNewSavingAmount);

        // Then
        then(monthlySaving).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testUpdateFinancialAccount(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money expectedSavingAmount,
        FinancialAccountId currentFinancialAccountId,
        Optional<Description> expectedMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // Given
        FinancialAccountId expectedNewFinancialAccountId = new FinancialAccountId(UUID.randomUUID());
        MonthlySaving expected = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedNewFinancialAccountId,
            expectedMemo,
            expectedVersion
        );

        // When
        monthlySaving.updateFinancialAccount(expectedNewFinancialAccountId);

        // Then
        then(monthlySaving).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideMonthlySavingData")
    void testUpdateMemo(
        MonthlySavingId expectedId,
        UserId expectedUserId,
        Year expectedYear,
        Month expectedMonth,
        Money expectedSavingAmount,
        FinancialAccountId expectedFinancialAccountId,
        Optional<Description> currentMemo,
        Integer expectedVersion,
        MonthlySaving monthlySaving
    ) {
        // Given
        Optional<Description> expectedNewMemoDesc = Optional.of(new Description("更新後のメモ"));
        MonthlySaving expected = new MonthlySaving(
            expectedId,
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedNewMemoDesc,
            expectedVersion
        );

        // When
        monthlySaving.updateMemo(expectedNewMemoDesc);

        // Then
        then(monthlySaving).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testCreate() {
        // Given
        UserId expectedUserId = new UserId(UUID.randomUUID());
        Year expectedYear = new Year(2024);
        Month expectedMonth = new Month(6);
        Money expectedSavingAmount = new Money(50_000);
        FinancialAccountId expectedFinancialAccountId = new FinancialAccountId(UUID.randomUUID());
        Optional<Description> expectedMemo = Optional.of(new Description("6月の貯金"));

        // When
        MonthlySaving actual = MonthlySaving.create(
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo
        );

        // Then
        assertThat(actual.id()).isNotNull();
        assertThat(actual.userId()).isEqualTo(expectedUserId);
        assertThat(actual.year()).isEqualTo(expectedYear);
        assertThat(actual.month()).isEqualTo(expectedMonth);
        assertThat(actual.savingAmount()).isEqualTo(expectedSavingAmount);
        assertThat(actual.financialAccountId()).isEqualTo(expectedFinancialAccountId);
        assertThat(actual.memo()).isEqualTo(expectedMemo);
        assertThat(actual.version()).isNull();
    }

    @Test
    void testCreate_withEmptyMemo() {
        // Given
        UserId expectedUserId = new UserId(UUID.randomUUID());
        Year expectedYear = new Year(2024);
        Month expectedMonth = new Month(7);
        Money expectedSavingAmount = new Money(30000);
        FinancialAccountId expectedFinancialAccountId = new FinancialAccountId(UUID.randomUUID());
        Optional<Description> expectedMemo = Optional.empty();

        // When
        MonthlySaving actual = MonthlySaving.create(
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo
        );

        // Then
        assertThat(actual.memo()).isEqualTo(Optional.empty());
    }
}
