package com.takata_kento.household_expenses.domain.expense.category;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FixedExpenseCategoryTest {

    static Stream<Arguments> provideFixedExpenseCategoryData() {
        FixedExpenseCategoryId id = new FixedExpenseCategoryId(UUID.randomUUID());
        UserGroupId userGroupId = new UserGroupId(UUID.randomUUID());
        CategoryName categoryName = new CategoryName("家賃");
        Description description = new Description("毎月の家賃");
        Money defaultAmount = new Money(80000);
        Integer version = Integer.valueOf(1);

        return Stream.of(
            Arguments.of(
                id,
                userGroupId,
                categoryName,
                description,
                defaultAmount,
                version,
                new FixedExpenseCategory(id, userGroupId, categoryName, description, defaultAmount, version)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseCategoryData")
    void testConstructor(
        FixedExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Money expectedDefaultAmount,
        Integer expectedVersion,
        FixedExpenseCategory fixedExpenseCategory
    ) {
        // When
        FixedExpenseCategory actual = new FixedExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedDefaultAmount,
            expectedVersion
        );

        // Then
        then(actual.id()).isEqualTo(expectedId);
        then(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        then(actual.categoryName()).isEqualTo(expectedCategoryName);
        then(actual.description()).isEqualTo(expectedDescription);
        then(actual.defaultAmount()).isEqualTo(expectedDefaultAmount);
        then(actual.version()).isEqualTo(expectedVersion);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseCategoryData")
    void testId(
        FixedExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Money expectedDefaultAmount,
        Integer expectedVersion,
        FixedExpenseCategory fixedExpenseCategory
    ) {
        // Given
        FixedExpenseCategory category = new FixedExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedDefaultAmount,
            expectedVersion
        );

        // When
        FixedExpenseCategoryId actual = fixedExpenseCategory.id();

        // Then
        then(actual).isEqualTo(expectedId);
        then(fixedExpenseCategory).usingRecursiveComparison().isEqualTo(category);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseCategoryData")
    void testUserGroupId(
        FixedExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Money expectedDefaultAmount,
        Integer expectedVersion,
        FixedExpenseCategory fixedExpenseCategory
    ) {
        // Given
        FixedExpenseCategory category = new FixedExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedDefaultAmount,
            expectedVersion
        );

        // When
        UserGroupId actual = fixedExpenseCategory.userGroupId();

        // Then
        then(actual).isEqualTo(expectedUserGroupId);
        then(fixedExpenseCategory).usingRecursiveComparison().isEqualTo(category);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseCategoryData")
    void testCategoryName(
        FixedExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Money expectedDefaultAmount,
        Integer expectedVersion,
        FixedExpenseCategory fixedExpenseCategory
    ) {
        // Given
        FixedExpenseCategory category = new FixedExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedDefaultAmount,
            expectedVersion
        );

        // When
        CategoryName actual = fixedExpenseCategory.categoryName();

        // Then
        then(actual).isEqualTo(expectedCategoryName);
        then(fixedExpenseCategory).usingRecursiveComparison().isEqualTo(category);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseCategoryData")
    void testDescription(
        FixedExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Money expectedDefaultAmount,
        Integer expectedVersion,
        FixedExpenseCategory fixedExpenseCategory
    ) {
        // Given
        FixedExpenseCategory category = new FixedExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedDefaultAmount,
            expectedVersion
        );

        // When
        Description actual = fixedExpenseCategory.description();

        // Then
        then(actual).isEqualTo(expectedDescription);
        then(fixedExpenseCategory).usingRecursiveComparison().isEqualTo(category);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseCategoryData")
    void testDefaultAmount(
        FixedExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Money expectedDefaultAmount,
        Integer expectedVersion,
        FixedExpenseCategory fixedExpenseCategory
    ) {
        // Given
        FixedExpenseCategory category = new FixedExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedDefaultAmount,
            expectedVersion
        );

        // When
        Money actual = fixedExpenseCategory.defaultAmount();

        // Then
        then(actual).isEqualTo(expectedDefaultAmount);
        then(fixedExpenseCategory).usingRecursiveComparison().isEqualTo(category);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseCategoryData")
    void testVersion(
        FixedExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Money expectedDefaultAmount,
        Integer expectedVersion,
        FixedExpenseCategory fixedExpenseCategory
    ) {
        // Given
        FixedExpenseCategory category = new FixedExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedDefaultAmount,
            expectedVersion
        );

        // When
        Integer actual = fixedExpenseCategory.version();

        // Then
        then(actual).isEqualTo(expectedVersion);
        then(fixedExpenseCategory).usingRecursiveComparison().isEqualTo(category);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseCategoryData")
    void testUpdateCategoryName(
        FixedExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName currentCategoryName,
        Description expectedDescription,
        Money expectedDefaultAmount,
        Integer expectedVersion,
        FixedExpenseCategory fixedExpenseCategory
    ) {
        // Given
        CategoryName expectedNewCategoryName = new CategoryName("新しい分類名");
        FixedExpenseCategory expected = new FixedExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedNewCategoryName,
            expectedDescription,
            expectedDefaultAmount,
            expectedVersion
        );

        // When
        fixedExpenseCategory.updateCategoryName(expectedNewCategoryName);

        // Then
        then(fixedExpenseCategory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseCategoryData")
    void testUpdateDescription(
        FixedExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description currentDescription,
        Money expectedDefaultAmount,
        Integer expectedVersion,
        FixedExpenseCategory fixedExpenseCategory
    ) {
        // Given
        Description expectedNewDescription = new Description("新しい説明");
        FixedExpenseCategory expected = new FixedExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedNewDescription,
            expectedDefaultAmount,
            expectedVersion
        );

        // When
        fixedExpenseCategory.updateDescription(expectedNewDescription);

        // Then
        then(fixedExpenseCategory).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideFixedExpenseCategoryData")
    void testUpdateDefaultAmount(
        FixedExpenseCategoryId expectedId,
        UserGroupId expectedUserGroupId,
        CategoryName expectedCategoryName,
        Description expectedDescription,
        Money currentDefaultAmount,
        Integer expectedVersion,
        FixedExpenseCategory fixedExpenseCategory
    ) {
        // Given
        Money expectedNewDefaultAmount = new Money(50000);
        FixedExpenseCategory expected = new FixedExpenseCategory(
            expectedId,
            expectedUserGroupId,
            expectedCategoryName,
            expectedDescription,
            expectedNewDefaultAmount,
            expectedVersion
        );

        // When
        fixedExpenseCategory.updateDefaultAmount(expectedNewDefaultAmount);

        // Then
        then(fixedExpenseCategory).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testCreate() {
        // Given
        CategoryName expectedCategoryName = new CategoryName("新規分類");
        Description expectedDescription = new Description("新規分類の説明");
        Money expectedDefaultAmount = new Money(10000);
        UserGroupId expectedUserGroupId = new UserGroupId(UUID.randomUUID());

        // When
        FixedExpenseCategory actual = FixedExpenseCategory.create(
            expectedCategoryName,
            expectedDescription,
            expectedDefaultAmount,
            expectedUserGroupId
        );

        // Then
        assertThat(actual.id()).isNotNull();
        assertThat(actual.categoryName()).isEqualTo(expectedCategoryName);
        assertThat(actual.description()).isEqualTo(expectedDescription);
        assertThat(actual.defaultAmount()).isEqualTo(expectedDefaultAmount);
        assertThat(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actual.version()).isNull();
    }
}
