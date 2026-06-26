package com.takata_kento.household_expenses.presentation.expense;

import com.takata_kento.household_expenses.domain.expense.category.FixedExpenseCategory;

/**
 * 固定費分類のレスポンス表現。
 *
 * @param id 分類ID（UUID文字列）
 * @param categoryName 分類名
 * @param description 説明
 * @param defaultAmount デフォルト金額
 */
public record FixedExpenseCategoryResponse(String id, String categoryName, String description, int defaultAmount) {
    public static FixedExpenseCategoryResponse from(FixedExpenseCategory category) {
        return new FixedExpenseCategoryResponse(
            category.id().toString(),
            category.categoryName().value(),
            category.description().value(),
            category.defaultAmount().amount()
        );
    }
}
