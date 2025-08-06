package com.takata_kento.household_expenses.domain;

import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import java.util.Optional;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public class User {

    @Id
    private final UserId id;

    private final Username username;

    @Column("user_group_id")
    private final UserGroupId userGroupId;

    public User(UserId id, Username username, UserGroupId userGroupId) {
        this.id = id;
        this.username = username;
        this.userGroupId = userGroupId;
    }

    public UserId id() {
        return id;
    }

    public Username username() {
        return username;
    }

    public Optional<UserGroupId> userGroupId() {
        return Optional.ofNullable(userGroupId);
    }
}
