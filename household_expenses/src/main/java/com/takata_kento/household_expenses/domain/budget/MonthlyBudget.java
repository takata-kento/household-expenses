package com.takata_kento.household_expenses.domain.budget;

import com.takata_kento.household_expenses.domain.valueobject.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("monthly_budget")
public class MonthlyBudget {

    @Id
    private MonthlyBudgetId id;

    @Column("user_group_id")
    private UserGroupId userGroupId;

    @Column("year")
    private Year year;

    @Column("month")
    private Month month;

    @Column("budget_amount")
    private Money budgetAmount;

    @Column("set_by_user_id")
    private UserId setByUserId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Integer version;

    public MonthlyBudget(
        MonthlyBudgetId id,
        UserGroupId userGroupId,
        Year year,
        Month month,
        Money budgetAmount,
        UserId setByUserId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        this.id = id;
        this.userGroupId = userGroupId;
        this.year = year;
        this.month = month;
        this.budgetAmount = budgetAmount;
        this.setByUserId = setByUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public static MonthlyBudget create(
        UserGroupId userGroupId,
        Year year,
        Month month,
        Money budgetAmount,
        UserId setByUserId
    ) {
        LocalDateTime now = LocalDateTime.now();
        MonthlyBudgetId id = new MonthlyBudgetId(UUID.randomUUID());
        return new MonthlyBudget(id, userGroupId, year, month, budgetAmount, setByUserId, now, null, null);
    }

    public MonthlyBudgetId id() {
        return id;
    }

    public UserGroupId userGroupId() {
        return userGroupId;
    }

    public Year year() {
        return year;
    }

    public Month month() {
        return month;
    }

    public Money budgetAmount() {
        return budgetAmount;
    }

    public UserId setByUserId() {
        return setByUserId;
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

    public void updateBudgetAmount(Money budgetAmount, UserId setByUserId) {
        this.budgetAmount = budgetAmount;
        this.setByUserId = setByUserId;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isCurrentMonth(LocalDate date) {
        return this.year.value() == date.getYear() && this.month.value() == date.getMonthValue();
    }

    public boolean isSetBy(UserId userId) {
        return this.setByUserId.equals(userId);
    }

    public Money calculateRemainingBudget(Money usedAmount) {
        return this.budgetAmount.subtract(usedAmount);
    }

    public boolean isOverBudget(Money usedAmount) {
        return usedAmount.amount() > this.budgetAmount.amount();
    }
}
