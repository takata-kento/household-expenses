package com.takata_kento.household_expenses.presentation.expense;

import com.takata_kento.household_expenses.domain.expense.category.LivingExpenseCategory;

/**
 * 生活費分類のレスポンス表現。
 *
 * @param id 分類ID（UUID文字列）
 * @param categoryName 分類名
 * @param description 説明
 * @param isDefault デフォルト分類フラグ
 */
public record LivingExpenseCategoryResponse(String id, String categoryName, String description, boolean isDefault) {
    public static LivingExpenseCategoryResponse from(LivingExpenseCategory category) {
        return new LivingExpenseCategoryResponse(
            category.id().toString(),
            category.categoryName().value(),
            category.description().value(),
            category.isDefault()
        );
    }
}
