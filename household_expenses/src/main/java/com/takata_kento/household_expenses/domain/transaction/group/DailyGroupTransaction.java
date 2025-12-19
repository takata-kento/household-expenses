package com.takata_kento.household_expenses.domain.transaction.group;

import com.takata_kento.household_expenses.domain.valueobject.DailyGroupTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("daily_group_transaction")
public class DailyGroupTransaction {

    @Id
    @Column("id")
    private DailyGroupTransactionId id;

    @Column("user_group_id")
    private UserGroupId userGroupId;

    @Column("transaction_date")
    private LocalDate transactionDate;

    @MappedCollection(idColumn = "daily_group_transaction_id", keyColumn = "created_at")
    private List<DailyLivingExpense> livingExpenses;

    @Version
    private Integer version;

    public DailyGroupTransaction(
        DailyGroupTransactionId id,
        UserGroupId userGroupId,
        LocalDate transactionDate,
        List<DailyLivingExpense> livingExpenses,
        Integer version
    ) {
        this.id = id;
        this.userGroupId = userGroupId;
        this.transactionDate = transactionDate;
        this.livingExpenses = livingExpenses != null ? new ArrayList<>(livingExpenses) : new ArrayList<>();
        this.version = version;
    }

    public DailyGroupTransactionId id() {
        return this.id;
    }

    public UserGroupId userGroupId() {
        return this.userGroupId;
    }

    public LocalDate transactionDate() {
        return this.transactionDate;
    }

    public List<DailyLivingExpense> livingExpenses() {
        return new ArrayList<>(this.livingExpenses);
    }

    public void addLivingExpense(
        UserId userId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        Money amount,
        Description memo
    ) {
        DailyLivingExpense livingExpense = DailyLivingExpense.create(
            this.id,
            userId,
            livingExpenseCategoryId,
            amount,
            memo
        );
        this.livingExpenses.add(livingExpense);
    }

    public Money calculateTotalLivingExpense() {
        return this.livingExpenses.stream().map(DailyLivingExpense::amount).reduce(new Money(0), Money::add);
    }
}
