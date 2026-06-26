package com.takata_kento.household_expenses.application.expense;

import com.takata_kento.household_expenses.application.exception.ForbiddenException;
import com.takata_kento.household_expenses.application.exception.GroupMembershipRequiredException;
import com.takata_kento.household_expenses.application.exception.ResourceNotFoundException;
import com.takata_kento.household_expenses.domain.expense.category.FixedExpenseCategory;
import com.takata_kento.household_expenses.domain.expense.category.FixedExpenseCategoryRepository;
import com.takata_kento.household_expenses.domain.expense.category.LivingExpenseCategory;
import com.takata_kento.household_expenses.domain.expense.category.LivingExpenseCategoryRepository;
import com.takata_kento.household_expenses.domain.expense.history.FixedExpenseHistory;
import com.takata_kento.household_expenses.domain.expense.history.FixedExpenseHistoryRepository;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.user.UserRepository;
import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExpenseService {

    private final UserRepository userRepository;
    private final LivingExpenseCategoryRepository livingExpenseCategoryRepository;
    private final FixedExpenseCategoryRepository fixedExpenseCategoryRepository;
    private final FixedExpenseHistoryRepository fixedExpenseHistoryRepository;

    public ExpenseService(
        UserRepository userRepository,
        LivingExpenseCategoryRepository livingExpenseCategoryRepository,
        FixedExpenseCategoryRepository fixedExpenseCategoryRepository,
        FixedExpenseHistoryRepository fixedExpenseHistoryRepository
    ) {
        this.userRepository = userRepository;
        this.livingExpenseCategoryRepository = livingExpenseCategoryRepository;
        this.fixedExpenseCategoryRepository = fixedExpenseCategoryRepository;
        this.fixedExpenseHistoryRepository = fixedExpenseHistoryRepository;
    }

    private User getCurrentUser(UserId userId) {
        return userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalStateException("User not found: " + userId));
    }

    private UserGroupId getCurrentUserGroupId(UserId userId) {
        return getCurrentUser(userId)
            .userGroupId()
            .orElseThrow(() -> new GroupMembershipRequiredException("User does not belong to any group"));
    }

    public LivingExpenseCategory createLivingExpenseCategory(
        UserId currentUserId,
        CategoryName name,
        Description description
    ) {
        UserGroupId userGroupId = getCurrentUserGroupId(currentUserId);
        LivingExpenseCategory category = LivingExpenseCategory.create(name, description, userGroupId);
        return livingExpenseCategoryRepository.save(category);
    }

    public LivingExpenseCategory updateLivingExpenseCategory(
        UserId currentUserId,
        LivingExpenseCategoryId id,
        CategoryName name,
        Description description
    ) {
        UserGroupId userGroupId = getCurrentUserGroupId(currentUserId);
        LivingExpenseCategory category = livingExpenseCategoryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("LivingExpenseCategory not found: " + id));
        if (!category.belongsTo(userGroupId)) {
            throw new ForbiddenException("Cannot modify category from another group: " + id);
        }
        category.updateCategoryName(name);
        category.updateDescription(description);
        return livingExpenseCategoryRepository.save(category);
    }

    public void deleteLivingExpenseCategory(UserId currentUserId, LivingExpenseCategoryId id) {
        UserGroupId userGroupId = getCurrentUserGroupId(currentUserId);
        LivingExpenseCategory category = livingExpenseCategoryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("LivingExpenseCategory not found: " + id));
        if (!category.belongsTo(userGroupId)) {
            throw new ForbiddenException("Cannot delete category from another group: " + id);
        }
        livingExpenseCategoryRepository.deleteById(id);
    }

    public FixedExpenseCategory createFixedExpenseCategory(
        UserId currentUserId,
        CategoryName name,
        Description description,
        Money defaultAmount
    ) {
        UserGroupId userGroupId = getCurrentUserGroupId(currentUserId);
        FixedExpenseCategory category = FixedExpenseCategory.create(name, description, defaultAmount, userGroupId);
        return fixedExpenseCategoryRepository.save(category);
    }

    public FixedExpenseHistory setFixedExpenseAmount(
        UserId currentUserId,
        FixedExpenseCategoryId categoryId,
        Year year,
        Month month,
        Money amount,
        LocalDate effectiveDate,
        Optional<Description> memo
    ) {
        UserGroupId userGroupId = getCurrentUserGroupId(currentUserId);
        FixedExpenseCategory category = fixedExpenseCategoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("FixedExpenseCategory not found: " + categoryId));
        if (!category.belongsTo(userGroupId)) {
            throw new ForbiddenException("Cannot set amount for category from another group: " + categoryId);
        }
        FixedExpenseHistory history = fixedExpenseHistoryRepository
            .findByFixedExpenseCategoryIdAndYearAndMonth(categoryId, year, month)
            .map(existing -> {
                existing.updateAmount(amount);
                existing.updateEffectiveDate(effectiveDate);
                existing.updateMemo(memo);
                return existing;
            })
            .orElseGet(() -> FixedExpenseHistory.create(categoryId, year, month, amount, effectiveDate, memo));
        return fixedExpenseHistoryRepository.save(history);
    }

    public List<LivingExpenseCategory> getLivingExpenseCategories(UserId currentUserId) {
        UserGroupId userGroupId = getCurrentUserGroupId(currentUserId);
        return livingExpenseCategoryRepository.findByUserGroupId(userGroupId);
    }

    public List<FixedExpenseCategory> getFixedExpenseCategories(UserId currentUserId) {
        UserGroupId userGroupId = getCurrentUserGroupId(currentUserId);
        return fixedExpenseCategoryRepository.findByUserGroupId(userGroupId);
    }

    public List<FixedExpenseHistory> getFixedExpenses(UserId currentUserId, Year year, Month month) {
        UserGroupId userGroupId = getCurrentUserGroupId(currentUserId);
        List<FixedExpenseCategoryId> categoryIds = fixedExpenseCategoryRepository
            .findByUserGroupId(userGroupId)
            .stream()
            .map(FixedExpenseCategory::id)
            .toList();
        if (categoryIds.isEmpty()) {
            return List.of();
        }
        return fixedExpenseHistoryRepository.findByFixedExpenseCategoryIdInAndYearAndMonth(categoryIds, year, month);
    }
}
