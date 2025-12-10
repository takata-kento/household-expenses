package com.takata_kento.household_expenses.domain.transaction.group;

import com.takata_kento.household_expenses.domain.valueobject.DailyLivingExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("daily_living_expense")
class DailyLivingExpense {

    @Id
    private DailyLivingExpenseId id;

    @Column("user_id")
    private UserId userId;

    @Column("transaction_date")
    private LocalDate transactionDate;

    @Column("living_expense_category_id")
    private LivingExpenseCategoryId livingExpenseCategoryId;

    @Column("amount")
    private Money amount;

    @Column("memo")
    private Description memo;

    @Version
    private Integer version;

    public DailyLivingExpense(
        DailyLivingExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo,
        Integer version
    ) {
        this.id = id;
        this.userId = userId;
        this.transactionDate = transactionDate;
        this.livingExpenseCategoryId = livingExpenseCategoryId;
        this.amount = amount;
        this.memo = memo;
        this.version = version;
    }

    public static DailyLivingExpense create(
        UserId userId,
        LocalDate transactionDate,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo
    ) {
        Long idValue = Math.abs(java.util.UUID.randomUUID().getLeastSignificantBits());
        DailyLivingExpenseId id = new DailyLivingExpenseId(idValue);
        return new DailyLivingExpense(id, userId, transactionDate, livingExpenseCategoryId, amount, memo, null);
    }

    public DailyLivingExpenseId id() {
        return this.id;
    }

    public UserId userId() {
        return this.userId;
    }

    public LocalDate transactionDate() {
        return this.transactionDate;
    }

    public LivingExpenseCategoryId livingExpenseCategoryId() {
        return this.livingExpenseCategoryId;
    }

    public Money amount() {
        return this.amount;
    }

    public Description memo() {
        return this.memo;
    }

    public void updateUser(UserId userId) {
        if (userId == null) throw new IllegalArgumentException("userid must not be null");
        if (this.userId.equals(userId)) throw new IllegalArgumentException(
            "same parameter is detected when update userid of DailyLivingExpense"
        );

        this.userId = userId;
    }

    public void updateTransactionDate(LocalDate transactionDate) {
        if (transactionDate == null) throw new IllegalArgumentException("transactionDate must not be null");
        if (this.transactionDate.equals(transactionDate)) throw new IllegalArgumentException(
            "same parameter is detected when update transactionDate of DailyLivingExpense"
        );
        this.transactionDate = transactionDate;
    }

    public void updateLivingExpenseCategory(LivingExpenseCategoryId id) {
        if (id == null) throw new IllegalArgumentException("livingExpenseCategoryId must not be null");
        if (this.livingExpenseCategoryId.equals(id)) throw new IllegalArgumentException(
            "same parameter is detected when update livingExpenseCategoryId of DailyLivingExpense"
        );
        this.livingExpenseCategoryId = id;
    }

    public void updateAmount(Money amount) {
        if (amount == null) throw new IllegalArgumentException("amount must not be null");
        if (this.amount.equals(amount)) throw new IllegalArgumentException(
            "same parameter is detected when update amount of DailyLivingExpense"
        );
        this.amount = amount;
    }

    public void updateMemo(Description memo) {
        if (memo == null) throw new IllegalArgumentException("memo must not be null");
        if (this.memo.equals(memo)) throw new IllegalArgumentException("same parameter is detected when update memo of DailyLivingExpense");
        this.memo = memo;
    }
}
