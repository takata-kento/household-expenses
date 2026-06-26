package com.takata_kento.household_expenses.presentation.budget;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * 月次予算の設定リクエスト。年月が既存の場合は金額を更新する。
 *
 * @param year 年
 * @param month 月
 * @param budgetAmount 予算額
 */
public record SetMonthlyBudgetRequest(
    @NotNull Integer year,
    @NotNull Integer month,
    @NotNull @PositiveOrZero Integer budgetAmount
) {}
