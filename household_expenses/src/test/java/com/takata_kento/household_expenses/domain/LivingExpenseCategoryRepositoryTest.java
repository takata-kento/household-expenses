package com.takata_kento.household_expenses.domain;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@Testcontainers
class LivingExpenseCategoryRepositoryTest {

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private LivingExpenseCategoryRepository livingExpenseCategoryRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        // 関連テーブルをカスケード削除
        jdbcClient.sql("DROP TABLE IF EXISTS living_expense_category CASCADE").update();

        // living_expense_categoryテーブル作成
        jdbcClient
            .sql(
                """
                CREATE TABLE living_expense_category (
                    id BIGINT PRIMARY KEY,
                    user_group_id BIGINT NOT NULL,
                    category_name VARCHAR(255) NOT NULL,
                    description VARCHAR(1000),
                    is_default BOOLEAN NOT NULL DEFAULT FALSE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    version INTEGER
                )
                """
            )
            .update();

        // テストデータ挿入
        LocalDateTime now = LocalDateTime.now();

        jdbcClient
            .sql(
                """
                INSERT INTO living_expense_category (id, user_group_id, category_name, description, is_default, created_at, updated_at, version)
                VALUES (1, 100, '食費', '食材・外食費', true, ?, ?, 0)
                """
            )
            .param(now)
            .param(now)
            .update();

        jdbcClient
            .sql(
                """
                INSERT INTO living_expense_category (id, user_group_id, category_name, description, is_default, created_at, updated_at, version)
                VALUES (2, 100, '交通費', '電車・バス代', false, ?, ?, 0)
                """
            )
            .param(now)
            .param(now)
            .update();

        jdbcClient
            .sql(
                """
                INSERT INTO living_expense_category (id, user_group_id, category_name, description, is_default, created_at, updated_at, version)
                VALUES (3, 200, '日用品', '生活雑貨', false, ?, ?, 0)
                """
            )
            .param(now)
            .param(now)
            .update();
    }

    @Test
    void testFindByUserGroupId() {
        // Given
        UserGroupId userGroupId = new UserGroupId(100L);

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
        UserGroupId userGroupId = new UserGroupId(100L);

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
