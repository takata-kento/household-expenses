package com.takata_kento.household_expenses.domain;

import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, UserId> {
    @Query("SELECT * FROM users WHERE username = :#{#username.value}")
    Optional<User> findByUsername(@Param("username") Username username);

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :#{#username.value})")
    boolean existsByUsername(@Param("username") Username username);
}
