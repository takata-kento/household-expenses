package com.takata_kento.household_expenses.domain.account;

import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FinancialAccountRepository extends CrudRepository<FinancialAccount, FinancialAccountId> {
    @Query("SELECT * FROM financial_account WHERE user_id = :#{#userId.value.toString()}")
    List<FinancialAccount> findByUserId(@Param("userId") UserId userId);
}
