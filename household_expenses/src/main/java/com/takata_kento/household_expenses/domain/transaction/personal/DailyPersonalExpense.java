package com.takata_kento.household_expenses.domain.transaction.personal;

import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("daily_personal_expense")
class DailyPersonalExpense {

    @Id
    private DailyPersonalExpenseId id;

    @Column("daily_personal_transaction_id")
    private DailyPersonalTransactionId dailyPersonalTransactionId;

    @Column("amount")
    private Money amount;

    @Column("memo")
    private Description memo;

    @Version
    private Integer version;

    DailyPersonalExpense(
        DailyPersonalExpenseId id,
        DailyPersonalTransactionId dailyPersonalTransactionId,
        Money amount,
        Description memo,
        Integer version
    ) {
        this.id = id;
        this.dailyPersonalTransactionId = dailyPersonalTransactionId;
        this.amount = amount;
        this.memo = memo;
        this.version = version;
    }

    static DailyPersonalExpense create(
        DailyPersonalTransactionId dailyPersonalTransactionId,
        Money amount,
        Description memo
    ) {
        DailyPersonalExpenseId id = new DailyPersonalExpenseId(UUID.randomUUID());
        return new DailyPersonalExpense(id, dailyPersonalTransactionId, amount, memo, null);
    }

    DailyPersonalExpenseId id() {
        return this.id;
    }

    DailyPersonalTransactionId dailyPersonalTransactionId() {
        return this.dailyPersonalTransactionId;
    }

    Money amount() {
        return this.amount;
    }

    Description memo() {
        return this.memo;
    }

    void updateAmount(Money amount) {
        if (amount == null) throw new IllegalArgumentException("amount must not be null");
        if (this.amount.equals(amount)) throw new IllegalArgumentException(
            "same parameter is detected when update amount of DailyPersonalExpense"
        );
        this.amount = amount;
    }

    void updateMemo(Description memo) {
        if (memo == null) throw new IllegalArgumentException("memo must not be null");
        if (this.memo.equals(memo)) throw new IllegalArgumentException(
            "same parameter is detected when update memo of DailyPersonalExpense"
        );
        this.memo = memo;
    }
}
