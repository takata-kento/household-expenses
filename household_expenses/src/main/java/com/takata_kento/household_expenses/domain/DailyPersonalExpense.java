package com.takata_kento.household_expenses.domain;

import com.takata_kento.household_expenses.domain.valueobject.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("daily_personal_expense")
public class DailyPersonalExpense {

    @Id
    private final DailyPersonalExpenseId id;

    @Column("user_id")
    private final UserId userId;

    @Column("transaction_date")
    private final LocalDate transactionDate;

    @Column("amount")
    private final Money amount;

    @Column("description")
    private final Description description;

    @Column("created_at")
    private final LocalDateTime createdAt;

    @Column("updated_at")
    private final LocalDateTime updatedAt;

    @Version
    private final Integer version;

    public DailyPersonalExpense(
        DailyPersonalExpenseId id,
        UserId userId,
        LocalDate transactionDate,
        Money amount,
        Description description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer version
    ) {
        this.id = id;
        this.userId = userId;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public DailyPersonalExpenseId id() {
        return id;
    }

    public UserId userId() {
        return userId;
    }

    public LocalDate transactionDate() {
        return transactionDate;
    }

    public Money amount() {
        return amount;
    }

    public Description description() {
        return description;
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

    public static DailyPersonalExpense of(
        UserId userId,
        LocalDate transactionDate,
        Money amount,
        Description description
    ) {
        LocalDateTime now = LocalDateTime.now();
        Long idValue = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        DailyPersonalExpenseId id = new DailyPersonalExpenseId(idValue);
        return new DailyPersonalExpense(id, userId, transactionDate, amount, description, now, null, null);
    }
}
