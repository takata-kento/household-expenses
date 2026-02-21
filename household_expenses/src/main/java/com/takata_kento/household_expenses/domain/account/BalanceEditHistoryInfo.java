package com.takata_kento.household_expenses.domain.account;

import com.takata_kento.household_expenses.domain.valueobject.BalanceEditHistoryId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import java.time.LocalDate;

public record BalanceEditHistoryInfo(
    BalanceEditHistoryId id,
    FinancialAccountId financialAccountId,
    Money oldBalance,
    Money newBalance,
    Description editReason,
    LocalDate editedAt
) {
    static BalanceEditHistoryInfo from(BalanceEditHistory history) {
        return new BalanceEditHistoryInfo(
            history.id(),
            history.financialAccountId(),
            history.oldBalance(),
            history.newBalance(),
            history.editReason(),
            history.editedAt()
        );
    }
}
