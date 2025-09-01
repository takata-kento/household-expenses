package com.takata_kento.household_expenses.domain;

import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LivingExpenseCategoryRepository
    extends CrudRepository<LivingExpenseCategory, LivingExpenseCategoryId> {
    @Query("SELECT * FROM living_expense_category WHERE user_group_id = :#{#userGroupId.value}")
    List<LivingExpenseCategory> findByUserGroupId(@Param("userGroupId") UserGroupId userGroupId);

    @Query(
        "SELECT * FROM living_expense_category WHERE user_group_id = :#{#userGroupId.value} AND is_default = :isDefault"
    )
    List<LivingExpenseCategory> findByUserGroupIdAndIsDefault(
        @Param("userGroupId") UserGroupId userGroupId,
        @Param("isDefault") Boolean isDefault
    );
}
