package com.takata_kento.household_expenses.domain.account;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.BalanceEditHistoryId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class BalanceEditHistoryTest {

    static Stream<Arguments> provideBalanceEditHistoryData() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
        UUID balanceEditHistoryUUID = UUID.randomUUID();
        UUID financialAccountUUID = UUID.randomUUID();
        return Stream.of(
            Arguments.of(
                new BalanceEditHistoryId(balanceEditHistoryUUID),
                new FinancialAccountId(financialAccountUUID),
                new Money(50_000),
                new Money(100_000),
                new Description("Salary deposit"),
                createdAt,
                Integer.valueOf(1),
                new BalanceEditHistory(
                    new BalanceEditHistoryId(balanceEditHistoryUUID),
                    new FinancialAccountId(financialAccountUUID),
                    new Money(50_000),
                    new Money(100_000),
                    new Description("Salary deposit"),
                    createdAt,
                    Integer.valueOf(1)
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideBalanceEditHistoryData")
    void testId(
        BalanceEditHistoryId expectedId,
        FinancialAccountId financialAccountId,
        Money oldBalance,
        Money newBalance,
        Description editReason,
        LocalDateTime createdAt,
        Integer version,
        BalanceEditHistory balanceEditHistory
    ) {
        // Given
        BalanceEditHistory expected = new BalanceEditHistory(
            expectedId,
            financialAccountId,
            oldBalance,
            newBalance,
            editReason,
            createdAt,
            version
        );

        // When
        BalanceEditHistoryId actual = balanceEditHistory.id();

        // Then
        then(actual).isEqualTo(expectedId);
        then(balanceEditHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideBalanceEditHistoryData")
    void testFinancialAccountId(
        BalanceEditHistoryId id,
        FinancialAccountId expectedFinancialAccountId,
        Money oldBalance,
        Money newBalance,
        Description editReason,
        LocalDateTime createdAt,
        Integer version,
        BalanceEditHistory balanceEditHistory
    ) {
        // Given
        BalanceEditHistory expected = new BalanceEditHistory(
            id,
            expectedFinancialAccountId,
            oldBalance,
            newBalance,
            editReason,
            createdAt,
            version
        );

        // When
        FinancialAccountId actual = balanceEditHistory.financialAccountId();

        // Then
        then(actual).isEqualTo(expectedFinancialAccountId);
        then(balanceEditHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideBalanceEditHistoryData")
    void testOldBalance(
        BalanceEditHistoryId id,
        FinancialAccountId financialAccountId,
        Money expectedOldBalance,
        Money newBalance,
        Description editReason,
        LocalDateTime createdAt,
        Integer version,
        BalanceEditHistory balanceEditHistory
    ) {
        // Given
        BalanceEditHistory expected = new BalanceEditHistory(
            id,
            financialAccountId,
            expectedOldBalance,
            newBalance,
            editReason,
            createdAt,
            version
        );

        // When
        Money actual = balanceEditHistory.oldBalance();

        // Then
        then(actual).isEqualTo(expectedOldBalance);
        then(balanceEditHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideBalanceEditHistoryData")
    void testNewBalance(
        BalanceEditHistoryId id,
        FinancialAccountId financialAccountId,
        Money oldBalance,
        Money expectedNewBalance,
        Description editReason,
        LocalDateTime createdAt,
        Integer version,
        BalanceEditHistory balanceEditHistory
    ) {
        // Given
        BalanceEditHistory expected = new BalanceEditHistory(
            id,
            financialAccountId,
            oldBalance,
            expectedNewBalance,
            editReason,
            createdAt,
            version
        );

        // When
        Money actual = balanceEditHistory.newBalance();

        // Then
        then(actual).isEqualTo(expectedNewBalance);
        then(balanceEditHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideBalanceEditHistoryData")
    void testEditReason(
        BalanceEditHistoryId id,
        FinancialAccountId financialAccountId,
        Money oldBalance,
        Money newBalance,
        Description expectedEditReason,
        LocalDateTime createdAt,
        Integer version,
        BalanceEditHistory balanceEditHistory
    ) {
        // Given
        BalanceEditHistory expected = new BalanceEditHistory(
            id,
            financialAccountId,
            oldBalance,
            newBalance,
            expectedEditReason,
            createdAt,
            version
        );

        // When
        Description actual = balanceEditHistory.editReason();

        // Then
        then(actual).isEqualTo(expectedEditReason);
        then(balanceEditHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideBalanceEditHistoryData")
    void testCreatedAt(
        BalanceEditHistoryId id,
        FinancialAccountId financialAccountId,
        Money oldBalance,
        Money newBalance,
        Description editReason,
        LocalDateTime expectedCreatedAt,
        Integer version,
        BalanceEditHistory balanceEditHistory
    ) {
        // Given
        BalanceEditHistory expected = new BalanceEditHistory(
            id,
            financialAccountId,
            oldBalance,
            newBalance,
            editReason,
            expectedCreatedAt,
            version
        );

        // When
        LocalDateTime actual = balanceEditHistory.createdAt();

        // Then
        then(actual).isEqualTo(expectedCreatedAt);
        then(balanceEditHistory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideBalanceEditHistoryData")
    void testCreate(
        BalanceEditHistoryId id,
        FinancialAccountId expectedFinancialAccountId,
        Money expectedOldBalance,
        Money expectedNewBalance,
        Description expectedEditReason,
        LocalDateTime createdAt,
        Integer version,
        BalanceEditHistory balanceEditHistory
    ) {
        // Given
        // (parameters provided by method source)

        // When
        BalanceEditHistory actual = BalanceEditHistory.create(
            expectedFinancialAccountId,
            expectedOldBalance,
            expectedNewBalance,
            expectedEditReason
        );

        // Then
        then(actual.id()).isNotNull();
        then(actual.financialAccountId()).isEqualTo(expectedFinancialAccountId);
        then(actual.oldBalance()).isEqualTo(expectedOldBalance);
        then(actual.newBalance()).isEqualTo(expectedNewBalance);
        then(actual.editReason()).isEqualTo(expectedEditReason);
        then(actual.createdAt()).isNull();
    }
}
