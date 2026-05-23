package com.takata_kento.household_expenses.application.account;

import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.account.FinancialAccount;
import com.takata_kento.household_expenses.domain.account.FinancialAccountRepository;
import com.takata_kento.household_expenses.domain.valueobject.AccountName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    private final FinancialAccountRepository financialAccountRepository;

    public AccountService(FinancialAccountRepository financialAccountRepository) {
        this.financialAccountRepository = financialAccountRepository;
    }

    public FinancialAccount createAccount(
        FinancialAccountId id,
        AccountName accountName,
        Money initialBalance,
        Boolean isMainAccount
    ) {
        UserId userId = CognitoUserContext.currentUserId();
        if (isMainAccount) {
            boolean alreadyHasMain = financialAccountRepository
                .findByUserId(userId)
                .stream()
                .anyMatch(FinancialAccount::isMainAccount);
            if (alreadyHasMain) {
                throw new IllegalStateException("User already has a main account");
            }
        }
        FinancialAccount account = new FinancialAccount(
            id,
            userId,
            accountName,
            initialBalance,
            isMainAccount,
            new HashSet<>(),
            null
        );
        return financialAccountRepository.save(account);
    }

    public FinancialAccount updateBalance(FinancialAccountId id, Money newBalance) {
        FinancialAccount account = findOwnedAccount(id);
        account.updateBalance(newBalance, LocalDate.now());
        return financialAccountRepository.save(account);
    }

    public FinancialAccount updateBalance(FinancialAccountId id, Money newBalance, Description reason) {
        FinancialAccount account = findOwnedAccount(id);
        account.updateBalance(newBalance, reason, LocalDate.now());
        return financialAccountRepository.save(account);
    }

    public Money calculateNewBalance(
        FinancialAccountId id,
        Money income,
        Money totalExpense,
        Money fixedExpense,
        Money saving
    ) {
        FinancialAccount account = findOwnedAccount(id);
        return account.balance().add(income).subtract(totalExpense).subtract(fixedExpense).subtract(saving);
    }

    public List<FinancialAccount> getUserAccounts() {
        UserId userId = CognitoUserContext.currentUserId();
        return financialAccountRepository.findByUserId(userId);
    }

    private FinancialAccount findOwnedAccount(FinancialAccountId id) {
        UserId userId = CognitoUserContext.currentUserId();
        FinancialAccount account = financialAccountRepository
            .findById(id)
            .orElseThrow(() -> new IllegalStateException("FinancialAccount not found: " + id));
        if (!account.userId().equals(userId)) {
            throw new IllegalStateException("FinancialAccount is not owned by the current user");
        }
        return account;
    }
}
