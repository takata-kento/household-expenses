package com.takata_kento.household_expenses.presentation.expense;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * 固定費分類の作成リクエスト。
 *
 * @param name 分類名
 * @param description 説明
 * @param defaultAmount デフォルト金額
 */
public record CreateFixedExpenseCategoryRequest(
    @NotBlank String name,
    @NotNull String description,
    @NotNull @PositiveOrZero Integer defaultAmount
) {}
