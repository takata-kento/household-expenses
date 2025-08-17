package com.takata_kento.household_expenses.domain;

import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.MonthlyBudgetId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MonthlyBudgetRepository extends CrudRepository<MonthlyBudget, MonthlyBudgetId> {
    @Query(
        "SELECT * FROM monthly_budget WHERE user_group_id = :#{#userGroupId.value} AND year = :#{#year.value} AND month = :#{#month.value}"
    )
    Optional<MonthlyBudget> findByUserGroupIdAndYearAndMonth(
        @Param("userGroupId") UserGroupId userGroupId,
        @Param("year") Year year,
        @Param("month") Month month
    );

    @Query("SELECT * FROM monthly_budget WHERE user_group_id = :#{#userGroupId.value}")
    List<MonthlyBudget> findByUserGroupId(@Param("userGroupId") UserGroupId userGroupId);

    @Query(
        "SELECT EXISTS(SELECT 1 FROM monthly_budget WHERE user_group_id = :#{#userGroupId.value} AND year = :#{#year.value} AND month = :#{#month.value})"
    )
    boolean existsByUserGroupIdAndYearAndMonth(
        @Param("userGroupId") UserGroupId userGroupId,
        @Param("year") Year year,
        @Param("month") Month month
    );

    @Query("SELECT * FROM monthly_budget WHERE user_group_id = :#{#userGroupId.value} AND year = :#{#year.value}")
    List<MonthlyBudget> findByUserGroupIdAndYear(
        @Param("userGroupId") UserGroupId userGroupId,
        @Param("year") Year year
    );
}
