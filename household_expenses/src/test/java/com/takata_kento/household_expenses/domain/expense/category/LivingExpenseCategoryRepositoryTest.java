package com.takata_kento.household_expenses.domain.expense.category;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
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
class LivingExpenseCategoryRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private LivingExpenseCategoryRepository livingExpenseCategoryRepository;

    @Autowired
    private JdbcClient jdbcClient;

    private final UUID USER_GROUP_UUID_1 = UUID.randomUUID();
    private final UUID USER_GROUP_UUID_2 = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // テストデータ挿入
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

        jdbcClient
            .sql(
                """
                INSERT INTO living_expense_category (id, user_group_id, category_name, description, is_default, created_at, updated_at, version)
                VALUES (:id, :user_group_id, '食費', '食材・外食費', true, :created_at, :updated_at, 0)
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
                INSERT INTO living_expense_category (id, user_group_id, category_name, description, is_default, created_at, updated_at, version)
                VALUES (:id, :user_group_id, '交通費', '電車・バス代', false, :created_at, :updated_at, 0)
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
                INSERT INTO living_expense_category (id, user_group_id, category_name, description, is_default, created_at, updated_at, version)
                VALUES (:id, :user_group_id, '日用品', '生活雑貨', false, :created_at, :updated_at, 0)
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
        List<LivingExpenseCategory> actual = livingExpenseCategoryRepository.findByUserGroupId(userGroupId);

        // Then
        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(LivingExpenseCategory::userGroupId).containsOnly(userGroupId);
        assertThat(actual)
            .extracting(LivingExpenseCategory::categoryName)
            .containsExactlyInAnyOrder(new CategoryName("食費"), new CategoryName("交通費"));
    }

    @Test
    void testFindByUserGroupIdAndIsDefault() {
        // Given
        UserGroupId userGroupId = new UserGroupId(USER_GROUP_UUID_1);

        // When
        List<LivingExpenseCategory> actualDefault = livingExpenseCategoryRepository.findByUserGroupIdAndIsDefault(
            userGroupId,
            true
        );

        List<LivingExpenseCategory> actualNonDefault = livingExpenseCategoryRepository.findByUserGroupIdAndIsDefault(
            userGroupId,
            false
        );

        // Then
        assertThat(actualDefault).hasSize(1);
        assertThat(actualDefault.get(0).categoryName()).isEqualTo(new CategoryName("食費"));
        assertThat(actualDefault.get(0).isDefault()).isTrue();

        assertThat(actualNonDefault).hasSize(1);
        assertThat(actualNonDefault.get(0).categoryName()).isEqualTo(new CategoryName("交通費"));
        assertThat(actualNonDefault.get(0).isDefault()).isFalse();
    }
}
