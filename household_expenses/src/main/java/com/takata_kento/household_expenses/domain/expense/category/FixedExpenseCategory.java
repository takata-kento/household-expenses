package com.takata_kento.household_expenses.domain.expense.category;

import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("fixed_expense_category")
public class FixedExpenseCategory {

    @Id
    private final FixedExpenseCategoryId id;

    @Column("user_group_id")
    private final UserGroupId userGroupId;

    @Column("category_name")
    private CategoryName categoryName;

    @Column("description")
    private Description description;

    @Column("default_amount")
    private Money defaultAmount;

    @Version
    private final Integer version;

    public FixedExpenseCategory(
        FixedExpenseCategoryId id,
        UserGroupId userGroupId,
        CategoryName categoryName,
        Description description,
        Money defaultAmount,
        Integer version
    ) {
        this.id = id;
        this.userGroupId = userGroupId;
        this.categoryName = categoryName;
        this.description = description;
        this.defaultAmount = defaultAmount;
        this.version = version;
    }

    public static FixedExpenseCategory create(
        CategoryName categoryName,
        Description description,
        Money defaultAmount,
        UserGroupId userGroupId
    ) {
        UUID idValue = UUID.randomUUID();
        FixedExpenseCategoryId id = new FixedExpenseCategoryId(idValue);
        return new FixedExpenseCategory(id, userGroupId, categoryName, description, defaultAmount, null);
    }

    public FixedExpenseCategoryId id() {
        return id;
    }

    public UserGroupId userGroupId() {
        return userGroupId;
    }

    public CategoryName categoryName() {
        return categoryName;
    }

    public Description description() {
        return description;
    }

    public Money defaultAmount() {
        return defaultAmount;
    }

    public Integer version() {
        return version;
    }

    public void updateCategoryName(CategoryName categoryName) {
        this.categoryName = categoryName;
    }

    public void updateDescription(Description description) {
        this.description = description;
    }

    public void updateDefaultAmount(Money defaultAmount) {
        this.defaultAmount = defaultAmount;
    }
}
