package com.takata_kento.household_expenses.domain;

import com.takata_kento.household_expenses.domain.valueobject.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("daily_living_expense")
public class DailyLivingExpense {

    @Id
    private final DailyLivingExpenseId id;

    @Column("user_id")
    private final UserId userId;

    @Column("user_group_id")
    private final UserGroupId userGroupId;

    @Column("living_expense_category_id")
    private final LivingExpenseCategoryId livingExpenseCategoryId;

    @Column("transaction_date")
    private final LocalDate transactionDate;

    @Column("amount")
    private final Money amount;

    @Column("memo")
    private final Description memo;

    @Column("created_at")
    private final LocalDateTime createdAt;

    @Column("updated_at")
    private final LocalDateTime updatedAt;

    @Version
    private final Integer version;

    public DailyLivingExpense(
        DailyLivingExpenseId id,
        UserId userId,
        UserGroupId userGroupId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        LocalDate transactionDate,
        Money amount,
        Description memo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        this.id = id;
        this.userId = userId;
        this.userGroupId = userGroupId;
        this.livingExpenseCategoryId = livingExpenseCategoryId;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.memo = memo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public DailyLivingExpenseId id() {
        return id;
    }

    public UserId userId() {
        return userId;
    }

    public UserGroupId userGroupId() {
        return userGroupId;
    }

    public LivingExpenseCategoryId livingExpenseCategoryId() {
        return livingExpenseCategoryId;
    }

    public LocalDate transactionDate() {
        return transactionDate;
    }

    public Money amount() {
        return amount;
    }

    public Description memo() {
        return memo;
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

    public static DailyLivingExpense of(
        UserId userId,
        UserGroupId userGroupId,
        LivingExpenseCategoryId livingExpenseCategoryId,
        LocalDate transactionDate,
        Money amount,
        Description memo
    ) {
        LocalDateTime now = LocalDateTime.now();
        Long idValue = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        DailyLivingExpenseId id = new DailyLivingExpenseId(idValue);
        return new DailyLivingExpense(
            id,
            userId,
            userGroupId,
            livingExpenseCategoryId,
            transactionDate,
            amount,
            memo,
            now,
            null,
            null
        );
    }
}
