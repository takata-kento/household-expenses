package com.takata_kento.household_expenses.domain.expense.history;

import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseHistoryId;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FixedExpenseHistoryRepository extends CrudRepository<FixedExpenseHistory, FixedExpenseHistoryId> {

    @Query(
        "SELECT * FROM fixed_expense_history " +
        "WHERE fixed_expense_category_id = :#{#categoryId.toString()} " +
        "AND year = :#{#year.value()} " +
        "AND month = :#{#month.value()}"
    )
    Optional<FixedExpenseHistory> findByFixedExpenseCategoryIdAndYearAndMonth(
        @Param("categoryId") FixedExpenseCategoryId categoryId,
        @Param("year") Year year,
        @Param("month") Month month
    );
}
