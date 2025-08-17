package com.takata_kento.household_expenses.domain;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.MonthlyBudgetId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
class MonthlyBudgetRepositoryTest {

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
    private MonthlyBudgetRepository monthlyBudgetRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        // 関連テーブルをカスケード削除
        jdbcClient.sql("DROP TABLE IF EXISTS monthly_budget CASCADE").update();
        jdbcClient.sql("DROP TABLE IF EXISTS users CASCADE").update();
        jdbcClient.sql("DROP TABLE IF EXISTS user_group CASCADE").update();

        // user_groupテーブル作成
        jdbcClient
            .sql(
                """
                CREATE TABLE user_group (
                    id BIGSERIAL PRIMARY KEY,
                    group_name VARCHAR(255) NOT NULL,
                    month_start_day INTEGER NOT NULL DEFAULT 1 CHECK (month_start_day >= 1 AND month_start_day <= 31),
                    created_by_user_id BIGINT,
                    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP WITH TIME ZONE,
                    version INTEGER DEFAULT 0
                )
                """
            )
            .update();

        // usersテーブル作成
        jdbcClient
            .sql(
                """
                CREATE TABLE "users" (
                    id BIGSERIAL PRIMARY KEY,
                    username VARCHAR(255) UNIQUE NOT NULL,
                    password_hash VARCHAR(255) NOT NULL,
                    user_group_id BIGINT REFERENCES user_group(id) ON DELETE SET NULL,
                    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP WITH TIME ZONE,
                    enabled boolean,
                    version INTEGER DEFAULT 0
                )
                """
            )
            .update();

        // user_groupのcreated_by_user_idに外部キー制約を追加
        jdbcClient
            .sql(
                "ALTER TABLE user_group ADD FOREIGN KEY (created_by_user_id) REFERENCES \"users\"(id) ON DELETE SET NULL"
            )
            .update();

