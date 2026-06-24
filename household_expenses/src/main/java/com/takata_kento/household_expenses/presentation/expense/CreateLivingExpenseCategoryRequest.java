package com.takata_kento.household_expenses.presentation.expense;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 生活費分類の作成・更新リクエスト。
 *
 * @param name 分類名
 * @param description 説明
 */
public record CreateLivingExpenseCategoryRequest(@NotBlank String name, @NotNull String description) {}
