package com.takata_kento.household_expenses.domain.saving;

import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.MonthlySavingId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MonthlySavingRepository extends CrudRepository<MonthlySaving, MonthlySavingId> {

    @Query("SELECT * FROM monthly_saving WHERE user_id = :#{#userId.value.toString()} AND year = :#{#year.value()} AND month = :#{#month.value()}")
    Optional<MonthlySaving> findByUserIdAndYearAndMonth(
        @Param("userId") UserId userId,
        @Param("year") Year year,
        @Param("month") Month month
    );

    @Query("SELECT * FROM monthly_saving WHERE user_id = :#{#userId.value.toString()} AND year = :#{#year.value()}")
    List<MonthlySaving> findByUserIdAndYear(
        @Param("userId") UserId userId,
        @Param("year") Year year
    );
}