        // monthly_budgetテーブル作成
        jdbcClient
            .sql(
                """
                CREATE TABLE monthly_budget (
                    id BIGINT PRIMARY KEY,
                    user_group_id BIGINT NOT NULL REFERENCES user_group(id) ON DELETE CASCADE,
                    year INTEGER NOT NULL,
                    month INTEGER NOT NULL CHECK (month >= 1 AND month <= 12),
                    budget_amount INTEGER NOT NULL,
                    set_by_user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
                    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP WITH TIME ZONE,
                    version INTEGER DEFAULT 0,
                    UNIQUE (user_group_id, year, month)
                )
                """
            )
            .update();
    }

    @Test
    void testSave() {
        // Given
        long userGroupId = 1L;
        long setByUserId = 1L;
        int year = 2024;
        int month = 6;
        int budgetAmount = 100000;

        // ユーザーを先に挿入
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, enabled, version) VALUES (?, ?, ?, ?, ?)")
            .params(setByUserId, "testuser", "dummy_hash", true, 1)
            .update();

        // ユーザーグループを挿入
        jdbcClient
            .sql(
                "INSERT INTO user_group (id, group_name, month_start_day, created_by_user_id, version) VALUES (?, ?, ?, ?, ?)"
            )
            .params(userGroupId, "Test Group", 1, setByUserId, 1)
            .update();

        MonthlyBudget monthlyBudget = MonthlyBudget.create(
            new UserGroupId(userGroupId),
            new Year(year),
            new Month(month),
            new Money(budgetAmount),
            new UserId(setByUserId)
        );

        // When
        MonthlyBudget savedMonthlyBudget = monthlyBudgetRepository.save(monthlyBudget);

        // Then
        assertThat(savedMonthlyBudget.id()).isNotNull();
        assertThat(savedMonthlyBudget.userGroupId()).isEqualTo(new UserGroupId(userGroupId));
        assertThat(savedMonthlyBudget.year()).isEqualTo(new Year(year));
        assertThat(savedMonthlyBudget.month()).isEqualTo(new Month(month));
        assertThat(savedMonthlyBudget.budgetAmount()).isEqualTo(new Money(budgetAmount));
        assertThat(savedMonthlyBudget.setByUserId()).isEqualTo(new UserId(setByUserId));
        assertThat(savedMonthlyBudget.createdAt()).isNotNull();
        assertThat(savedMonthlyBudget.version()).isNotNull();

        // DBから直接確認
        Integer budgetAmountFromDb = jdbcClient
            .sql("SELECT budget_amount FROM monthly_budget WHERE id = ?")
            .param(savedMonthlyBudget.id().value())
            .query(Integer.class)
            .single();
        assertThat(budgetAmountFromDb).isEqualTo(budgetAmount);

        Integer yearFromDb = jdbcClient
            .sql("SELECT year FROM monthly_budget WHERE id = ?")
            .param(savedMonthlyBudget.id().value())
            .query(Integer.class)
            .single();
        assertThat(yearFromDb).isEqualTo(year);

        Integer monthFromDb = jdbcClient
            .sql("SELECT month FROM monthly_budget WHERE id = ?")
            .param(savedMonthlyBudget.id().value())
            .query(Integer.class)
            .single();
        assertThat(monthFromDb).isEqualTo(month);
    }

    @Test
    void testFindById() {
        // Given
        MonthlyBudgetId expectedId = new MonthlyBudgetId(2L);
        UserGroupId expectedUserGroupId = new UserGroupId(2L);
        Year expectedYear = new Year(2024);
        Month expectedMonth = new Month(7);
        Money expectedBudgetAmount = new Money(120000);
        UserId expectedSetByUserId = new UserId(20L);
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2024, 7, 1, 10, 0, 0);
        LocalDateTime expectedUpdatedAt = null;
        Integer expectedVersion = 0;

        // ユーザーを先に挿入
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, enabled, version) VALUES (?, ?, ?, ?, ?)")
            .params(expectedSetByUserId.value(), "testuser2", "dummy_hash", true, 1)
            .update();

        // ユーザーグループを挿入
        jdbcClient
            .sql(
                "INSERT INTO user_group (id, group_name, month_start_day, created_by_user_id, version) VALUES (?, ?, ?, ?, ?)"
            )
            .params(expectedUserGroupId.value(), "テストグループ2", 1, expectedSetByUserId.value(), 1)
            .update();

        jdbcClient
            .sql(
                "INSERT INTO monthly_budget (id, user_group_id, year, month, budget_amount, set_by_user_id, created_at, version) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            )
            .params(
                expectedId.value(),
                expectedUserGroupId.value(),
                expectedYear.value(),
                expectedMonth.value(),
                expectedBudgetAmount.amount(),
                expectedSetByUserId.value(),
                expectedCreatedAt,
                expectedVersion
            )
            .update();

        // When
        Optional<MonthlyBudget> actualOptional = monthlyBudgetRepository.findById(expectedId);

        // Then
        assertThat(actualOptional).isPresent();
        MonthlyBudget actual = actualOptional.get();
        assertThat(actual.id()).isEqualTo(expectedId);
        assertThat(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actual.year()).isEqualTo(expectedYear);
        assertThat(actual.month()).isEqualTo(expectedMonth);
        assertThat(actual.budgetAmount()).isEqualTo(expectedBudgetAmount);
        assertThat(actual.setByUserId()).isEqualTo(expectedSetByUserId);
        assertThat(actual.createdAt()).isEqualTo(expectedCreatedAt);
        assertThat(actual.updatedAt()).isEqualTo(expectedUpdatedAt);
        assertThat(actual.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testFindByUserGroupIdAndYearAndMonth() {
        // Given
        MonthlyBudgetId expectedId = new MonthlyBudgetId(3L);
        UserGroupId expectedUserGroupId = new UserGroupId(3L);
        Year expectedYear = new Year(2024);
        Month expectedMonth = new Month(8);
        Money expectedBudgetAmount = new Money(150000);
        UserId expectedSetByUserId = new UserId(30L);
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2024, 8, 1, 10, 0, 0);
        LocalDateTime expectedUpdatedAt = null;
        Integer expectedVersion = 0;

        // ユーザーを先に挿入
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, enabled, version) VALUES (?, ?, ?, ?, ?)")
            .params(expectedSetByUserId.value(), "testuser3", "dummy_hash", true, 1)
            .update();

        // ユーザーグループを挿入
        jdbcClient
            .sql(
                "INSERT INTO user_group (id, group_name, month_start_day, created_by_user_id, version) VALUES (?, ?, ?, ?, ?)"
            )
            .params(expectedUserGroupId.value(), "テストグループ3", 1, expectedSetByUserId.value(), 1)
            .update();

        jdbcClient
            .sql(
                "INSERT INTO monthly_budget (id, user_group_id, year, month, budget_amount, set_by_user_id, created_at, version) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            )
            .params(
                expectedId.value(),
                expectedUserGroupId.value(),
                expectedYear.value(),
                expectedMonth.value(),
                expectedBudgetAmount.amount(),
                expectedSetByUserId.value(),
                expectedCreatedAt,
                expectedVersion
            )
            .update();

        // When
        Optional<MonthlyBudget> actualOptional = monthlyBudgetRepository.findByUserGroupIdAndYearAndMonth(
            expectedUserGroupId,
            expectedYear,
            expectedMonth
        );

        // Then
        assertThat(actualOptional).isPresent();
        MonthlyBudget actual = actualOptional.get();
        assertThat(actual.id()).isEqualTo(expectedId);
        assertThat(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actual.year()).isEqualTo(expectedYear);
        assertThat(actual.month()).isEqualTo(expectedMonth);
        assertThat(actual.budgetAmount()).isEqualTo(expectedBudgetAmount);
        assertThat(actual.setByUserId()).isEqualTo(expectedSetByUserId);
        assertThat(actual.createdAt()).isEqualTo(expectedCreatedAt);
        assertThat(actual.updatedAt()).isEqualTo(expectedUpdatedAt);
        assertThat(actual.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testExistsByUserGroupIdAndYearAndMonth() {
        // Given
        MonthlyBudgetId expectedId = new MonthlyBudgetId(4L);
        UserGroupId expectedUserGroupId = new UserGroupId(4L);
        Year expectedYear = new Year(2024);
        Month expectedMonth = new Month(9);
        Money expectedBudgetAmount = new Money(80000);
        UserId expectedSetByUserId = new UserId(40L);
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2024, 9, 1, 10, 0, 0);
        Integer expectedVersion = 0;
        boolean expectedExists = true;
        boolean expectedNotExists = false;

        // ユーザーを先に挿入
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, enabled, version) VALUES (?, ?, ?, ?, ?)")
            .params(expectedSetByUserId.value(), "testuser4", "dummy_hash", true, 1)
            .update();

        // ユーザーグループを挿入
        jdbcClient
            .sql(
                "INSERT INTO user_group (id, group_name, month_start_day, created_by_user_id, version) VALUES (?, ?, ?, ?, ?)"
            )
            .params(expectedUserGroupId.value(), "テストグループ4", 1, expectedSetByUserId.value(), 1)
            .update();

        jdbcClient
            .sql(
                "INSERT INTO monthly_budget (id, user_group_id, year, month, budget_amount, set_by_user_id, created_at, version) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            )
            .params(
                expectedId.value(),
                expectedUserGroupId.value(),
                expectedYear.value(),
                expectedMonth.value(),
                expectedBudgetAmount.amount(),
                expectedSetByUserId.value(),
                expectedCreatedAt,
                expectedVersion
            )
            .update();

        // When
        boolean actualExists = monthlyBudgetRepository.existsByUserGroupIdAndYearAndMonth(
            expectedUserGroupId,
            expectedYear,
            expectedMonth
        );
        boolean actualNotExists = monthlyBudgetRepository.existsByUserGroupIdAndYearAndMonth(
            expectedUserGroupId,
            expectedYear,
            new Month(10)
        );

        // Then
        assertThat(actualExists).isEqualTo(expectedExists);
        assertThat(actualNotExists).isEqualTo(expectedNotExists);
    }

    @Test
    void testFindByUserGroupId() {
        // Given
        MonthlyBudgetId expectedId1 = new MonthlyBudgetId(5L);
        MonthlyBudgetId expectedId2 = new MonthlyBudgetId(6L);
        UserGroupId expectedUserGroupId = new UserGroupId(5L);
        Year expectedYear1 = new Year(2024);
        Month expectedMonth1 = new Month(10);
        Year expectedYear2 = new Year(2024);
        Month expectedMonth2 = new Month(11);
        Money expectedBudgetAmount1 = new Money(90000);
        Money expectedBudgetAmount2 = new Money(95000);
        UserId expectedSetByUserId = new UserId(50L);
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2024, 10, 1, 10, 0, 0);
        Integer expectedVersion = 0;
        int expectedListSize = 2;

        // ユーザーを先に挿入
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, enabled, version) VALUES (?, ?, ?, ?, ?)")
            .params(expectedSetByUserId.value(), "testuser5", "dummy_hash", true, 1)
            .update();

        // ユーザーグループを挿入
        jdbcClient
            .sql(
                "INSERT INTO user_group (id, group_name, month_start_day, created_by_user_id, version) VALUES (?, ?, ?, ?, ?)"
            )
            .params(expectedUserGroupId.value(), "テストグループ5", 1, expectedSetByUserId.value(), 1)
            .update();

        jdbcClient
            .sql(
                "INSERT INTO monthly_budget (id, user_group_id, year, month, budget_amount, set_by_user_id, created_at, version) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            )
            .params(
                expectedId1.value(),
                expectedUserGroupId.value(),
                expectedYear1.value(),
                expectedMonth1.value(),
                expectedBudgetAmount1.amount(),
                expectedSetByUserId.value(),
                expectedCreatedAt,
                expectedVersion
            )
            .update();

        jdbcClient
            .sql(
                "INSERT INTO monthly_budget (id, user_group_id, year, month, budget_amount, set_by_user_id, created_at, version) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            )
            .params(
                expectedId2.value(),
                expectedUserGroupId.value(),
                expectedYear2.value(),
                expectedMonth2.value(),
                expectedBudgetAmount2.amount(),
                expectedSetByUserId.value(),
                expectedCreatedAt,
                expectedVersion
            )
            .update();

        // When
        List<MonthlyBudget> actualList = monthlyBudgetRepository.findByUserGroupId(expectedUserGroupId);

        // Then
        assertThat(actualList).hasSize(expectedListSize);
        assertThat(actualList.get(0).userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actualList.get(1).userGroupId()).isEqualTo(expectedUserGroupId);
    }
}
