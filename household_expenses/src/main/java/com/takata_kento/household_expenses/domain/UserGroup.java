package com.takata_kento.household_expenses.domain;

import com.takata_kento.household_expenses.domain.valueobject.*;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_group")
public class UserGroup {

    @Id
    private UserGroupId id;

    @Column("group_name")
    private GroupName groupName;

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
        GroupName groupName,
        Day monthStartDay,
        UserId createdByUserId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        this.id = id;
        this.groupName = groupName;
        this.monthStartDay = monthStartDay;
        this.createdByUserId = createdByUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public static UserGroup create(GroupName groupName, Day monthStartDay, UserId createdByUserId) {
        LocalDateTime now = LocalDateTime.now();
        return new UserGroup(null, groupName, monthStartDay, createdByUserId, now, now, null);
    }

    public UserGroupId id() {
        return id;
    }

    public GroupName groupName() {
        return groupName;
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
        this.groupName = groupName;
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
