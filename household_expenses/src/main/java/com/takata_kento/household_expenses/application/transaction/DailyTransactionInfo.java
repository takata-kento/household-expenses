package com.takata_kento.household_expenses.application.transaction;

import com.takata_kento.household_expenses.domain.transaction.group.DailyLivingExpenseInfo;
import com.takata_kento.household_expenses.domain.transaction.personal.DailyPersonalExpenseInfo;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import java.time.LocalDate;
import java.util.List;

/**
 * 1日の収支情報（閲覧／記録結果）の集約表現。
 *
 * <p>{@code livingExpenses} はその日のグループ全員分（共有データ）、{@code personalExpenses} は
 * 現在ユーザー分のみ（非共有データ）。{@code totalExpense} は
 * {@code 切り上げ(生活費合計 ÷ グループ人数) + 個人支出合計}、{@code budgetBalance} は予算残金。
 */
public record DailyTransactionInfo(
    LocalDate transactionDate,
    Money income,
    List<DailyLivingExpenseInfo> livingExpenses,
    List<DailyPersonalExpenseInfo> personalExpenses,
    Money totalLivingExpense,
    Money totalPersonalExpense,
    Money totalExpense,
    Money budgetBalance
) {}
