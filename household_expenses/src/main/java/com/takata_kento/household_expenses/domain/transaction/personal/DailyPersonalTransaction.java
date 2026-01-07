package com.takata_kento.household_expenses.domain.transaction.personal;

import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("daily_personal_transaction")
public class DailyPersonalTransaction {

    @Id
    @Column("id")
    private DailyPersonalTransactionId id;

    @Column("user_id")
    private UserId userId;

    @Column("transaction_date")
    private LocalDate transactionDate;

    @Column("income")
    private Money income;

    @MappedCollection(idColumn = "daily_personal_transaction_id")
    private List<DailyPersonalExpense> personalExpenses;

    @Version
    private Integer version;

    public DailyPersonalTransaction(
        DailyPersonalTransactionId id,
        UserId userId,
        LocalDate transactionDate,
        Money income,
        List<DailyPersonalExpense> personalExpenses,
        Integer version
    ) {
        this.id = id;
        this.userId = userId;
        this.transactionDate = transactionDate;
        this.income = income;
        this.personalExpenses = personalExpenses != null ? new ArrayList<>(personalExpenses) : new ArrayList<>();
        this.version = version;
    }

    public DailyPersonalTransactionId id() {
        return this.id;
    }

    public UserId userId() {
        return this.userId;
    }

    public LocalDate transactionDate() {
        return this.transactionDate;
    }

    public Money income() {
        return this.income;
    }

    public List<DailyPersonalExpenseInfo> personalExpenses() {
        return this.personalExpenses.stream().map(DailyPersonalExpenseInfo::from).toList();
    }

    public void addPersonalExpense(Money amount, Description memo) {
        DailyPersonalExpense personalExpense = DailyPersonalExpense.create(this.id, amount, memo);
        this.personalExpenses.add(personalExpense);
    }

    public void updateIncome(Money income) {
        if (income == null) throw new IllegalArgumentException("income must not be null");
        if (this.income.equals(income)) throw new IllegalArgumentException(
            "same parameter is detected when update income of DailyPersonalTransaction"
        );
        this.income = income;
    }

    public Money calculateTotalPersonalExpense() {
        return this.personalExpenses.stream().map(DailyPersonalExpense::amount).reduce(new Money(0), Money::add);
    }
}
