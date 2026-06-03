package com.takata_kento.household_expenses.application.transaction;

import com.takata_kento.household_expenses.application.budget.BudgetService;
import com.takata_kento.household_expenses.domain.transaction.group.DailyGroupTransaction;
import com.takata_kento.household_expenses.domain.transaction.group.DailyGroupTransactionRepository;
import com.takata_kento.household_expenses.domain.transaction.group.DailyLivingExpenseInfo;
import com.takata_kento.household_expenses.domain.transaction.personal.DailyPersonalExpenseInfo;
import com.takata_kento.household_expenses.domain.transaction.personal.DailyPersonalTransaction;
import com.takata_kento.household_expenses.domain.transaction.personal.DailyPersonalTransactionRepository;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.user.UserRepository;
import com.takata_kento.household_expenses.domain.valueobject.DailyGroupTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionService {

    private final DailyGroupTransactionRepository dailyGroupTransactionRepository;
    private final DailyPersonalTransactionRepository dailyPersonalTransactionRepository;
    private final UserRepository userRepository;
    private final BudgetService budgetService;

    public TransactionService(
        DailyGroupTransactionRepository dailyGroupTransactionRepository,
        DailyPersonalTransactionRepository dailyPersonalTransactionRepository,
        UserRepository userRepository,
        BudgetService budgetService
    ) {
        this.dailyGroupTransactionRepository = dailyGroupTransactionRepository;
        this.dailyPersonalTransactionRepository = dailyPersonalTransactionRepository;
        this.userRepository = userRepository;
        this.budgetService = budgetService;
    }

    private User getCurrentUser(UserId userId) {
        return userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalStateException("User not found: " + userId));
    }

    private UserGroupId currentUserGroupId(User currentUser) {
        return currentUser
            .userGroupId()
            .orElseThrow(() -> new IllegalStateException("User does not belong to any group"));
    }

    private int groupMemberCount(UserGroupId userGroupId) {
        int count = userRepository.findByUserGroupId(userGroupId).size();
        if (count <= 0) {
            throw new IllegalStateException("UserGroup has no members: " + userGroupId);
        }
        return count;
    }

    public Money calculateTotalExpense(Money totalLivingExpense, Money totalPersonalExpense, int groupMemberCount) {
        if (groupMemberCount <= 0) {
            throw new IllegalArgumentException("groupMemberCount must be positive");
        }
        int perPerson = (totalLivingExpense.amount() + groupMemberCount - 1) / groupMemberCount;
        return new Money(perPerson).add(totalPersonalExpense);
    }

    public DailyTransactionInfo recordDailyTransaction(
        UserId currentUserId,
        LocalDate transactionDate,
        Money income,
        List<LivingExpenseInput> livingExpenses,
        List<PersonalExpenseInput> personalExpenses
    ) {
        User currentUser = getCurrentUser(currentUserId);
        UserGroupId userGroupId = currentUserGroupId(currentUser);
        if (dailyPersonalTransactionRepository.existsByUserIdAndTransactionDate(currentUser.id(), transactionDate)) {
            throw new IllegalStateException("DailyPersonalTransaction already exists for " + transactionDate);
        }

        DailyGroupTransaction groupTransaction = dailyGroupTransactionRepository
            .findByUserGroupIdAndTransactionDate(userGroupId, transactionDate)
            .orElseGet(() ->
                new DailyGroupTransaction(
                    new DailyGroupTransactionId(UUID.randomUUID()),
                    userGroupId,
                    transactionDate,
                    new HashSet<>(),
                    null
                )
            );
        for (LivingExpenseInput input : livingExpenses) {
            groupTransaction.addLivingExpense(currentUser.id(), input.categoryId(), input.amount(), input.memo());
        }
        DailyGroupTransaction savedGroupTransaction = dailyGroupTransactionRepository.save(groupTransaction);

        DailyPersonalTransaction personalTransaction = new DailyPersonalTransaction(
            new DailyPersonalTransactionId(UUID.randomUUID()),
            currentUser.id(),
            transactionDate,
            income,
            new ArrayList<>(),
            null
        );
        for (PersonalExpenseInput input : personalExpenses) {
            personalTransaction.addPersonalExpense(input.amount(), input.memo());
        }
        DailyPersonalTransaction savedPersonalTransaction = dailyPersonalTransactionRepository.save(
            personalTransaction
        );

        return buildInfo(
            currentUser.id(),
            transactionDate,
            savedGroupTransaction,
            savedPersonalTransaction,
            groupMemberCount(userGroupId)
        );
    }

    public DailyTransactionInfo getDailyTransaction(UserId currentUserId, LocalDate transactionDate) {
        User currentUser = getCurrentUser(currentUserId);
        UserGroupId userGroupId = currentUserGroupId(currentUser);
        DailyGroupTransaction groupTransaction = dailyGroupTransactionRepository
            .findByUserGroupIdAndTransactionDate(userGroupId, transactionDate)
            .orElse(null);
        DailyPersonalTransaction personalTransaction = dailyPersonalTransactionRepository
            .findByUserIdAndTransactionDate(currentUser.id(), transactionDate)
            .orElse(null);
        return buildInfo(
            currentUser.id(),
            transactionDate,
            groupTransaction,
            personalTransaction,
            groupMemberCount(userGroupId)
        );
    }

    public DailyTransactionInfo updateDailyTransaction(
        UserId currentUserId,
        LocalDate transactionDate,
        Money income,
        List<LivingExpenseInput> livingExpenses,
        List<PersonalExpenseInput> personalExpenses
    ) {
        User currentUser = getCurrentUser(currentUserId);
        UserGroupId userGroupId = currentUserGroupId(currentUser);

        DailyPersonalTransaction personalTransaction = dailyPersonalTransactionRepository
            .findByUserIdAndTransactionDate(currentUser.id(), transactionDate)
            .orElseThrow(() -> new IllegalStateException("DailyPersonalTransaction not found for " + transactionDate));
        if (!personalTransaction.income().equals(income)) {
            personalTransaction.updateIncome(income);
        }
        personalTransaction.clearPersonalExpenses();
        for (PersonalExpenseInput input : personalExpenses) {
            personalTransaction.addPersonalExpense(input.amount(), input.memo());
        }
        DailyPersonalTransaction savedPersonalTransaction = dailyPersonalTransactionRepository.save(
            personalTransaction
        );

        DailyGroupTransaction groupTransaction = dailyGroupTransactionRepository
            .findByUserGroupIdAndTransactionDate(userGroupId, transactionDate)
            .orElseThrow(() -> new IllegalStateException("DailyGroupTransaction not found for " + transactionDate));
        groupTransaction.removeLivingExpensesOf(currentUser.id());
        for (LivingExpenseInput input : livingExpenses) {
            groupTransaction.addLivingExpense(currentUser.id(), input.categoryId(), input.amount(), input.memo());
        }
        DailyGroupTransaction savedGroupTransaction = dailyGroupTransactionRepository.save(groupTransaction);

        return buildInfo(
            currentUser.id(),
            transactionDate,
            savedGroupTransaction,
            savedPersonalTransaction,
            groupMemberCount(userGroupId)
        );
    }

    public void deleteDailyTransaction(UserId currentUserId, LocalDate transactionDate) {
        User currentUser = getCurrentUser(currentUserId);
        UserGroupId userGroupId = currentUserGroupId(currentUser);

        DailyPersonalTransaction personalTransaction = dailyPersonalTransactionRepository
            .findByUserIdAndTransactionDate(currentUser.id(), transactionDate)
            .orElseThrow(() -> new IllegalStateException("DailyPersonalTransaction not found for " + transactionDate));
        dailyPersonalTransactionRepository.delete(personalTransaction);

        dailyGroupTransactionRepository
            .findByUserGroupIdAndTransactionDate(userGroupId, transactionDate)
            .ifPresent(groupTransaction -> {
                groupTransaction.removeLivingExpensesOf(currentUser.id());
                if (groupTransaction.livingExpenses().isEmpty()) {
                    dailyGroupTransactionRepository.delete(groupTransaction);
                } else {
                    dailyGroupTransactionRepository.save(groupTransaction);
                }
            });
    }

    private DailyTransactionInfo buildInfo(
        UserId currentUserId,
        LocalDate transactionDate,
        DailyGroupTransaction groupTransaction,
        DailyPersonalTransaction personalTransaction,
        int groupMemberCount
    ) {
        Money income = personalTransaction != null ? personalTransaction.income() : new Money(0);
        List<DailyPersonalExpenseInfo> personalExpenses = personalTransaction != null
            ? personalTransaction.personalExpenses()
            : List.of();
        Money totalPersonalExpense = personalTransaction != null
            ? personalTransaction.calculateTotalPersonalExpense()
            : new Money(0);

        List<DailyLivingExpenseInfo> livingExpenses = groupTransaction != null
            ? groupTransaction.livingExpenses()
            : List.of();
        Money totalLivingExpense = groupTransaction != null
            ? groupTransaction.calculateTotalLivingExpense()
            : new Money(0);

        Money totalExpense = calculateTotalExpense(totalLivingExpense, totalPersonalExpense, groupMemberCount);
        Money budgetBalance = budgetService.calculateBudgetBalance(currentUserId, transactionDate);

        return new DailyTransactionInfo(
            transactionDate,
            income,
            livingExpenses,
            personalExpenses,
            totalLivingExpense,
            totalPersonalExpense,
            totalExpense,
            budgetBalance
        );
    }
}
