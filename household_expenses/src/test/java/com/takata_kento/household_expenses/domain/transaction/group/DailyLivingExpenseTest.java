package com.takata_kento.household_expenses.domain.transaction.group;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.DailyLivingExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DailyLivingExpenseTest {

    static Stream<Arguments> provideDailyLivingExpenseData() {
        return Stream.of(
            Arguments.of(
                new DailyLivingExpenseId(1L),
                new UserId(100L),
                LocalDate.of(2024, 6, 1),
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
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            expectedId,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        DailyLivingExpense expected = new DailyLivingExpense(
            expectedId,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        DailyLivingExpenseId actual = dailyLivingExpense.id();

        // Then
        assertThat(actual).isEqualTo(expectedId);
        assertThat(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testUserId(
        DailyLivingExpenseId id,
        UserId expectedUserId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            expectedUserId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            expectedUserId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        UserId actual = dailyLivingExpense.userId();

        // Then
        assertThat(actual).isEqualTo(expectedUserId);
        assertThat(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testTransactionDate(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate expectedTransactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            expectedTransactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            userId,
            expectedTransactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        LocalDate actual = dailyLivingExpense.transactionDate();

        // Then
        assertThat(actual).isEqualTo(expectedTransactionDate);
        assertThat(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testLivingExpenseCategoryId(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId expectedLivingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            expectedLivingExpenseCategoryId,
            amount,
            memo,
            version
        );

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            expectedLivingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        LivingExpenseCategoryId actual = dailyLivingExpense.livingExpenseCategoryId();

        // Then
        assertThat(actual).isEqualTo(expectedLivingExpenseCategoryId);
        assertThat(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testAmount(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money expectedAmount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            expectedAmount,
            memo,
            version
        );

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            expectedAmount,
            memo,
            version
        );

        // When
        Money actual = dailyLivingExpense.amount();

        // Then
        assertThat(actual).isEqualTo(expectedAmount);
        assertThat(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testMemo(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description expectedMemo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            expectedMemo,
            version
        );

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            expectedMemo,
            version
        );

        // When
        Description actual = dailyLivingExpense.memo();

        // Then
        assertThat(actual).isEqualTo(expectedMemo);
        assertThat(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testUpdateUser(
        DailyLivingExpenseId id,
        UserId previousUserId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            previousUserId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        UserId expectedUserId = new UserId(200L);

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            expectedUserId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        dailyLivingExpense.updateUser(expectedUserId);

        // Then
        assertThat(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testThrowSameUserErrorWhenUpdateUser(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        UserId sameUserId = new UserId(100L);

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyLivingExpense.updateUser(sameUserId)
        );

        // Then
        assertThat(actual).hasMessage("same parameter is detected when update userid of DailyLivingExpense");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testNullCheckWhenUpdateUser(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        IllegalArgumentException exception = catchIllegalArgumentException(() -> dailyLivingExpense.updateUser(null));

        // Then
        assertThat(exception).hasMessage("userid must not be null");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testUpdateTransactionDate(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        LocalDate expectedTransactionDate = LocalDate.of(2025, 6, 1);

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            userId,
            expectedTransactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        // When
        dailyLivingExpense.updateTransactionDate(expectedTransactionDate);

        // Then
        assertThat(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testThrowSameDateErrorWhenUpdateTransactionDate(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money money,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            money,
            memo,
            version
        );

        LocalDate sameTransactionDate = LocalDate.of(2024, 6, 1);

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyLivingExpense.updateTransactionDate(sameTransactionDate)
        );

        // Then
        assertThat(actual).hasMessage("same parameter is detected when update transactionDate of DailyLivingExpense");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testNullCheckWhenUpdateTransactionDate(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money money,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            money,
            memo,
            version
        );

        // When
        IllegalArgumentException exception = catchIllegalArgumentException(() ->
            dailyLivingExpense.updateTransactionDate(null)
        );

        // Then
        assertThat(exception).hasMessage("transactionDate must not be null");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testUpdateLivingExpenseCategory(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        LivingExpenseCategoryId expectedCategory = new LivingExpenseCategoryId(200);

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            expectedCategory,
            amount,
            memo,
            version
        );

        // When
        dailyLivingExpense.updateLivingExpenseCategory(expectedCategory);

        // Then
        assertThat(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testThrowSameCategoryErrorWhenUpdateLivingExpenseCategory(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money money,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            money,
            memo,
            version
        );

        LivingExpenseCategoryId sameCategory = new LivingExpenseCategoryId(100);

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyLivingExpense.updateLivingExpenseCategory(sameCategory)
        );

        // Then
        assertThat(actual).hasMessage(
            "same parameter is detected when update livingExpenseCategoryId of DailyLivingExpense"
        );
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testNullCheckWhenUpdateLivingExpenseCategory(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money money,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            money,
            memo,
            version
        );

        // When
        IllegalArgumentException exception = catchIllegalArgumentException(() ->
            dailyLivingExpense.updateLivingExpenseCategory(null)
        );

        // Then
        assertThat(exception).hasMessage("livingExpenseCategoryId must not be null");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testUpdateAmount(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        Money expectedAmount = new Money(20_000);

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            expectedAmount,
            memo,
            version
        );

        // When
        dailyLivingExpense.updateAmount(expectedAmount);

        // Then
        assertThat(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testThrowSameAmountErrorWhenUpdate(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money money,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            money,
            memo,
            version
        );

        Money sameAmount = new Money(10_000);

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyLivingExpense.updateAmount(sameAmount)
        );

        // Then
        assertThat(actual).hasMessage("same parameter is detected when update amount of DailyLivingExpense");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testNullCheckWhenUpdateAmount(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money money,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            money,
            memo,
            version
        );

        // When
        IllegalArgumentException exception = catchIllegalArgumentException(() -> dailyLivingExpense.updateAmount(null));

        // Then
        assertThat(exception).hasMessage("amount must not be null");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testUpdateMemo(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            memo,
            version
        );

        Description expectedMemo = new Description("updated description");

        DailyLivingExpense expected = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            amount,
            expectedMemo,
            version
        );

        // When
        dailyLivingExpense.updateMemo(expectedMemo);

        // Then
        assertThat(dailyLivingExpense).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testThrowSameDescriptionErrorWhenUpdateMemo(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money money,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            money,
            memo,
            version
        );

        Description sameMemo = new Description("Description");

        // When
        IllegalArgumentException actual = catchIllegalArgumentException(() ->
            dailyLivingExpense.updateMemo(sameMemo)
        );

        // Then
        assertThat(actual).hasMessage("same parameter is detected when update memo of DailyLivingExpense");
    }

    @ParameterizedTest
    @MethodSource("provideDailyLivingExpenseData")
    void testNullCheckWhenUpdateMemo(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money money,
        Description memo,
        Integer version
    ) {
        // Given
        DailyLivingExpense dailyLivingExpense = new DailyLivingExpense(
            id,
            userId,
            transactionDate,
            livingExpenseCategoryId,
            money,
            memo,
            version
        );

        // When
        IllegalArgumentException exception = catchIllegalArgumentException(() -> dailyLivingExpense.updateMemo(null));

        // Then
        assertThat(exception).hasMessage("memo must not be null");
    }
}
