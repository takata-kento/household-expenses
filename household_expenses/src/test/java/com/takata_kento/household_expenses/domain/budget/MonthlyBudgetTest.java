package com.takata_kento.household_expenses.domain.budget;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.MonthlyBudgetId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MonthlyBudgetTest {

    // Basic Given
    final MonthlyBudgetId EXPECTED_ID = new MonthlyBudgetId(1L);
    final UserGroupId EXPECTED_USER_GROUP_ID = new UserGroupId(1L);
    final Year EXPECTED_YEAR = new Year(2024);
    final Month EXPECTED_MONTH = new Month(6);
    final Money EXPECTED_BUDGET_AMOUNT = new Money(100000);
    final UUID USER_UUID = UUID.randomUUID();
    final UserId EXPECTED_SET_BY_USER_ID = new UserId(USER_UUID);
    final LocalDateTime EXPECTED_CREATED_AT = LocalDateTime.of(2024, 6, 1, 10, 0, 0);
    final LocalDateTime EXPECTED_UPDATED_AT = LocalDateTime.of(2024, 6, 10, 10, 0, 0);
    final Integer EXPECTED_VERSION = 0;

    @Test
    void testCreate() {
        // When
        MonthlyBudget actual = MonthlyBudget.create(
            EXPECTED_USER_GROUP_ID,
            EXPECTED_YEAR,
            EXPECTED_MONTH,
            EXPECTED_BUDGET_AMOUNT,
            EXPECTED_SET_BY_USER_ID
        );

        // Then
        assertThat(actual.id()).isNotNull();
        assertThat(actual.userGroupId()).isEqualTo(EXPECTED_USER_GROUP_ID);
        assertThat(actual.year()).isEqualTo(EXPECTED_YEAR);
        assertThat(actual.month()).isEqualTo(EXPECTED_MONTH);
        assertThat(actual.budgetAmount()).isEqualTo(EXPECTED_BUDGET_AMOUNT);
        assertThat(actual.setByUserId()).isEqualTo(EXPECTED_SET_BY_USER_ID);
        assertThat(actual.createdAt()).isNotNull();
        assertThat(actual.updatedAt()).isNull();
        assertThat(actual.version()).isNull();
    }

    @Test
    void testUpdateBudgetAmount() {
        // Given
        MonthlyBudget budget = new MonthlyBudget(
            EXPECTED_ID,
            EXPECTED_USER_GROUP_ID,
            EXPECTED_YEAR,
            EXPECTED_MONTH,
            EXPECTED_BUDGET_AMOUNT,
            EXPECTED_SET_BY_USER_ID,
            EXPECTED_CREATED_AT,
            null,
            EXPECTED_VERSION
        );

        Money expectedBudgetAmount = new Money(120000);
        UserId expectedSetByUserId = new UserId(UUID.randomUUID());

        // When
        budget.updateBudgetAmount(expectedBudgetAmount, expectedSetByUserId);

        // Then
        assertThat(budget.id()).isEqualTo(EXPECTED_ID);
        assertThat(budget.userGroupId()).isEqualTo(EXPECTED_USER_GROUP_ID);
        assertThat(budget.year()).isEqualTo(EXPECTED_YEAR);
        assertThat(budget.month()).isEqualTo(EXPECTED_MONTH);
        assertThat(budget.budgetAmount()).isEqualTo(expectedBudgetAmount);
        assertThat(budget.setByUserId()).isEqualTo(expectedSetByUserId);
        assertThat(budget.createdAt()).isEqualTo(EXPECTED_CREATED_AT);
        assertThat(budget.updatedAt()).isNotNull();
        assertThat(budget.version()).isEqualTo(EXPECTED_VERSION);
    }

    @Test
    void testIsCurrentMonth() {
        // Given
        MonthlyBudget budget = setUpBudget();

        LocalDate currentDate = LocalDate.of(2024, 6, 15);
        LocalDate differentDate = LocalDate.of(2024, 7, 15);

        // When
        boolean actualIsCurrentMonth = budget.isCurrentMonth(currentDate);
        boolean actualIsNotCurrentMonth = budget.isCurrentMonth(differentDate);

        // Then
        fieldCheck(budget);
        assertThat(actualIsCurrentMonth).isTrue();
        assertThat(actualIsNotCurrentMonth).isFalse();
    }

    @Test
    void testIsSetBy() {
        // Given
        MonthlyBudget budget = setUpBudget();

        UserId differentUserId = new UserId(UUID.randomUUID());

        // When
        boolean actualIsSetByUser = budget.isSetBy(EXPECTED_SET_BY_USER_ID);
        boolean actualIsNotSetByUser = budget.isSetBy(differentUserId);

        // Then
        fieldCheck(budget);
        assertThat(actualIsSetByUser).isTrue();
        assertThat(actualIsNotSetByUser).isFalse();
    }

    @Test
    void testCalculateRemainingBudget() {
        // Given
        MonthlyBudget budget = setUpBudget();

        Money usedAmount = new Money(30000);
        Money expectedRemainingBudget = new Money(70000);

        // When
        Money actualRemainingBudget = budget.calculateRemainingBudget(usedAmount);

        // Then
        fieldCheck(budget);
        assertThat(actualRemainingBudget).isEqualTo(expectedRemainingBudget);
    }

    @Test
    void testIsOverBudget() {
        // Given
        MonthlyBudget budget = setUpBudget();

        Money underBudgetAmount = new Money(80000);
        Money overBudgetAmount = new Money(120000);

        // When
        boolean actualIsUnderBudget = budget.isOverBudget(underBudgetAmount);
        boolean actualIsOverBudget = budget.isOverBudget(overBudgetAmount);

        // Then
        fieldCheck(budget);
        assertThat(actualIsUnderBudget).isFalse();
        assertThat(actualIsOverBudget).isTrue();
    }

    MonthlyBudget setUpBudget() {
        return new MonthlyBudget(
            EXPECTED_ID,
            EXPECTED_USER_GROUP_ID,
            EXPECTED_YEAR,
            EXPECTED_MONTH,
            EXPECTED_BUDGET_AMOUNT,
            EXPECTED_SET_BY_USER_ID,
            EXPECTED_CREATED_AT,
            EXPECTED_UPDATED_AT,
            EXPECTED_VERSION
        );
    }

    void fieldCheck(MonthlyBudget budget) {
        assertThat(budget.id()).isEqualTo(EXPECTED_ID);
        assertThat(budget.userGroupId()).isEqualTo(EXPECTED_USER_GROUP_ID);
        assertThat(budget.year()).isEqualTo(EXPECTED_YEAR);
        assertThat(budget.month()).isEqualTo(EXPECTED_MONTH);
        assertThat(budget.budgetAmount()).isEqualTo(EXPECTED_BUDGET_AMOUNT);
        assertThat(budget.setByUserId()).isEqualTo(EXPECTED_SET_BY_USER_ID);
        assertThat(budget.createdAt()).isEqualTo(EXPECTED_CREATED_AT);
        assertThat(budget.updatedAt()).isEqualTo(EXPECTED_UPDATED_AT);
        assertThat(budget.version()).isEqualTo(EXPECTED_VERSION);
    }
}
