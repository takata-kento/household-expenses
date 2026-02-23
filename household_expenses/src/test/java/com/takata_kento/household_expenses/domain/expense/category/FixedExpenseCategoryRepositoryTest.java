package com.takata_kento.household_expenses.domain.expense.category;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@DataJdbcTest
@Testcontainers
@Sql("/schema.sql")
class FixedExpenseCategoryRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private FixedExpenseCategoryRepository fixedExpenseCategoryRepository;

    @Autowired
    private JdbcClient jdbcClient;

    private final UUID USER_GROUP_UUID_1 = UUID.randomUUID();
    private final UUID USER_GROUP_UUID_2 = UUID.randomUUID();
    private final UUID CATEGORY_UUID_1 = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        // ユーザーグループを挿入
        jdbcClient
            .sql(
                """
                INSERT INTO
                    user_group (id, group_name, month_start_day, version)
                VALUES
                    (:id, :group_name, :month_start_day, :version)
                """
            )
            .param("id", USER_GROUP_UUID_1.toString())
            .param("group_name", "Test Group 1")
            .param("month_start_day", 1)
            .param("version", 0)
            .update();

        jdbcClient
            .sql(
                """
                INSERT INTO
                    user_group (id, group_name, month_start_day, version)
                VALUES
                    (:id, :group_name, :month_start_day, :version)
                """
            )
            .param("id", USER_GROUP_UUID_2.toString())
            .param("group_name", "Test Group 2")
            .param("month_start_day", 1)
            .param("version", 0)
            .update();

        // 固定費分類を挿入
        jdbcClient
            .sql(
                """
                INSERT INTO fixed_expense_category (id, user_group_id, category_name, description, default_amount, created_at, updated_at, version)
                VALUES (:id, :user_group_id, '家賃', '毎月の家賃', 80000, :created_at, :updated_at, 0)
                """
            )
            .param("id", CATEGORY_UUID_1.toString())
            .param("user_group_id", USER_GROUP_UUID_1.toString())
            .param("created_at", now)
            .param("updated_at", now)
            .update();

        jdbcClient
            .sql(
                """
                INSERT INTO fixed_expense_category (id, user_group_id, category_name, description, default_amount, created_at, updated_at, version)
                VALUES (:id, :user_group_id, '光熱費', '電気・ガス・水道', 15000, :created_at, :updated_at, 0)
                """
            )
            .param("id", UUID.randomUUID().toString())
            .param("user_group_id", USER_GROUP_UUID_1.toString())
            .param("created_at", now)
            .param("updated_at", now)
            .update();

        jdbcClient
            .sql(
                """
                INSERT INTO fixed_expense_category (id, user_group_id, category_name, description, default_amount, created_at, updated_at, version)
                VALUES (:id, :user_group_id, '通信費', 'インターネット・携帯', 5000, :created_at, :updated_at, 0)
                """
            )
            .param("id", UUID.randomUUID().toString())
            .param("user_group_id", USER_GROUP_UUID_2.toString())
            .param("created_at", now)
            .param("updated_at", now)
            .update();
    }

    @Test
    void testFindByUserGroupId() {
        // Given
        UserGroupId userGroupId = new UserGroupId(USER_GROUP_UUID_1);

        // When
        List<FixedExpenseCategory> actual = fixedExpenseCategoryRepository.findByUserGroupId(userGroupId);

        // Then
        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(FixedExpenseCategory::userGroupId).containsOnly(userGroupId);
        assertThat(actual)
            .extracting(FixedExpenseCategory::categoryName)
            .containsExactlyInAnyOrder(new CategoryName("家賃"), new CategoryName("光熱費"));
    }

    @Test
    void testSave() {
        // Given
        CategoryName expectedCategoryName = new CategoryName("保険料");
        Description expectedDescription = new Description("生命保険・医療保険");
        Money expectedDefaultAmount = new Money(20_000);
        UserGroupId expectedUserGroupId = new UserGroupId(USER_GROUP_UUID_1);

        FixedExpenseCategory newCategory = FixedExpenseCategory.create(
            expectedCategoryName,
            expectedDescription,
            expectedDefaultAmount,
            expectedUserGroupId
        );

        // When
        FixedExpenseCategory savedCategory = fixedExpenseCategoryRepository.save(newCategory);

        // Then
        String userGroupIdFromDb = jdbcClient
            .sql("SELECT user_group_id FROM fixed_expense_category WHERE id = ?")
            .param(savedCategory.id().toString())
            .query(String.class).single();
        assertThat(userGroupIdFromDb).isEqualTo(USER_GROUP_UUID_1.toString());

        String categoryNameFromDb = jdbcClient
            .sql("SELECT category_name FROM fixed_expense_category WHERE id = ?")
            .param(savedCategory.id().toString())
            .query(String.class).single();
        assertThat(categoryNameFromDb).isEqualTo(expectedCategoryName.value());

        String descriptionFromDb = jdbcClient
            .sql("SELECT description FROM fixed_expense_category WHERE id = ?")
            .param(savedCategory.id().toString())
            .query(String.class).single();
        assertThat(descriptionFromDb).isEqualTo(expectedDescription.value());

        Integer defaultAmountFromDb = jdbcClient
            .sql("SELECT default_amount FROM fixed_expense_category WHERE id = ?")
            .param(savedCategory.id().toString())
            .query(Integer.class).single();
        assertThat(defaultAmountFromDb).isEqualTo(expectedDefaultAmount.amount());
    }

    @Test
    void testUpdate() {
        // Given
        FixedExpenseCategoryId categoryId = new FixedExpenseCategoryId(CATEGORY_UUID_1);
        CategoryName newCategoryName = new CategoryName("更新後の家賃");
        Description newDescription = new Description("更新後の説明");
        Money newDefaultAmount = new Money(90_000);

        // When
        FixedExpenseCategory category = fixedExpenseCategoryRepository.findById(categoryId).orElseThrow();
        category.updateCategoryName(newCategoryName);
        category.updateDescription(newDescription);
        category.updateDefaultAmount(newDefaultAmount);
        fixedExpenseCategoryRepository.save(category);

        // Then
        String categoryNameFromDb = jdbcClient
            .sql("SELECT category_name FROM fixed_expense_category WHERE id = ?")
            .param(categoryId.toString())
            .query(String.class).single();
        assertThat(categoryNameFromDb).isEqualTo(newCategoryName.value());

        String descriptionFromDb = jdbcClient
            .sql("SELECT description FROM fixed_expense_category WHERE id = ?")
            .param(categoryId.toString())
            .query(String.class).single();
        assertThat(descriptionFromDb).isEqualTo(newDescription.value());

        Integer defaultAmountFromDb = jdbcClient
            .sql("SELECT default_amount FROM fixed_expense_category WHERE id = ?")
            .param(categoryId.toString())
            .query(Integer.class).single();
        assertThat(defaultAmountFromDb).isEqualTo(newDefaultAmount.amount());
    }
}
