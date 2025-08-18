package com.takata_kento.household_expenses.domain;

import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("living_expense_category")
public class LivingExpenseCategory {

    @Id
    private final LivingExpenseCategoryId id;

    @Column("user_group_id")
    private final UserGroupId userGroupId;

    @Column("category_name")
    private CategoryName categoryName;

    @Column("description")
    private Description description;

    @Column("is_default")
    private Boolean isDefault;

    @Version
    private final Integer version;

    public LivingExpenseCategory(
        LivingExpenseCategoryId id,
        UserGroupId userGroupId,
        CategoryName categoryName,
        Description description,
        Boolean isDefault,
        Integer version
    ) {
        this.id = id;
        this.userGroupId = userGroupId;
        this.categoryName = categoryName;
        this.description = description;
        this.isDefault = isDefault;
        this.version = version;
    }

    public static LivingExpenseCategory create(
        CategoryName categoryName,
        Description description,
        UserGroupId userGroupId
    ) {
        Long idValue = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        LivingExpenseCategoryId id = new LivingExpenseCategoryId(idValue);
        return new LivingExpenseCategory(id, userGroupId, categoryName, description, false, null);
    }

    public LivingExpenseCategoryId id() {
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

    public Boolean isDefault() {
        return isDefault;
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

    public void markAsDefault() {
        this.isDefault = true;
    }

    public void unmarkAsDefault() {
        this.isDefault = false;
    }
}
