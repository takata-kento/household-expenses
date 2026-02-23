package com.takata_kento.household_expenses.domain.saving;

import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.MonthlySavingId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("monthly_saving")
public class MonthlySaving {

    @Id
    private final MonthlySavingId id;

    @Column("user_id")
    private final UserId userId;

    @Column("year")
    private final Year year;

    @Column("month")
    private final Month month;

    @Column("saving_amount")
    private Money savingAmount;

    @Column("financial_account_id")
    private FinancialAccountId financialAccountId;

    @Column("memo")
    private Optional<Description> memo;

    @Version
    private final Integer version;

    public MonthlySaving(
        MonthlySavingId id,
        UserId userId,
        Year year,
        Month month,
        Money savingAmount,
        FinancialAccountId financialAccountId,
        Optional<Description> memo,
        Integer version
    ) {
        this.id = id;
        this.userId = userId;
        this.year = year;
        this.month = month;
        this.savingAmount = savingAmount;
        this.financialAccountId = financialAccountId;
        this.memo = memo;
        this.version = version;
    }

    public static MonthlySaving create(
        UserId userId,
        Year year,
        Month month,
        Money savingAmount,
        FinancialAccountId financialAccountId,
        Optional<Description> memo
    ) {
        MonthlySavingId id = new MonthlySavingId(UUID.randomUUID());
        return new MonthlySaving(id, userId, year, month, savingAmount, financialAccountId, memo, null);
    }

    public MonthlySavingId id() {
        return id;
    }

    public UserId userId() {
        return userId;
    }

    public Year year() {
        return year;
    }

    public Month month() {
        return month;
    }

    public Money savingAmount() {
        return savingAmount;
    }

    public FinancialAccountId financialAccountId() {
        return financialAccountId;
    }

    public Optional<Description> memo() {
        return memo;
    }

    public Integer version() {
        return version;
    }

    public void updateSavingAmount(Money savingAmount) {
        this.savingAmount = savingAmount;
    }

    public void updateFinancialAccount(FinancialAccountId financialAccountId) {
        this.financialAccountId = financialAccountId;
    }

    public void updateMemo(Optional<Description> memo) {
        this.memo = memo;
    }
}
