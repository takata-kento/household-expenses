package com.takata_kento.household_expenses.application.budget;

import com.takata_kento.household_expenses.domain.budget.MonthlyBudget;

/**
 * 月次予算の設定（upsert）結果。
 *
 * @param budget 設定後の月次予算
 * @param created 新規作成された場合は {@code true}、既存予算の更新の場合は {@code false}
 */
public record SetMonthlyBudgetResult(MonthlyBudget budget, boolean created) {}
