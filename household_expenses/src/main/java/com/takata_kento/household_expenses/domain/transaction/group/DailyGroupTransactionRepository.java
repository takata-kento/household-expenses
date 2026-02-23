package com.takata_kento.household_expenses.domain.transaction.group;

import com.takata_kento.household_expenses.domain.valueobject.DailyGroupTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DailyGroupTransactionRepository
    extends CrudRepository<DailyGroupTransaction, DailyGroupTransactionId> {
    @Query(
        "SELECT * FROM daily_group_transaction WHERE user_group_id = :#{#userGroupId.value.toString()} AND transaction_date = :transactionDate"
    )
    Optional<DailyGroupTransaction> findByUserGroupIdAndTransactionDate(
        @Param("userGroupId") UserGroupId userGroupId,
        @Param("transactionDate") LocalDate transactionDate
    );

    @Query("SELECT * FROM daily_group_transaction WHERE user_group_id = :#{#userGroupId.value.toString()}")
    List<DailyGroupTransaction> findByUserGroupId(@Param("userGroupId") UserGroupId userGroupId);
}
