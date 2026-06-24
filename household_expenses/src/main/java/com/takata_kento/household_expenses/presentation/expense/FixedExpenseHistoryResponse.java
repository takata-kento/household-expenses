package com.takata_kento.household_expenses.presentation.expense;

import com.takata_kento.household_expenses.domain.expense.history.FixedExpenseHistory;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import java.time.LocalDate;

/**
 * 固定費履歴（月次の固定費実績）のレスポンス表現。
 *
 * @param id 履歴ID（UUID文字列）
 * @param categoryId 固定費分類ID（UUID文字列）
 * @param year 年
 * @param month 月
 * @param amount 金額
 * @param effectiveDate 適用開始日
 * @param memo メモ（未設定の場合は null）
 */
public record FixedExpenseHistoryResponse(
    String id,
    String categoryId,
    int year,
    int month,
    int amount,
    LocalDate effectiveDate,
    String memo
) {
    public static FixedExpenseHistoryResponse from(FixedExpenseHistory history) {
        return new FixedExpenseHistoryResponse(
            history.id().toString(),
            history.fixedExpenseCategoryId().toString(),
            history.year().value(),
            history.month().value(),
            history.amount().amount(),
            history.effectiveDate(),
            history.memo().map(Description::value).orElse(null)
        );
    }
}
