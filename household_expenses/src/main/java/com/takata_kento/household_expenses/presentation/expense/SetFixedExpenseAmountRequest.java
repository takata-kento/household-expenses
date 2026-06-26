package com.takata_kento.household_expenses.presentation.expense;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

/**
 * 記録月の固定費金額の設定リクエスト。年月が既存の場合は更新する。
 *
 * @param year 年
 * @param month 月
 * @param amount 金額
 * @param effectiveDate 適用開始日
 * @param memo メモ（省略可）
 */
public record SetFixedExpenseAmountRequest(
    @NotNull Integer year,
    @NotNull Integer month,
    @NotNull @PositiveOrZero Integer amount,
    @NotNull LocalDate effectiveDate,
    String memo
) {}
