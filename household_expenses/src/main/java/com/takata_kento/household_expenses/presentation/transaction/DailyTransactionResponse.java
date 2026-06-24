package com.takata_kento.household_expenses.presentation.transaction;

import com.takata_kento.household_expenses.application.transaction.DailyTransactionInfo;
import java.time.LocalDate;
import java.util.List;

/**
 * 1日の収支情報のレスポンス表現。
 *
 * @param transactionDate 取引日
 * @param income 収入
 * @param livingExpenses 生活費明細（グループ全員分）
 * @param personalExpenses 個人支出明細（本人分）
 * @param totalLivingExpense 生活費合計
 * @param totalPersonalExpense 個人支出合計
 * @param totalExpense 支出合計（切り上げ(生活費合計÷人数)＋個人支出合計）
 * @param budgetBalance 予算残金
 */
public record DailyTransactionResponse(
    LocalDate transactionDate,
    int income,
    List<LivingExpenseItem> livingExpenses,
    List<PersonalExpenseItem> personalExpenses,
    int totalLivingExpense,
    int totalPersonalExpense,
    int totalExpense,
    int budgetBalance
) {
    /** 生活費明細1件。 */
    public record LivingExpenseItem(String id, String userId, String categoryId, int amount, String memo) {}

    /** 個人支出明細1件。 */
    public record PersonalExpenseItem(String id, int amount, String memo) {}

    public static DailyTransactionResponse from(DailyTransactionInfo info) {
        List<LivingExpenseItem> living = info
            .livingExpenses()
            .stream()
            .map(expense ->
                new LivingExpenseItem(
                    expense.id().toString(),
                    expense.userId().toString(),
                    expense.livingExpenseCategoryId().toString(),
                    expense.amount().amount(),
                    expense.memo().value()
                )
            )
            .toList();
        List<PersonalExpenseItem> personal = info
            .personalExpenses()
            .stream()
            .map(expense -> new PersonalExpenseItem(expense.id().toString(), expense.amount().amount(), expense.memo().value()))
            .toList();
        return new DailyTransactionResponse(
            info.transactionDate(),
            info.income().amount(),
            living,
            personal,
            info.totalLivingExpense().amount(),
            info.totalPersonalExpense().amount(),
            info.totalExpense().amount(),
            info.budgetBalance().amount()
        );
    }
}
