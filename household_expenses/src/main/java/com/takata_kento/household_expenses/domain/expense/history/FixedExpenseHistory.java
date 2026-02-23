package com.takata_kento.household_expenses.domain.expense.history;

import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseHistoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("fixed_expense_history")
public class FixedExpenseHistory {

    @Id
    private final FixedExpenseHistoryId id;

    @Column("fixed_expense_category_id")
    private final FixedExpenseCategoryId fixedExpenseCategoryId;

    @Column("year")
    private final Year year;

    @Column("month")
    private final Month month;

    @Column("amount")
    private Money amount;

    @Column("effective_date")
    private LocalDate effectiveDate;

    @Column("memo")
    private Optional<Description> memo;

    @Version
    private final Integer version;

    public FixedExpenseHistory(
        FixedExpenseHistoryId id,
        FixedExpenseCategoryId fixedExpenseCategoryId,
        Year year,
        Month month,
        Money amount,
        LocalDate effectiveDate,
        Optional<Description> memo,
        Integer version
    ) {
        this.id = id;
        this.fixedExpenseCategoryId = fixedExpenseCategoryId;
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.effectiveDate = effectiveDate;
        this.memo = memo;
        this.version = version;
    }

    public static FixedExpenseHistory create(
        FixedExpenseCategoryId fixedExpenseCategoryId,
        Year year,
        Month month,
        Money amount,
        LocalDate effectiveDate,
        Optional<Description> memo
    ) {
        UUID idValue = UUID.randomUUID();
        FixedExpenseHistoryId id = new FixedExpenseHistoryId(idValue);
        return new FixedExpenseHistory(id, fixedExpenseCategoryId, year, month, amount, effectiveDate, memo, null);
    }

    public FixedExpenseHistoryId id() {
        return id;
    }

    public FixedExpenseCategoryId fixedExpenseCategoryId() {
        return fixedExpenseCategoryId;
    }

    public Year year() {
        return year;
    }

    public Month month() {
        return month;
    }

    public Money amount() {
        return amount;
    }

    public LocalDate effectiveDate() {
        return effectiveDate;
    }

    public Optional<Description> memo() {
        return memo;
    }

    public Integer version() {
        return version;
    }

    public void updateAmount(Money amount) {
        this.amount = amount;
    }

    public void updateEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void updateMemo(Optional<Description> memo) {
        this.memo = memo;
    }
}
