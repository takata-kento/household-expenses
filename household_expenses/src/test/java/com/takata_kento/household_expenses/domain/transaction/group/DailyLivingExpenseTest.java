package com.takata_kento.household_expenses.domain.transaction.group;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.DailyGroupTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.DailyLivingExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DailyLivingExpenseTest {

    static Stream<Arguments> provideDailyLivingExpenseData() {
        UUID dailyLivingExpenseUUID = UUID.randomUUID();
        UUID dailyGroupTransactionUUID = UUID.randomUUID();
        return Stream.of(
            Arguments.of(
                new DailyLivingExpenseId(dailyLivingExpenseUUID),
                new DailyGroupTransactionId(dailyGroupTransactionUUID),
                new UserId(100L),
                new LivingExpenseCategoryId(100L),
                new Money(10_000),
                new Description("Description"),
                Integer.valueOf(1),
                new DailyLivingExpense(
                    new DailyLivingExpenseId(dailyLivingExpenseUUID),
                    new DailyGroupTransactionId(dailyGroupTransactionUUID),
                    new UserId(100L),
                    new LivingExpenseCategoryId(100L),
                    new Money(10_000),
                    new Description("Description"),
                    Integer.valueOf(1)
                )
            )
        );
    }

    static Stream<DailyLivingExpense> provideDailyLivingExpenseInstance() {
        return Stream.of(
            new DailyLivingExpense(
                new DailyLivingExpenseId(UUID.randomUUID()),
                new DailyGroupTransactionId(UUID.randomUUID()),
                new UserId(100L),
                new LivingExpenseCategoryId(100L),
                new Money(10_000),
                new Description("Description"),
                Integer.valueOf(1)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testId(
        DailyLivingExpenseId expectedId,
        DailyGroupTransactionId dailyGroupTransactionId,
        UserId userId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version,
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        DailyLivingExpense expected = new DailyLivingExpense(
            expectedId,
            dailyGroupTransactionId,
            userId,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        DailyLivingExpenseId actual = dailyLivingExpense.id();

        // Then
        then(actual).isEqualTo(expectedId);
        then(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testUserId(
        DailyLivingExpenseId id,
        DailyGroupTransactionId dailyGroupTransactionId,
        UserId expectedUserId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version,
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            dailyGroupTransactionId,
            expectedUserId,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        UserId actual = dailyLivingExpense.userId();

        // Then
        then(actual).isEqualTo(expectedUserId);
        then(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testDailyGroupTransactionId(
        DailyLivingExpenseId id,
        DailyGroupTransactionId expectedDailyGroupTransactionId,
        UserId userId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version,
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            expectedDailyGroupTransactionId,
            userId,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        DailyGroupTransactionId actual = dailyLivingExpense.dailyGroupTransactionId();

        // Then
        then(actual).isEqualTo(expectedDailyGroupTransactionId);
        then(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testLivingExpenseCategoryId(
        DailyLivingExpenseId id,
        DailyGroupTransactionId dailyGroupTransactionId,
        UserId userId,
        LivingExpenseCategoryId expectedLivingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version,
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            dailyGroupTransactionId,
            userId,
            expectedLivingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        LivingExpenseCategoryId actual = dailyLivingExpense.livingExpenseCategoryId();

        // Then
        then(actual).isEqualTo(expectedLivingExpenseCategoryId);
        then(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testAmount(
        DailyLivingExpenseId id,
        DailyGroupTransactionId dailyGroupTransactionId,
        UserId userId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money expectedAmount,
        Description memo,
        Integer version,
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            dailyGroupTransactionId,
            userId,
            livingExpenseCategoryId,
            expectedAmount,
            memo,
            version
        );

        // When
        Money actual = dailyLivingExpense.amount();

        // Then
        then(actual).isEqualTo(expectedAmount);
        then(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testMemo(
        DailyLivingExpenseId id,
        DailyGroupTransactionId dailyGroupTransactionId,
        UserId userId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description expectedMemo,
        Integer version,
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            dailyGroupTransactionId,
            userId,
            livingExpenseCategoryId,
            amount,
            expectedMemo,
            version
        );

        // When
        Description actual = dailyLivingExpense.memo();

        // Then
        then(actual).isEqualTo(expectedMemo);
        then(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testUpdateUser(
        DailyLivingExpenseId id,
        DailyGroupTransactionId dailyGroupTransactionId,
        UserId previousUserId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version,
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        UserId expectedUserId = new UserId(200L);

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            dailyGroupTransactionId,
            expectedUserId,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        dailyLivingExpense.updateUser(expectedUserId);

        // Then
        then(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseInstance")
    void testThrowSameUserErrorWhenUpdateUser(
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        UserId sameUserId = new UserId(100L);

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyLivingExpense.updateUser(sameUserId)
        );

        // Then
        then(actual).hasMessage("same parameter is detected when update userid of DailyLivingExpense");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseInstance")
    void testNullCheckWhenUpdateUser(
        DailyLivingExpense dailyLivingExpense
    ) {
        // When
        IllegalArgumentException exception = catchIllegalArgumentException(() -> dailyLivingExpense.updateUser(null));

        // Then
        then(exception).hasMessage("userid must not be null");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testUpdateLivingExpenseCategory(
        DailyLivingExpenseId id,
        DailyGroupTransactionId dailyGroupTransactionId,
        UserId userId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version,
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        LivingExpenseCategoryId expectedCategory = new LivingExpenseCategoryId(200);

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            dailyGroupTransactionId,
            userId,
            expectedCategory,
            amount,
            memo,
            version
        );

        // When
        dailyLivingExpense.updateLivingExpenseCategory(expectedCategory);

        // Then
        then(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseInstance")
    void testThrowSameCategoryErrorWhenUpdateLivingExpenseCategory(
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        LivingExpenseCategoryId sameCategory = new LivingExpenseCategoryId(100);

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyLivingExpense.updateLivingExpenseCategory(sameCategory)
        );

        // Then
        then(actual).hasMessage(
            "same parameter is detected when update livingExpenseCategoryId of DailyLivingExpense"
        );
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseInstance")
    void testNullCheckWhenUpdateLivingExpenseCategory(
        DailyLivingExpense dailyLivingExpense
    ) {
        // When
        IllegalArgumentException exception = catchIllegalArgumentException(() ->
            dailyLivingExpense.updateLivingExpenseCategory(null)
        );

        // Then
        then(exception).hasMessage("livingExpenseCategoryId must not be null");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testUpdateAmount(
        DailyLivingExpenseId id,
        DailyGroupTransactionId dailyGroupTransactionId,
        UserId userId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version,
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        Money expectedAmount = new Money(20_000);

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            dailyGroupTransactionId,
            userId,
            livingExpenseCategoryId,
            expectedAmount,
            memo,
            version
        );

        // When
        dailyLivingExpense.updateAmount(expectedAmount);

        // Then
        then(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseInstance")
    void testThrowSameAmountErrorWhenUpdate(
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        Money sameAmount = new Money(10_000);

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyLivingExpense.updateAmount(sameAmount)
        );

        // Then
        then(actual).hasMessage("same parameter is detected when update amount of DailyLivingExpense");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseInstance")
    void testNullCheckWhenUpdateAmount(
        DailyLivingExpense dailyLivingExpense
    ) {
        // When
        IllegalArgumentException exception = catchIllegalArgumentException(() -> dailyLivingExpense.updateAmount(null));

        // Then
        then(exception).hasMessage("amount must not be null");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testUpdateMemo(
        DailyLivingExpenseId id,
        DailyGroupTransactionId dailyGroupTransactionId,
        UserId userId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version,
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        Description expectedMemo = new Description("updated description");

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            dailyGroupTransactionId,
            userId,
            livingExpenseCategoryId,
            amount,
            expectedMemo,
            version
        );

        // When
        dailyLivingExpense.updateMemo(expectedMemo);

        // Then
        then(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseInstance")
    void testThrowSameDescriptionErrorWhenUpdateMemo(
        DailyLivingExpense dailyLivingExpense
    ) {
        // Given
        Description sameMemo = new Description("Description");

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() -> dailyLivingExpense.updateMemo(sameMemo));

        // Then
        then(actual).hasMessage("same parameter is detected when update memo of DailyLivingExpense");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseInstance")
    void testNullCheckWhenUpdateMemo(
        DailyLivingExpense dailyLivingExpense
    ) {
        // When
        IllegalArgumentException exception = catchIllegalArgumentException(() -> dailyLivingExpense.updateMemo(null));

        // Then
        then(exception).hasMessage("memo must not be null");
    }
}
