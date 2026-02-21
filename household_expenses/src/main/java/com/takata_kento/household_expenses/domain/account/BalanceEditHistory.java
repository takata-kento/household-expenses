package com.takata_kento.household_expenses.domain.account;

import com.takata_kento.household_expenses.domain.valueobject.BalanceEditHistoryId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("balance_edit_history")
class BalanceEditHistory {

    @Id
    private BalanceEditHistoryId id;

    @Column("financial_account_id")
    private FinancialAccountId financialAccountId;

    @Column("old_balance")
    private Money oldBalance;

    @Column("new_balance")
    private Money newBalance;

    @Column("edit_reason")
    private Description editReason;

    @Column("edited_at")
    private LocalDate editedAt;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Version
    private Integer version;

    BalanceEditHistory(
        BalanceEditHistoryId id,
        FinancialAccountId financialAccountId,
        Money oldBalance,
        Money newBalance,
        Description editReason,
        LocalDate editedAt,
        LocalDateTime createdAt,
        Integer version
    ) {
        this.id = id;
        this.financialAccountId = financialAccountId;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.editReason = editReason;
        this.editedAt = editedAt;
        this.createdAt = createdAt;
        this.version = version;
    }

    static BalanceEditHistory create(
        FinancialAccountId financialAccountId,
        Money oldBalance,
        Money newBalance,
        Description editReason,
        LocalDate editedAt
    ) {
        BalanceEditHistoryId id = new BalanceEditHistoryId(UUID.randomUUID());
        return new BalanceEditHistory(id, financialAccountId, oldBalance, newBalance, editReason, editedAt, null, null);
    }

    BalanceEditHistoryId id() {
        return this.id;
    }

    FinancialAccountId financialAccountId() {
        return this.financialAccountId;
    }

    Money oldBalance() {
        return this.oldBalance;
    }

    Money newBalance() {
        return this.newBalance;
    }

    Description editReason() {
        return this.editReason;
    }

    LocalDate editedAt() {
        return this.editedAt;
    }

    LocalDateTime createdAt() {
        return this.createdAt;
    }
}
