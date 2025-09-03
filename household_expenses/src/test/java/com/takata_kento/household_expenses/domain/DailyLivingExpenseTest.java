package com.takata_kento.household_expenses.domain;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DailyLivingExpenseTest {

    static Stream<Arguments> provideConstructorParameter() {
        return Stream.of(
            Arguments.of(
                new DailyLivingExpenseId(1L),
                new UserId(1L),
                new UserGroupId(1L),
                new LivingExpenseCategoryId(1L),
                LocalDate.of(2024, 1, 1),
                new Money(1000),
                new Description("test memo"),
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 1, 11, 0),
                1
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideConstructorParameter")
    void testConstructor(
        DailyLivingExpenseId expectedId,
        UserId expectedUserId,
        UserGroupId expectedUserGroupId,
        LivingExpenseCategoryId expectedLivingExpenseCategoryId,
        LocalDate expectedTransactionDate,
        Money expectedAmount,
        Description expectedMemo,
        LocalDateTime expectedCreatedAt,
        LocalDateTime expectedUpdatedAt,
        Integer expectedVersion
    ) {
        // When
        DailyLivingExpense actual = new DailyLivingExpense(
            expectedId,
            expectedUserId,
            expectedUserGroupId,
            expectedLivingExpenseCategoryId,
            expectedTransactionDate,
            expectedAmount,
            expectedMemo,
            expectedCreatedAt,
            expectedUpdatedAt,
            expectedVersion
        );

        // Then
        assertThat(actual.id()).isEqualTo(expectedId);
        assertThat(actual.userId()).isEqualTo(expectedUserId);
        assertThat(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actual.livingExpenseCategoryId()).isEqualTo(expectedLivingExpenseCategoryId);
        assertThat(actual.transactionDate()).isEqualTo(expectedTransactionDate);
        assertThat(actual.amount()).isEqualTo(expectedAmount);
        assertThat(actual.memo()).isEqualTo(expectedMemo);
        assertThat(actual.createdAt()).isEqualTo(expectedCreatedAt);
        assertThat(actual.updatedAt()).isEqualTo(expectedUpdatedAt);
        assertThat(actual.version()).isEqualTo(expectedVersion);
    }

    @ParameterizedTest
    @MethodSource("provideConstructorParameter")
    void testId(
        DailyLivingExpenseId expectedId,
        UserId userId,
        UserGroupId userGroupId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        LocalDate transactionDate,
        Money amount,
        Description memo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            expectedId,
            userId,
            userGroupId,
            livingExpenseCategoryId,
            transactionDate,
            amount,
            memo,
            createdAt,
            updatedAt,
            version
        );

        // When
        DailyLivingExpenseId actual = dailyLivingExpense.id();

        // Then
        assertThat(actual).isEqualTo(expectedId);
    }

    @ParameterizedTest
    @MethodSource("provideConstructorParameter")
    void testUserId(
        DailyLivingExpenseId id,
        UserId expectedUserId,
        UserGroupId userGroupId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        LocalDate transactionDate,
        Money amount,
        Description memo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            expectedUserId,
            userGroupId,
            livingExpenseCategoryId,
            transactionDate,
            amount,
            memo,
            createdAt,
            updatedAt,
            version
        );

        // When
        UserId actual = dailyLivingExpense.userId();

        // Then
        assertThat(actual).isEqualTo(expectedUserId);
    }

    @ParameterizedTest
    @MethodSource("provideConstructorParameter")
    void testUserGroupId(
        DailyLivingExpenseId id,
        UserId userId,
        UserGroupId expectedUserGroupId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        LocalDate transactionDate,
        Money amount,
        Description memo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            expectedUserGroupId,
            livingExpenseCategoryId,
            transactionDate,
            amount,
            memo,
            createdAt,
            updatedAt,
            version
        );

        // When
        UserGroupId actual = dailyLivingExpense.userGroupId();

        // Then
        assertThat(actual).isEqualTo(expectedUserGroupId);
    }

    @ParameterizedTest
    @MethodSource("provideConstructorParameter")
    void testLivingExpenseCategoryId(
        DailyLivingExpenseId id,
        UserId userId,
        UserGroupId userGroupId,
        LivingExpenseCategoryId expectedLivingExpenseCategoryId,
        LocalDate transactionDate,
        Money amount,
        Description memo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            userGroupId,
            expectedLivingExpenseCategoryId,
            transactionDate,
            amount,
            memo,
            createdAt,
            updatedAt,
            version
        );

        // When
        LivingExpenseCategoryId actual = dailyLivingExpense.livingExpenseCategoryId();

        // Then
        assertThat(actual).isEqualTo(expectedLivingExpenseCategoryId);
    }

    @ParameterizedTest
    @MethodSource("provideConstructorParameter")
    void testTransactionDate(
        DailyLivingExpenseId id,
        UserId userId,
        UserGroupId userGroupId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        LocalDate expectedTransactionDate,
        Money amount,
        Description memo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            userGroupId,
            livingExpenseCategoryId,
            expectedTransactionDate,
            amount,
            memo,
            createdAt,
            updatedAt,
            version
        );

        // When
        LocalDate actual = dailyLivingExpense.transactionDate();

        // Then
        assertThat(actual).isEqualTo(expectedTransactionDate);
    }

    @ParameterizedTest
    @MethodSource("provideConstructorParameter")
    void testAmount(
        DailyLivingExpenseId id,
        UserId userId,
        UserGroupId userGroupId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        LocalDate transactionDate,
        Money expectedAmount,
        Description memo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            userGroupId,
            livingExpenseCategoryId,
            transactionDate,
            expectedAmount,
            memo,
            createdAt,
            updatedAt,
            version
        );

        // When
        Money actual = dailyLivingExpense.amount();

        // Then
        assertThat(actual).isEqualTo(expectedAmount);
    }

    @ParameterizedTest
    @MethodSource("provideConstructorParameter")
    void testMemo(
        DailyLivingExpenseId id,
        UserId userId,
        UserGroupId userGroupId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        LocalDate transactionDate,
        Money amount,
        Description expectedMemo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            userGroupId,
            livingExpenseCategoryId,
            transactionDate,
            amount,
            expectedMemo,
            createdAt,
            updatedAt,
            version
        );

        // When
        Description actual = dailyLivingExpense.memo();

        // Then
        assertThat(actual).isEqualTo(expectedMemo);
    }

    @ParameterizedTest
    @MethodSource("provideConstructorParameter")
    void testCreatedAt(
        DailyLivingExpenseId id,
        UserId userId,
        UserGroupId userGroupId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        LocalDate transactionDate,
        Money amount,
        Description memo,
        LocalDateTime expectedCreatedAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            userGroupId,
            livingExpenseCategoryId,
            transactionDate,
            amount,
            memo,
            expectedCreatedAt,
            updatedAt,
            version
        );

        // When
        LocalDateTime actual = dailyLivingExpense.createdAt();

        // Then
        assertThat(actual).isEqualTo(expectedCreatedAt);
    }

    @ParameterizedTest
    @MethodSource("provideConstructorParameter")
    void testUpdatedAt(
        DailyLivingExpenseId id,
        UserId userId,
        UserGroupId userGroupId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        LocalDate transactionDate,
        Money amount,
        Description memo,
        LocalDateTime createdAt,
        LocalDateTime expectedUpdatedAt,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            userGroupId,
            livingExpenseCategoryId,
            transactionDate,
            amount,
            memo,
            createdAt,
            expectedUpdatedAt,
            version
        );

        // When
        LocalDateTime actual = dailyLivingExpense.updatedAt();

        // Then
        assertThat(actual).isEqualTo(expectedUpdatedAt);
    }

    @ParameterizedTest
    @MethodSource("provideConstructorParameter")
    void testVersion(
        DailyLivingExpenseId id,
        UserId userId,
        UserGroupId userGroupId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        LocalDate transactionDate,
        Money amount,
        Description memo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer expectedVersion
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            userGroupId,
            livingExpenseCategoryId,
            transactionDate,
            amount,
            memo,
            createdAt,
            updatedAt,
            expectedVersion
        );

        // When
        Integer actual = dailyLivingExpense.version();

        // Then
        assertThat(actual).isEqualTo(expectedVersion);
    }

    @Test
    void testOf() {
        // Given
        UserId expectedUserId = new UserId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(1L);
        LivingExpenseCategoryId expectedLivingExpenseCategoryId = new LivingExpenseCategoryId(1L);
        LocalDate expectedTransactionDate = LocalDate.of(2024, 1, 1);
        Money expectedAmount = new Money(1000);
        Description expectedMemo = new Description("test memo");

        // When
        DailyLivingExpense actual = DailyLivingExpense.of(
            expectedUserId,
            expectedUserGroupId,
            expectedLivingExpenseCategoryId,
            expectedTransactionDate,
            expectedAmount,
            expectedMemo
        );

        // Then
        assertThat(actual.userId()).isEqualTo(expectedUserId);
        assertThat(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actual.livingExpenseCategoryId()).isEqualTo(expectedLivingExpenseCategoryId);
        assertThat(actual.transactionDate()).isEqualTo(expectedTransactionDate);
        assertThat(actual.amount()).isEqualTo(expectedAmount);
        assertThat(actual.memo()).isEqualTo(expectedMemo);
        assertThat(actual.id()).isNotNull();
        assertThat(actual.createdAt()).isNotNull();
        assertThat(actual.updatedAt()).isNull();
        assertThat(actual.version()).isNull();
    }
}
