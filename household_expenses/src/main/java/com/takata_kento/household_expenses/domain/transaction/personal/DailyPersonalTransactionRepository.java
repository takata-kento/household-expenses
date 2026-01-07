package com.takata_kento.household_expenses.domain.transaction.personal;

import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DailyPersonalTransactionRepository
    extends CrudRepository<DailyPersonalTransaction, DailyPersonalTransactionId> {
    @Query(
        "SELECT * FROM daily_personal_transaction WHERE user_id = :#{#userId.value} AND transaction_date = :transactionDate"
    )
    Optional<DailyPersonalTransaction> findByUserIdAndTransactionDate(
        @Param("userId") UserId userId,
        @Param("transactionDate") LocalDate transactionDate
    );

    @Query("SELECT * FROM daily_personal_transaction WHERE user_id = :#{#userId.value}")
    List<DailyPersonalTransaction> findByUserId(@Param("userId") UserId userId);

    @Query(
        "SELECT EXISTS(SELECT 1 FROM daily_personal_transaction WHERE user_id = :#{#userId.value} AND transaction_date = :transactionDate)"
    )
    boolean existsByUserIdAndTransactionDate(
        @Param("userId") UserId userId,
        @Param("transactionDate") LocalDate transactionDate
    );
}
