package com.takata_kento.household_expenses.domain.transaction.group;

import com.takata_kento.household_expenses.domain.valueobject.DailyLivingExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;

public record DailyLivingExpenseInfo(
    DailyLivingExpenseId id,
    UserId userId,
    LivingExpenseCategoryId livingExpenseCategoryId,
    Money amount,
    Description memo
) {
    public static DailyLivingExpenseInfo from(DailyLivingExpense dailyLivingExpense) {
        return new DailyLivingExpenseInfo(
            dailyLivingExpense.id(),
            dailyLivingExpense.userId(),
            dailyLivingExpense.livingExpenseCategoryId(),
            dailyLivingExpense.amount(),
            dailyLivingExpense.memo()
        );
    }
}
