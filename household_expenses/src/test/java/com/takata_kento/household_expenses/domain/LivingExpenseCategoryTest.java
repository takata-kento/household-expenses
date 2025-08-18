package com.takata_kento.household_expenses.domain;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import org.junit.jupiter.api.Test;

class LivingExpenseCategoryTest {

    @Test
    void testConstructor() {
        // Given
        LivingExpenseCategoryId expectedId = new LivingExpenseCategoryId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        CategoryName expectedCategoryName = new CategoryName("食費");
        Description expectedDescription = new Description("食材・外食費");
        Boolean expectedIsDefault = true;
        Integer expectedVersion = 1;

        // When
        LivingExpenseCategory actual = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedIsDefault,
            expectedVersion
        );

        // Then
        assertThat(actual.id()).isEqualTo(expectedId);
        assertThat(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actual.categoryName()).isEqualTo(expectedCategoryName);
        assertThat(actual.description()).isEqualTo(expectedDescription);
        assertThat(actual.isDefault()).isEqualTo(expectedIsDefault);
        assertThat(actual.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testId() {
        // Given
        LivingExpenseCategoryId expectedId = new LivingExpenseCategoryId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        CategoryName expectedCategoryName = new CategoryName("食費");
        Description expectedDescription = new Description("食材・外食費");
        Boolean expectedIsDefault = true;
        Integer expectedVersion = 1;
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedIsDefault,
            expectedVersion
        );

        // When
        LivingExpenseCategoryId actual = category.id();

        // Then
        assertThat(actual).isEqualTo(expectedId);
        assertThat(category.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(category.categoryName()).isEqualTo(expectedCategoryName);
        assertThat(category.description()).isEqualTo(expectedDescription);
        assertThat(category.isDefault()).isEqualTo(expectedIsDefault);
        assertThat(category.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testUserGroupId() {
        // Given
        LivingExpenseCategoryId expectedId = new LivingExpenseCategoryId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        CategoryName expectedCategoryName = new CategoryName("食費");
        Description expectedDescription = new Description("食材・外食費");
        Boolean expectedIsDefault = true;
        Integer expectedVersion = 1;
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedIsDefault,
            expectedVersion
        );

        // When
        UserGroupId actual = category.userGroupId();

        // Then
        assertThat(actual).isEqualTo(expectedUserGroupId);
        assertThat(category.id()).isEqualTo(expectedId);
        assertThat(category.categoryName()).isEqualTo(expectedCategoryName);
        assertThat(category.description()).isEqualTo(expectedDescription);
        assertThat(category.isDefault()).isEqualTo(expectedIsDefault);
        assertThat(category.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testCategoryName() {
        // Given
        LivingExpenseCategoryId expectedId = new LivingExpenseCategoryId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        CategoryName expectedCategoryName = new CategoryName("食費");
        Description expectedDescription = new Description("食材・外食費");
        Boolean expectedIsDefault = true;
        Integer expectedVersion = 1;
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedIsDefault,
            expectedVersion
        );

        // When
        CategoryName actual = category.categoryName();

        // Then
        assertThat(actual).isEqualTo(expectedCategoryName);
        assertThat(category.id()).isEqualTo(expectedId);
        assertThat(category.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(category.description()).isEqualTo(expectedDescription);
        assertThat(category.isDefault()).isEqualTo(expectedIsDefault);
        assertThat(category.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testDescription() {
        // Given
        LivingExpenseCategoryId expectedId = new LivingExpenseCategoryId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        CategoryName expectedCategoryName = new CategoryName("食費");
        Description expectedDescription = new Description("食材・外食費");
        Boolean expectedIsDefault = true;
        Integer expectedVersion = 1;
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedIsDefault,
            expectedVersion
        );

        // When
        Description actual = category.description();

        // Then
        assertThat(actual).isEqualTo(expectedDescription);
        assertThat(category.id()).isEqualTo(expectedId);
        assertThat(category.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(category.categoryName()).isEqualTo(expectedCategoryName);
        assertThat(category.isDefault()).isEqualTo(expectedIsDefault);
        assertThat(category.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testIsDefault() {
        // Given
        LivingExpenseCategoryId expectedId = new LivingExpenseCategoryId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        CategoryName expectedCategoryName = new CategoryName("食費");
        Description expectedDescription = new Description("食材・外食費");
        Boolean expectedIsDefault = true;
        Integer expectedVersion = 1;
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedIsDefault,
            expectedVersion
        );

        // When
        Boolean actual = category.isDefault();

        // Then
        assertThat(actual).isEqualTo(expectedIsDefault);
        assertThat(category.id()).isEqualTo(expectedId);
        assertThat(category.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(category.categoryName()).isEqualTo(expectedCategoryName);
        assertThat(category.description()).isEqualTo(expectedDescription);
        assertThat(category.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testUpdateCategoryName() {
        // Given
        LivingExpenseCategoryId expectedId = new LivingExpenseCategoryId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        Description expectedDescription = new Description("元の説明");
        Boolean expectedIsDefault = true;
        Integer expectedVersion = 1;
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            new CategoryName("元の分類名"),
            expectedDescription,
            expectedIsDefault,
            expectedVersion
        );
        CategoryName expectedNewCategoryName = new CategoryName("新しい分類名");

        // When
        category.updateCategoryName(expectedNewCategoryName);

        // Then
        assertThat(category.categoryName()).isEqualTo(expectedNewCategoryName);
        assertThat(category.id()).isEqualTo(expectedId);
        assertThat(category.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(category.description()).isEqualTo(expectedDescription);
        assertThat(category.isDefault()).isEqualTo(expectedIsDefault);
        assertThat(category.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testUpdateDescription() {
        // Given
        LivingExpenseCategoryId expectedId = new LivingExpenseCategoryId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        CategoryName expectedCategoryName = new CategoryName("食費");
        Boolean expectedIsDefault = true;
        Integer expectedVersion = 1;
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            new Description("元の説明"),
            expectedIsDefault,
            expectedVersion
        );
        Description expectedNewDescription = new Description("新しい説明");

        // When
        category.updateDescription(expectedNewDescription);

        // Then
        assertThat(category.description()).isEqualTo(expectedNewDescription);
        assertThat(category.id()).isEqualTo(expectedId);
        assertThat(category.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(category.categoryName()).isEqualTo(expectedCategoryName);
        assertThat(category.isDefault()).isEqualTo(expectedIsDefault);
        assertThat(category.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testMarkAsDefault() {
        // Given
        LivingExpenseCategoryId expectedId = new LivingExpenseCategoryId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        CategoryName expectedCategoryName = new CategoryName("食費");
        Description expectedDescription = new Description("食材・外食費");
        Integer expectedVersion = 1;
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            false, // 初期状態はデフォルトではない
            expectedVersion
        );

        // When
        category.markAsDefault();

        // Then
        assertThat(category.isDefault()).isTrue(); // デフォルトに設定される
        assertThat(category.id()).isEqualTo(expectedId);
        assertThat(category.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(category.categoryName()).isEqualTo(expectedCategoryName);
        assertThat(category.description()).isEqualTo(expectedDescription);
        assertThat(category.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testUnmarkAsDefault() {
        // Given
        LivingExpenseCategoryId expectedId = new LivingExpenseCategoryId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        CategoryName expectedCategoryName = new CategoryName("食費");
        Description expectedDescription = new Description("食材・外食費");
        Integer expectedVersion = 1;
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            true, // 初期状態はデフォルト
            expectedVersion
        );

        // When
        category.unmarkAsDefault();

        // Then
        assertThat(category.isDefault()).isFalse(); // デフォルトが解除される
        assertThat(category.id()).isEqualTo(expectedId);
        assertThat(category.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(category.categoryName()).isEqualTo(expectedCategoryName);
        assertThat(category.description()).isEqualTo(expectedDescription);
        assertThat(category.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testCreate() {
        // Given
        CategoryName expectedCategoryName = new CategoryName("新規分類");
        Description expectedDescription = new Description("新規分類の説明");
        UserGroupId expectedUserGroupId = new UserGroupId(100L);

        // When
        LivingExpenseCategory actual = LivingExpenseCategory.create(
            expectedCategoryName,
            expectedDescription,
            expectedUserGroupId
        );

        // Then
        assertThat(actual.id()).isNotNull();
        assertThat(actual.categoryName()).isEqualTo(expectedCategoryName);
        assertThat(actual.description()).isEqualTo(expectedDescription);
        assertThat(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actual.isDefault()).isFalse();
        assertThat(actual.version()).isNull();
    }
}
