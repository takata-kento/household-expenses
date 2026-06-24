package com.takata_kento.household_expenses.presentation.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * 日次個人支出1件のリクエスト。
 *
 * @param amount 金額
 * @param memo 使用目的
 */
public record PersonalExpenseRequest(@NotNull @PositiveOrZero Integer amount, @NotNull String memo) {}
