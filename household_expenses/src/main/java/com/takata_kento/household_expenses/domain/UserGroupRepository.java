package com.takata_kento.household_expenses.domain;

import com.takata_kento.household_expenses.domain.valueobject.GroupName;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserGroupRepository extends CrudRepository<UserGroup, UserGroupId> {
    @Query("SELECT * FROM user_group WHERE group_name = :#{#groupName.value}")
    Optional<UserGroup> findByGroupName(@Param("groupName") GroupName groupName);

    @Query("SELECT EXISTS(SELECT 1 FROM user_group WHERE group_name = :#{#groupName.value})")
    boolean existsByGroupName(@Param("groupName") GroupName groupName);

    @Query("SELECT * FROM user_group WHERE created_by_user_id = :#{#createdByUserId.value}")
    List<UserGroup> findByCreatedByUserId(@Param("createdByUserId") UserId createdByUserId);

    @Query("SELECT * FROM user_group WHERE group_name LIKE '%' || :groupNameFragment || '%'")
    List<UserGroup> findByGroupNameContaining(@Param("groupNameFragment") String groupNameFragment);
}
