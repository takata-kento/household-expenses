package com.takata_kento.household_expenses.presentation.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * 日次生活費1件のリクエスト。
 *
 * @param categoryId 生活費分類ID（UUID文字列）
 * @param amount 金額
 * @param memo メモ
 */
public record LivingExpenseRequest(
    @NotBlank String categoryId,
    @NotNull @PositiveOrZero Integer amount,
    @NotNull String memo
) {}
