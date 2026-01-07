package com.takata_kento.household_expenses.domain.transaction.personal;

import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.Money;

public record DailyPersonalExpenseInfo(DailyPersonalExpenseId id, Money amount, Description memo) {
    public static DailyPersonalExpenseInfo from(DailyPersonalExpense dailyPersonalExpense) {
        return new DailyPersonalExpenseInfo(
            dailyPersonalExpense.id(),
            dailyPersonalExpense.amount(),
            dailyPersonalExpense.memo()
        );
    }
}
