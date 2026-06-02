package com.takata_kento.household_expenses.domain.account;

import com.takata_kento.household_expenses.domain.valueobject.AccountName;
import com.takata_kento.household_expenses.domain.valueobject.BankName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("financial_account")
public class FinancialAccount {

    @Id
    @Column("id")
    private FinancialAccountId id;

    @Column("user_id")
    private UserId userId;

    @Column("bank_name")
    private BankName bankName;

    @Column("account_name")
    private Optional<AccountName> accountName;

    @Column("balance")
    private Money balance;

    @Column("is_main_account")
    private Boolean isMainAccount;

    @MappedCollection(idColumn = "financial_account_id")
    private Set<BalanceEditHistory> editHistories;

    @Version
    private Integer version;

    public FinancialAccount(
        FinancialAccountId id,
        UserId userId,
        BankName bankName,
        Optional<AccountName> accountName,
        Money balance,
        Boolean isMainAccount,
        Set<BalanceEditHistory> editHistories,
        Integer version
    ) {
        this.id = id;
        this.userId = userId;
        this.bankName = bankName;
        this.accountName = accountName != null ? accountName : Optional.empty();
        this.balance = balance;
        this.isMainAccount = isMainAccount;
        this.editHistories = editHistories != null ? new HashSet<>(editHistories) : new HashSet<>();
        this.version = version;
    }

    public FinancialAccountId id() {
        return this.id;
    }

    public UserId userId() {
        return this.userId;
    }

    public BankName bankName() {
        return this.bankName;
    }

    public Optional<AccountName> accountName() {
        return this.accountName;
    }

    public Money balance() {
        return this.balance;
    }

    public Boolean isMainAccount() {
        return this.isMainAccount;
    }

    public Set<BalanceEditHistoryInfo> editHistories() {
        return this.editHistories.stream().map(BalanceEditHistoryInfo::from).collect(Collectors.toUnmodifiableSet());
    }

    private void addEditHistory(
        Money oldBalance,
        Money newBalance,
        Optional<Description> editReason,
        LocalDate editedAt
    ) {
        BalanceEditHistory history = BalanceEditHistory.create(this.id, oldBalance, newBalance, editReason, editedAt);
        this.editHistories.add(history);
    }

    public Optional<BalanceEditHistoryInfo> latestEditHistory() {
        return this.editHistories.stream()
            .max(Comparator.comparing(BalanceEditHistory::createdAt, Comparator.nullsFirst(Comparator.naturalOrder())))
            .map(BalanceEditHistoryInfo::from);
    }

    public void updateBalance(Money newBalance, LocalDate editedAt) {
        addEditHistory(this.balance, newBalance, Optional.empty(), editedAt);
        this.balance = newBalance;
    }

    public void updateBalance(Money newBalance, Description reason, LocalDate editedAt) {
        addEditHistory(this.balance, newBalance, Optional.of(reason), editedAt);
        this.balance = newBalance;
    }

    public void updateAccountName(AccountName newAccountName) {
        this.accountName = Optional.of(newAccountName);
    }
}
