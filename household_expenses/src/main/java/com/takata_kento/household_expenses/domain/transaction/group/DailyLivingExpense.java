package com.takata_kento.household_expenses.domain.transaction.group;

import com.takata_kento.household_expenses.domain.valueobject.DailyGroupTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.DailyLivingExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("daily_living_expense")
class DailyLivingExpense {

    @Id
    private DailyLivingExpenseId id;

    @Column("daily_group_transaction_id")
    private DailyGroupTransactionId dailyGroupTransactionId;

    @Column("user_id")
    private UserId userId;

    @Column("living_expense_category_id")
    private LivingExpenseCategoryId livingExpenseCategoryId;

    @Column("amount")
    private Money amount;

    @Column("memo")
    private Description memo;

    @Version
    private Integer version;

    DailyLivingExpense(
        DailyLivingExpenseId id,
        DailyGroupTransactionId dailyGroupTransactionId,
        UserId userId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        this.id = id;
        this.dailyGroupTransactionId = dailyGroupTransactionId;
        this.userId = userId;
        this.livingExpenseCategoryId = livingExpenseCategoryId;
        this.amount = amount;
        this.memo = memo;
        this.version = version;
    }

    static DailyLivingExpense create(
        DailyGroupTransactionId dailyGroupTransactionId,
        UserId userId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo
    ) {
        DailyLivingExpenseId id = new DailyLivingExpenseId(UUID.randomUUID());
        return new DailyLivingExpense(id, dailyGroupTransactionId, userId, livingExpenseCategoryId, amount, memo, null);
    }

    DailyLivingExpenseId id() {
        return this.id;
    }

    DailyGroupTransactionId dailyGroupTransactionId() {
        return this.dailyGroupTransactionId;
    }

    UserId userId() {
        return this.userId;
    }

    LivingExpenseCategoryId livingExpenseCategoryId() {
        return this.livingExpenseCategoryId;
    }

    Money amount() {
        return this.amount;
    }

    Description memo() {
        return this.memo;
    }

    void updateUser(UserId userId) {
        if (userId == null) throw new IllegalArgumentException("userid must not be null");
        if (this.userId.equals(userId)) throw new IllegalArgumentException(
            "same parameter is detected when update userid of DailyLivingExpense"
        );

        this.userId = userId;
    }

    void updateLivingExpenseCategory(LivingExpenseCategoryId id) {
        if (id == null) throw new IllegalArgumentException("livingExpenseCategoryId must not be null");
        if (this.livingExpenseCategoryId.equals(id)) throw new IllegalArgumentException(
            "same parameter is detected when update livingExpenseCategoryId of DailyLivingExpense"
        );
        this.livingExpenseCategoryId = id;
    }

    void updateAmount(Money amount) {
        if (amount == null) throw new IllegalArgumentException("amount must not be null");
        if (this.amount.equals(amount)) throw new IllegalArgumentException(
            "same parameter is detected when update amount of DailyLivingExpense"
        );
        this.amount = amount;
    }

    void updateMemo(Description memo) {
        if (memo == null) throw new IllegalArgumentException("memo must not be null");
        if (this.memo.equals(memo)) throw new IllegalArgumentException(
            "same parameter is detected when update memo of DailyLivingExpense"
        );
        this.memo = memo;
    }
}
