package com.takata_kento.household_expenses.domain.account;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.AccountName;
import com.takata_kento.household_expenses.domain.valueobject.BalanceEditHistoryId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FinancialAccountTest {

    static Stream<Arguments> provideFinancialAccountData() {
        UUID financialAccountUUID = UUID.randomUUID();
        UUID userUUID = UUID.randomUUID();
        return Stream.of(
            Arguments.of(
                new FinancialAccountId(financialAccountUUID),
                new UserId(userUUID),
                new AccountName("メイン口座"),
                new Money(100_000),
                Boolean.TRUE,
                new HashSet<BalanceEditHistory>(),
                Integer.valueOf(1),
                new FinancialAccount(
                    new FinancialAccountId(financialAccountUUID),
                    new UserId(userUUID),
                    new AccountName("メイン口座"),
                    new Money(100_000),
                    Boolean.TRUE,
                    new HashSet<BalanceEditHistory>(),
                    Integer.valueOf(1)
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFinancialAccountData")
    void testId(
        FinancialAccountId expectedId,
        UserId userId,
        AccountName accountName,
        Money balance,
        Boolean isMainAccount,
        Set<BalanceEditHistory> editHistories,
        Integer version,
        FinancialAccount financialAccount
    ) {
        // Given
        FinancialAccount expected = new FinancialAccount(
            expectedId,
            userId,
            accountName,
            balance,
            isMainAccount,
            editHistories,
            version
        );

        // When
        FinancialAccountId actual = financialAccount.id();

        // Then
        then(actual).isEqualTo(expectedId);
        then(financialAccount).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFinancialAccountData")
    void testUserId(
        FinancialAccountId id,
        UserId expectedUserId,
        AccountName accountName,
        Money balance,
        Boolean isMainAccount,
        Set<BalanceEditHistory> editHistories,
        Integer version,
        FinancialAccount financialAccount
    ) {
        // Given
        FinancialAccount expected = new FinancialAccount(
            id,
            expectedUserId,
            accountName,
            balance,
            isMainAccount,
            editHistories,
            version
        );

        // When
        UserId actual = financialAccount.userId();

        // Then
        then(actual).isEqualTo(expectedUserId);
        then(financialAccount).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFinancialAccountData")
    void testAccountName(
        FinancialAccountId id,
        UserId userId,
        AccountName expectedAccountName,
        Money balance,
        Boolean isMainAccount,
        Set<BalanceEditHistory> editHistories,
        Integer version,
        FinancialAccount financialAccount
    ) {
        // Given
        FinancialAccount expected = new FinancialAccount(
            id,
            userId,
            expectedAccountName,
            balance,
            isMainAccount,
            editHistories,
            version
        );

        // When
        AccountName actual = financialAccount.accountName();

        // Then
        then(actual).isEqualTo(expectedAccountName);
        then(financialAccount).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFinancialAccountData")
    void testBalance(
        FinancialAccountId id,
        UserId userId,
        AccountName accountName,
        Money expectedBalance,
        Boolean isMainAccount,
        Set<BalanceEditHistory> editHistories,
        Integer version,
        FinancialAccount financialAccount
    ) {
        // Given
        FinancialAccount expected = new FinancialAccount(
            id,
            userId,
            accountName,
            expectedBalance,
            isMainAccount,
            editHistories,
            version
        );

        // When
        Money actual = financialAccount.balance();

        // Then
        then(actual).isEqualTo(expectedBalance);
        then(financialAccount).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFinancialAccountData")
    void testIsMainAccount(
        FinancialAccountId id,
        UserId userId,
        AccountName accountName,
        Money balance,
        Boolean expectedIsMainAccount,
        Set<BalanceEditHistory> editHistories,
        Integer version,
        FinancialAccount financialAccount
    ) {
        // Given
        FinancialAccount expected = new FinancialAccount(
            id,
            userId,
            accountName,
            balance,
            expectedIsMainAccount,
            editHistories,
            version
        );

        // When
        Boolean actual = financialAccount.isMainAccount();

        // Then
        then(actual).isEqualTo(expectedIsMainAccount);
        then(financialAccount).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFinancialAccountData")
    void testEditHistories(
        FinancialAccountId id,
        UserId userId,
        AccountName accountName,
        Money balance,
        Boolean isMainAccount,
        Set<BalanceEditHistory> expectedEditHistories,
        Integer version,
        FinancialAccount financialAccount
    ) {
        // Given
        FinancialAccount expected = new FinancialAccount(
            id,
            userId,
            accountName,
            balance,
            isMainAccount,
            expectedEditHistories,
            version
        );

        // When
        Set<BalanceEditHistoryInfo> actual = financialAccount.editHistories();

        // Then
        then(actual).isEqualTo(expectedEditHistories);
        then(financialAccount).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFinancialAccountData")
    void testLatestEditHistoryEmpty(
        FinancialAccountId id,
        UserId userId,
        AccountName accountName,
        Money balance,
        Boolean isMainAccount,
        Set<BalanceEditHistory> editHistories,
        Integer version,
        FinancialAccount financialAccount
    ) {
        // Given
        // editHistories is empty

        // When
        Optional<BalanceEditHistoryInfo> actual = financialAccount.latestEditHistory();

        // Then
        then(actual).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideFinancialAccountData")
    void testLatestEditHistory(
        FinancialAccountId id,
        UserId userId,
        AccountName accountName,
        Money balance,
        Boolean isMainAccount,
        Set<BalanceEditHistory> editHistories,
        Integer version,
        FinancialAccount financialAccount
    ) {
        // Given
        LocalDateTime olderCreatedAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime newerCreatedAt = LocalDateTime.of(2025, 6, 1, 10, 0, 0);

        BalanceEditHistory olderHistory = new BalanceEditHistory(
            new BalanceEditHistoryId(UUID.randomUUID()),
            id,
            new Money(50_000),
            new Money(100_000),
            new Description("First edit"),
            LocalDate.of(2025, 1, 1),
            olderCreatedAt,
            Integer.valueOf(1)
        );
        BalanceEditHistory newerHistory = new BalanceEditHistory(
            new BalanceEditHistoryId(UUID.randomUUID()),
            id,
            new Money(100_000),
            new Money(150_000),
            new Description("Second edit"),
            LocalDate.of(2025, 6, 1),
            newerCreatedAt,
            Integer.valueOf(1)
        );

        Set<BalanceEditHistory> histories = new HashSet<>();
        histories.add(olderHistory);
        histories.add(newerHistory);

        FinancialAccount accountWithHistories = new FinancialAccount(
            id,
            userId,
            accountName,
            balance,
            isMainAccount,
            histories,
            version
        );

        // When
        Optional<BalanceEditHistoryInfo> actual = accountWithHistories.latestEditHistory();

        // Then
        then(actual).isPresent();
        then(actual.get().editedAt()).isEqualTo(LocalDate.of(2025, 6, 1));
        then(actual.get().oldBalance()).isEqualTo(new Money(100_000));
        then(actual.get().newBalance()).isEqualTo(new Money(150_000));
        then(actual.get().editReason()).isEqualTo(new Description("Second edit"));
    }

    @ParameterizedTest
    @MethodSource("provideFinancialAccountData")
    void testUpdateBalance(
        FinancialAccountId id,
        UserId userId,
        AccountName accountName,
        Money balance,
        Boolean isMainAccount,
        Set<BalanceEditHistory> editHistories,
        Integer version,
        FinancialAccount financialAccount
    ) {
        // Given
        Money newBalance = new Money(200_000);
        Description reason = new Description("Balance correction");
        LocalDate editedAt = LocalDate.of(2026, 2, 15);
        Money expectedOldBalance = balance;
        int expectedHistorySize = editHistories.size() + 1;

        // When
        financialAccount.updateBalance(newBalance, reason, editedAt);

        // Then
        then(financialAccount.balance()).isEqualTo(newBalance);

        Set<BalanceEditHistoryInfo> actualEditHistories = financialAccount.editHistories();
        then(actualEditHistories).hasSize(expectedHistorySize);

        then(actualEditHistories).anySatisfy(history -> {
                then(history.financialAccountId()).isEqualTo(id);
                then(history.oldBalance()).isEqualTo(expectedOldBalance);
                then(history.newBalance()).isEqualTo(newBalance);
                then(history.editReason()).isEqualTo(reason);
                then(history.editedAt()).isEqualTo(editedAt);
            });
    }
}
