package com.takata_kento.household_expenses.domain.expense.category;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LivingExpenseCategoryTest {

    static Stream<Arguments> provideLivingExpenseCategoryData() {
        LivingExpenseCategoryId id = new LivingExpenseCategoryId(UUID.randomUUID());
        UserGroupId userGroupId = new UserGroupId(100L);
        CategoryName categoryName = new CategoryName("食費");
        Description description = new Description("食材・外食費");
        Boolean isDefault = Boolean.TRUE;
        Integer version = Integer.valueOf(1);

        return Stream.of(
            Arguments.of(
                id,
                userGroupId,
                categoryName,
                description,
                isDefault,
                version,
                new LivingExpenseCategory(id, userGroupId, categoryName, description, isDefault, version)
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
        Integer expectedVersion,
        LivingExpenseCategory livingExpenseCategory
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
        then(actual.id()).isEqualTo(expectedId);
        then(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        then(actual.categoryName()).isEqualTo(expectedCategoryName);
        then(actual.description()).isEqualTo(expectedDescription);
        then(actual.isDefault()).isEqualTo(expectedIsDefault);
        then(actual.version()).isEqualTo(expectedVersion);
    }

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testId(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion,
        LivingExpenseCategory livingExpenseCategory
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
        LivingExpenseCategoryId actual = livingExpenseCategory.id();

        // Then
        then(actual).isEqualTo(expectedId);
        then(livingExpenseCategory).usingRecursiveComparison().isEqualTo(category);
    }

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testUserGroupId(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion,
        LivingExpenseCategory livingExpenseCategory
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
        then(actual).isEqualTo(expectedUserGroupId);
        then(livingExpenseCategory).usingRecursiveComparison().isEqualTo(category);
    }

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testCategoryName(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion,
        LivingExpenseCategory livingExpenseCategory
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
        then(actual).isEqualTo(expectedCategoryName);
        then(livingExpenseCategory).usingRecursiveComparison().isEqualTo(category);
    }

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testDescription(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion,
        LivingExpenseCategory livingExpenseCategory
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
        then(actual).isEqualTo(expectedDescription);
        then(livingExpenseCategory).usingRecursiveComparison().isEqualTo(category);
    }

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testIsDefault(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion,
        LivingExpenseCategory livingExpenseCategory
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
        then(actual).isEqualTo(expectedIsDefault);
        then(livingExpenseCategory).usingRecursiveComparison().isEqualTo(category);
    }

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testUpdateCategoryName(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName currentCategoryName,
        Description expectedDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion,
        LivingExpenseCategory livingExpenseCategory
    ) {
        // Given
        CategoryName expectedNewCategoryName = new CategoryName("新しい分類名");
        LivingExpenseCategory expected = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedNewCategoryName,
            expectedDescription,
            expectedIsDefault,
            expectedVersion
        );

        // When
        livingExpenseCategory.updateCategoryName(expectedNewCategoryName);

        // Then
        then(livingExpenseCategory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideLivingExpenseCategoryData")
    void testUpdateDescription(
        LivingExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description currentDescription,
        Boolean expectedIsDefault,
        Integer expectedVersion,
        LivingExpenseCategory livingExpenseCategory
    ) {
        // Given
        Description expectedNewDescription = new Description("新しい説明");
        LivingExpenseCategory expected = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedNewDescription,
            expectedIsDefault,
            expectedVersion
        );

        // When
        livingExpenseCategory.updateDescription(expectedNewDescription);

        // Then
        then(livingExpenseCategory).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testMarkAsDefault() {
        // Given
        LivingExpenseCategoryId expectedId = new LivingExpenseCategoryId(UUID.randomUUID());
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
        Integer expectedVersion,
        LivingExpenseCategory livingExpenseCategory
    ) {
        // Given
        LivingExpenseCategory expected = new LivingExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            Boolean.FALSE, // 初期状態はデフォルト
            expectedVersion
        );

        // When
        livingExpenseCategory.unmarkAsDefault();

        // Then
        then(livingExpenseCategory).usingRecursiveComparison().isEqualTo(expected);
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
