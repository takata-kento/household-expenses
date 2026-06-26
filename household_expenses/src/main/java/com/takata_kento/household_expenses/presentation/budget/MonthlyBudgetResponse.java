package com.takata_kento.household_expenses.presentation.budget;

import com.takata_kento.household_expenses.domain.budget.MonthlyBudget;

/**
 * 月次予算のレスポンス表現。
 *
 * @param id 予算ID（UUID文字列）
 * @param year 年
 * @param month 月
 * @param budgetAmount 予算額
 */
public record MonthlyBudgetResponse(String id, int year, int month, int budgetAmount) {
    public static MonthlyBudgetResponse from(MonthlyBudget budget) {
        return new MonthlyBudgetResponse(
            budget.id().toString(),
            budget.year().value(),
            budget.month().value(),
            budget.budgetAmount().amount()
        );
    }
}
