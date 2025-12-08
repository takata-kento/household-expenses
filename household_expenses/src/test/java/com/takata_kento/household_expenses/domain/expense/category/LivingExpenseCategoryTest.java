package com.takata_kento.household_expenses.domain.expense.category;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LivingExpenseCategoryTest {

    static Stream<Arguments> provideLivingExpenseCategoryData() {
        return Stream.of(
            Arguments.of(
                new LivingExpenseCategoryId(1L),
                new UserGroupId(100L),
                new CategoryName("食費"),
                new Description("食材・外食費"),
                true,
                1
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testConstructor(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion
    ) {
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

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testId(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion
    ) {
        // Given
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

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testUserGroupId(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion
    ) {
        // Given
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

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testCategoryName(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion
    ) {
        // Given
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

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testDescription(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion
    ) {
        // Given
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

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testIsDefault(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion
    ) {
        // Given
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

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testUpdateCategoryName(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName currentCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion
    ) {
        // Given
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            currentCategoryName,
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

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testUpdateDescription(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description currentDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion
    ) {
        // Given
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            currentDescription,
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

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testUnmarkAsDefault(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean currentIsDefault,
        Integer expectedVersion
    ) {
        // Given
        LivingExpenseCategory category = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            currentIsDefault, // 初期状態はデフォルト
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
