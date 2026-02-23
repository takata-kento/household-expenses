package com.takata_kento.household_expenses.domain.expense.category;

import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FixedExpenseCategoryRepository extends CrudRepository<FixedExpenseCategory, FixedExpenseCategoryId> {
    @Query("SELECT * FROM fixed_expense_category WHERE user_group_id = :#{#userGroupId.toString()}")
    List<FixedExpenseCategory> findByUserGroupId(@Param("userGroupId") UserGroupId userGroupId);
}
