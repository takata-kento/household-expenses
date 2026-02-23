package com.takata_kento.household_expenses.domain.transaction.group;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.DailyGroupTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DailyGroupTransactionTest {

    static Stream<Arguments> provideDailyGroupTransactionData() {
        UUID uuid = UUID.randomUUID();
        UUID userGroupUUID = UUID.randomUUID();
        return Stream.of(
            Arguments.of(
                new DailyGroupTransactionId(uuid),
                new UserGroupId(userGroupUUID),
                LocalDate.of(2025, 12, 18),
                new HashSet<DailyLivingExpense>(),
                Integer.valueOf(1),
                new DailyGroupTransaction(
                    new DailyGroupTransactionId(uuid),
                    new UserGroupId(userGroupUUID),
                    LocalDate.of(2025, 12, 18),
                    new HashSet<DailyLivingExpense>(),
                    Integer.valueOf(1)
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideDailyGroupTransactionData")
    void testId(
        DailyGroupTransactionId expectedId,
        UserGroupId userGroupId,
        LocalDate transactionDate,
        Set<DailyLivingExpense> livingExpenses,
        Integer version,
        DailyGroupTransaction dailyGroupTransaction
    ) {
        // Given
        DailyGroupTransaction expected = new DailyGroupTransaction(
            expectedId,
            userGroupId,
            transactionDate,
            livingExpenses,
            version
        );

        // When
        DailyGroupTransactionId actual = dailyGroupTransaction.id();

        // Then
        then(actual).isEqualTo(expectedId);
        then(dailyGroupTransaction).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyGroupTransactionData")
    void testUserGroupId(
        DailyGroupTransactionId id,
        UserGroupId expectedUserGroupId,
        LocalDate transactionDate,
        Set<DailyLivingExpense> livingExpenses,
        Integer version,
        DailyGroupTransaction dailyGroupTransaction
    ) {
        // Given
        DailyGroupTransaction expected = new DailyGroupTransaction(
            id,
            expectedUserGroupId,
            transactionDate,
            livingExpenses,
            version
        );

        // When
        UserGroupId actual = dailyGroupTransaction.userGroupId();

        // Then
        then(actual).isEqualTo(expectedUserGroupId);
        then(dailyGroupTransaction).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyGroupTransactionData")
    void testTransactionDate(
        DailyGroupTransactionId id,
        UserGroupId userGroupId,
        LocalDate expectedTransactionDate,
        Set<DailyLivingExpense> livingExpenses,
        Integer version,
        DailyGroupTransaction dailyGroupTransaction
    ) {
        // Given
        DailyGroupTransaction expected = new DailyGroupTransaction(
            id,
            userGroupId,
            expectedTransactionDate,
            livingExpenses,
            version
        );

        // When
        LocalDate actual = dailyGroupTransaction.transactionDate();

        // Then
        then(actual).isEqualTo(expectedTransactionDate);
        then(dailyGroupTransaction).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyGroupTransactionData")
    void testLivingExpenses(
        DailyGroupTransactionId id,
        UserGroupId userGroupId,
        LocalDate transactionDate,
        Set<DailyLivingExpense> expectedLivingExpenses,
        Integer version,
        DailyGroupTransaction dailyGroupTransaction
    ) {
        // Given
        DailyGroupTransaction expected = new DailyGroupTransaction(
            id,
            userGroupId,
            transactionDate,
            expectedLivingExpenses,
            version
        );

        // When
        Set<DailyLivingExpense> actual = dailyGroupTransaction.livingExpenses();

        // Then
        then(actual).isEqualTo(expectedLivingExpenses);
        then(dailyGroupTransaction).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyGroupTransactionData")
    void testAddLivingExpense(
        DailyGroupTransactionId id,
        UserGroupId userGroupId,
        LocalDate transactionDate,
        Set<DailyLivingExpense> livingExpenses,
        Integer version,
        DailyGroupTransaction dailyGroupTransaction
    ) {
        // Given
        UserId userId = new UserId(UUID.randomUUID());
        LivingExpenseCategoryId categoryId = new LivingExpenseCategoryId(UUID.randomUUID());
        Money amount = new Money(1000);
        Description memo = new Description("test memo");
        int expectedSize = livingExpenses.size() + 1;

        // When
        dailyGroupTransaction.addLivingExpense(userId, categoryId, amount, memo);

        // Then
        Set<DailyLivingExpense> actualLivingExpenses = dailyGroupTransaction.livingExpenses();
        then(actualLivingExpenses).hasSize(expectedSize);

        then(actualLivingExpenses).anySatisfy(expense -> {
            then(expense.userId()).isEqualTo(userId);
            then(expense.livingExpenseCategoryId()).isEqualTo(categoryId);
            then(expense.amount()).isEqualTo(amount);
            then(expense.memo()).isEqualTo(memo);
            then(expense.dailyGroupTransactionId()).isEqualTo(id);
        });
    }

    @ParameterizedTest
    @MethodSource("provideDailyGroupTransactionData")
    void testCalculateTotalLivingExpense(
        DailyGroupTransactionId id,
        UserGroupId userGroupId,
        LocalDate transactionDate,
        Set<DailyLivingExpense> livingExpenses,
        Integer version,
        DailyGroupTransaction dailyGroupTransaction
    ) {
        // Given
        UserId userId1 = new UserId(UUID.randomUUID());
        LivingExpenseCategoryId categoryId1 = new LivingExpenseCategoryId(UUID.randomUUID());
        Money amount1 = new Money(1000);
        Description memo1 = new Description("test memo 1");

        UserId userId2 = new UserId(UUID.randomUUID());
        LivingExpenseCategoryId categoryId2 = new LivingExpenseCategoryId(UUID.randomUUID());
        Money amount2 = new Money(2000);
        Description memo2 = new Description("test memo 2");

        Money expectedTotal = amount1.add(amount2);

        dailyGroupTransaction.addLivingExpense(userId1, categoryId1, amount1, memo1);
        dailyGroupTransaction.addLivingExpense(userId2, categoryId2, amount2, memo2);

        // When
        Money actual = dailyGroupTransaction.calculateTotalLivingExpense();

        // Then
        then(actual).isEqualTo(expectedTotal);
    }
}
