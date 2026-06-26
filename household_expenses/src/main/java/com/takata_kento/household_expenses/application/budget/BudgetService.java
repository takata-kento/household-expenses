package com.takata_kento.household_expenses.application.budget;

import com.takata_kento.household_expenses.application.exception.GroupMembershipRequiredException;
import com.takata_kento.household_expenses.application.exception.ResourceNotFoundException;
import com.takata_kento.household_expenses.domain.budget.MonthlyBudget;
import com.takata_kento.household_expenses.domain.budget.MonthlyBudgetRepository;
import com.takata_kento.household_expenses.domain.transaction.group.DailyGroupTransaction;
import com.takata_kento.household_expenses.domain.transaction.group.DailyGroupTransactionRepository;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.user.UserRepository;
import com.takata_kento.household_expenses.domain.usergroup.UserGroup;
import com.takata_kento.household_expenses.domain.usergroup.UserGroupRepository;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BudgetService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final MonthlyBudgetRepository monthlyBudgetRepository;
    private final DailyGroupTransactionRepository dailyGroupTransactionRepository;

    public BudgetService(
        UserRepository userRepository,
        UserGroupRepository userGroupRepository,
        MonthlyBudgetRepository monthlyBudgetRepository,
        DailyGroupTransactionRepository dailyGroupTransactionRepository
    ) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.monthlyBudgetRepository = monthlyBudgetRepository;
        this.dailyGroupTransactionRepository = dailyGroupTransactionRepository;
    }

    private User getCurrentUser(UserId userId) {
        return userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalStateException("User not found: " + userId));
    }

    private UserGroupId currentUserGroupId(User currentUser) {
        return currentUser
            .userGroupId()
            .orElseThrow(() -> new GroupMembershipRequiredException("User does not belong to any group"));
    }

    public SetMonthlyBudgetResult setMonthlyBudget(UserId currentUserId, Year year, Month month, Money budgetAmount) {
        User currentUser = getCurrentUser(currentUserId);
        UserGroupId userGroupId = currentUserGroupId(currentUser);
        return monthlyBudgetRepository
            .findByUserGroupIdAndYearAndMonth(userGroupId, year, month)
            .map(existing -> {
                existing.updateBudgetAmount(budgetAmount, currentUser.id());
                return new SetMonthlyBudgetResult(monthlyBudgetRepository.save(existing), false);
            })
            .orElseGet(() -> {
                MonthlyBudget created = MonthlyBudget.create(userGroupId, year, month, budgetAmount, currentUser.id());
                return new SetMonthlyBudgetResult(monthlyBudgetRepository.save(created), true);
            });
    }

    public MonthlyBudget getMonthlyBudget(UserId currentUserId, Year year, Month month) {
        User currentUser = getCurrentUser(currentUserId);
        UserGroupId userGroupId = currentUserGroupId(currentUser);
        return monthlyBudgetRepository
            .findByUserGroupIdAndYearAndMonth(userGroupId, year, month)
            .orElseThrow(() ->
                new ResourceNotFoundException("MonthlyBudget not found: " + year.value() + "-" + month.value())
            );
    }

    public List<MonthlyBudget> getMonthlyBudgetsByYear(UserId currentUserId, Year year) {
        User currentUser = getCurrentUser(currentUserId);
        UserGroupId userGroupId = currentUserGroupId(currentUser);
        return monthlyBudgetRepository.findByUserGroupIdAndYear(userGroupId, year);
    }

    public Money calculateBudgetBalance(UserId currentUserId, LocalDate targetDate) {
        User currentUser = getCurrentUser(currentUserId);
        UserGroupId userGroupId = currentUserGroupId(currentUser);
        UserGroup userGroup = userGroupRepository
            .findById(userGroupId)
            .orElseThrow(() -> new IllegalStateException("UserGroup not found: " + userGroupId));
        LocalDate periodStart = resolvePeriodStart(targetDate, userGroup.monthStartDay().value());
        Year periodYear = new Year(periodStart.getYear());
        Month periodMonth = new Month(periodStart.getMonthValue());
        MonthlyBudget budget = monthlyBudgetRepository
            .findByUserGroupIdAndYearAndMonth(userGroupId, periodYear, periodMonth)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "MonthlyBudget not found: " + periodYear.value() + "-" + periodMonth.value()
                )
            );
        Money totalSpent = dailyGroupTransactionRepository
            .findByUserGroupIdAndTransactionDateBetween(userGroupId, periodStart, targetDate)
            .stream()
            .map(DailyGroupTransaction::calculateTotalLivingExpense)
            .reduce(new Money(0), Money::add);
        return budget.calculateRemainingBudget(totalSpent);
    }

    // monthStartDay が当月の日数を超える場合は当月末日に丸める (例: 月始日=31 で 2月の場合は2月末日)
    private LocalDate resolvePeriodStart(LocalDate targetDate, int monthStartDay) {
        int targetMonthEffectiveStartDay = Math.min(monthStartDay, targetDate.lengthOfMonth());
        if (targetDate.getDayOfMonth() >= targetMonthEffectiveStartDay) {
            return LocalDate.of(targetDate.getYear(), targetDate.getMonth(), targetMonthEffectiveStartDay);
        }
        LocalDate previousMonth = targetDate.minusMonths(1);
        int previousMonthEffectiveStartDay = Math.min(monthStartDay, previousMonth.lengthOfMonth());
        return LocalDate.of(previousMonth.getYear(), previousMonth.getMonth(), previousMonthEffectiveStartDay);
    }
}
