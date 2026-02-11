package com.takata_kento.household_expenses.domain.usergroup;

import com.takata_kento.household_expenses.domain.valueobject.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_group")
public class UserGroup {

    @Id
    private UserGroupId id;

    @Column("group_name")
    private GroupName name;

    @Column("month_start_day")
    private Day monthStartDay;

    @Column("created_by_user_id")
    private final UserId createdByUserId;

    @Column("created_at")
    private final LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Integer version;

    public UserGroup(
        UserGroupId id,
        GroupName name,
        Day monthStartDay,
        UserId createdByUserId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        this.id = id;
        this.name = name;
        this.monthStartDay = monthStartDay;
        this.createdByUserId = createdByUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public static UserGroup create(GroupName name, Day monthStartDay, UserId createdByUserId) {
        LocalDateTime now = LocalDateTime.now();
        UserGroupId userGroupId = new UserGroupId(UUID.randomUUID());
        return new UserGroup(userGroupId, name, monthStartDay, createdByUserId, now, null, null);
    }

    public UserGroupId id() {
        return id;
    }

    public GroupName name() {
        return name;
    }

    public Day monthStartDay() {
        return monthStartDay;
    }

    public UserId createdByUserId() {
        return createdByUserId;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public Integer version() {
        return version;
    }

    public void updateGroupName(GroupName groupName) {
        this.name = groupName;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateMonthStartDay(Day monthStartDay) {
        this.monthStartDay = monthStartDay;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canBeModifiedBy(UserId userId) {
        return createdByUserId.equals(userId);
    }
}
