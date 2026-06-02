package com.takata_kento.household_expenses.application.saving;

import com.takata_kento.household_expenses.domain.account.FinancialAccount;
import com.takata_kento.household_expenses.domain.account.FinancialAccountRepository;
import com.takata_kento.household_expenses.domain.saving.MonthlySaving;
import com.takata_kento.household_expenses.domain.saving.MonthlySavingRepository;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.user.UserRepository;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SavingService {

    private final MonthlySavingRepository monthlySavingRepository;
    private final FinancialAccountRepository financialAccountRepository;
    private final UserRepository userRepository;

    public SavingService(
        MonthlySavingRepository monthlySavingRepository,
        FinancialAccountRepository financialAccountRepository,
        UserRepository userRepository
    ) {
        this.monthlySavingRepository = monthlySavingRepository;
        this.financialAccountRepository = financialAccountRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(UserId userId) {
        return userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalStateException("User not found: " + userId));
    }

    private void verifyAccountOwnedBy(FinancialAccountId financialAccountId, UserId userId) {
        FinancialAccount account = financialAccountRepository
            .findById(financialAccountId)
            .orElseThrow(() -> new IllegalStateException("FinancialAccount not found: " + financialAccountId));
        if (!account.userId().equals(userId)) {
            throw new IllegalStateException("FinancialAccount is not owned by current user");
        }
    }

    private MonthlySaving loadOwnedSaving(Year year, Month month, UserId userId) {
        return monthlySavingRepository
            .findByUserIdAndYearAndMonth(userId, year, month)
            .orElseThrow(() ->
                new IllegalStateException("MonthlySaving not found for " + year.value() + "-" + month.value())
            );
    }

    public MonthlySaving recordMonthlySaving(
        UserId currentUserId,
        Year year,
        Month month,
        Money savingAmount,
        FinancialAccountId financialAccountId,
        Optional<Description> memo
    ) {
        User currentUser = getCurrentUser(currentUserId);
        if (monthlySavingRepository.findByUserIdAndYearAndMonth(currentUser.id(), year, month).isPresent()) {
            throw new IllegalStateException("MonthlySaving already exists for " + year.value() + "-" + month.value());
        }
        verifyAccountOwnedBy(financialAccountId, currentUser.id());
        MonthlySaving saving = MonthlySaving.create(
            currentUser.id(),
            year,
            month,
            savingAmount,
            financialAccountId,
            memo
        );
        return monthlySavingRepository.save(saving);
    }

    public MonthlySaving getMonthlySaving(UserId currentUserId, Year year, Month month) {
        User currentUser = getCurrentUser(currentUserId);
        return loadOwnedSaving(year, month, currentUser.id());
    }

    public MonthlySaving updateMonthlySaving(
        UserId currentUserId,
        Year year,
        Month month,
        Money savingAmount,
        FinancialAccountId financialAccountId,
        Optional<Description> memo
    ) {
        User currentUser = getCurrentUser(currentUserId);
        MonthlySaving saving = loadOwnedSaving(year, month, currentUser.id());
        verifyAccountOwnedBy(financialAccountId, currentUser.id());
        saving.updateSavingAmount(savingAmount);
        saving.updateFinancialAccount(financialAccountId);
        saving.updateMemo(memo);
        return monthlySavingRepository.save(saving);
    }

    public void deleteMonthlySaving(UserId currentUserId, Year year, Month month) {
        User currentUser = getCurrentUser(currentUserId);
        MonthlySaving saving = loadOwnedSaving(year, month, currentUser.id());
        monthlySavingRepository.delete(saving);
    }

    public List<MonthlySaving> getSavingsByYear(UserId currentUserId, Year year) {
        User currentUser = getCurrentUser(currentUserId);
        return monthlySavingRepository.findByUserIdAndYear(currentUser.id(), year);
    }
}
