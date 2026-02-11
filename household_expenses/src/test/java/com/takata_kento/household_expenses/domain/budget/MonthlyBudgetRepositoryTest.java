package com.takata_kento.household_expenses.domain.budget;

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
class MonthlyBudgetRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private MonthlyBudgetRepository monthlyBudgetRepository;

    @Autowired
    private JdbcClient jdbcClient;

    final UUID USER_UUID = UUID.randomUUID();
    final UUID BUDGET_UUID_1 = UUID.randomUUID();
    final UUID BUDGET_UUID_2 = UUID.randomUUID();
    final UUID USER_GRORUP_UUID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // テストユーザーを挿入
        jdbcClient
            .sql(
                """
                INSERT INTO
                    users (id, username, password_hash, enabled, version)
                VALUES
                    (:id, :username, :password_hash, :enabled, :version)
                """
            )
            .param("id", USER_UUID.toString())
            .param("username", "testuser")
            .param("password_hash", "dummy_hash")
            .param("enabled", true)
            .param("version", 1)
            .update();

        // ユーザーグループを挿入
        jdbcClient
            .sql(
                """
                INSERT INTO
                    user_group (id, group_name, month_start_day, created_by_user_id, version)
                VALUES
                    (:id, :group_name, :month_start_day, :created_by_user_id, :version)
                """
            )
            .param("id", USER_GRORUP_UUID.toString())
            .param("group_name", "Test Group")
            .param("month_start_day", 1)
            .param("created_by_user_id", USER_UUID.toString())
            .param("version", 1)
            .update();

        // 月予算情報を挿入
        jdbcClient
            .sql(
                """
                INSERT INTO
                    monthly_budget (id, user_group_id, year, month, budget_amount, set_by_user_id, created_at, version)
                VALUES
                    (:id, :user_group_id, :year, :month, :budget_amount, :set_by_user_id, :created_at, :version)
                """
            )
            .param("id", BUDGET_UUID_1.toString())
            .param("user_group_id", USER_GRORUP_UUID.toString())
            .param("year", 2024)
            .param("month", 7)
            .param("budget_amount", 100000)
            .param("set_by_user_id", USER_UUID.toString())
            .param("created_at", LocalDateTime.of(2024, 7, 1, 10, 0, 0))
            .param("version", 1)
            .update();

        jdbcClient
            .sql(
                """
                INSERT INTO
                    monthly_budget (id, user_group_id, year, month, budget_amount, set_by_user_id, created_at, version)
                VALUES
                    (:id, :user_group_id, :year, :month, :budget_amount, :set_by_user_id, :created_at, :version)
                """
            )
            .param("id", BUDGET_UUID_2.toString())
            .param("user_group_id", USER_GRORUP_UUID.toString())
            .param("year", 2024)
            .param("month", 11)
            .param("budget_amount", 95000)
            .param("set_by_user_id", USER_UUID.toString())
            .param("created_at", LocalDateTime.of(2024, 7, 1, 10, 0, 0))
            .param("version", 1)
            .update();
    }

    @Test
    void testSave() {
        // Given
        int year = 2024;
        int month = 6;
        int budgetAmount = 100000;

        MonthlyBudget monthlyBudget = MonthlyBudget.create(
            new UserGroupId(USER_GRORUP_UUID),
            new Year(year),
            new Month(month),
            new Money(budgetAmount),
            new UserId(USER_UUID)
        );

        // When
        MonthlyBudget savedMonthlyBudget = monthlyBudgetRepository.save(monthlyBudget);

        // Then
        // DBから直接確認
        String userGroupIdFromDb = jdbcClient
            .sql("SELECT user_group_id FROM monthly_budget WHERE id = ?")
            .param(savedMonthlyBudget.id().toString())
            .query(String.class)
            .single();
        assertThat(userGroupIdFromDb).isEqualTo(USER_GRORUP_UUID.toString());

        Integer budgetAmountFromDb = jdbcClient
            .sql("SELECT budget_amount FROM monthly_budget WHERE id = ?")
            .param(savedMonthlyBudget.id().toString())
            .query(Integer.class)
            .single();
        assertThat(budgetAmountFromDb).isEqualTo(budgetAmount);

        Integer yearFromDb = jdbcClient
            .sql("SELECT year FROM monthly_budget WHERE id = ?")
            .param(savedMonthlyBudget.id().toString())
            .query(Integer.class)
            .single();
        assertThat(yearFromDb).isEqualTo(year);

        Integer monthFromDb = jdbcClient
            .sql("SELECT month FROM monthly_budget WHERE id = ?")
            .param(savedMonthlyBudget.id().toString())
            .query(Integer.class)
            .single();
        assertThat(monthFromDb).isEqualTo(month);

        String setByUserIdFromDb = jdbcClient
            .sql("SELECT set_by_user_id FROM monthly_budget WHERE id = ?")
            .param(savedMonthlyBudget.id().toString())
            .query(String.class)
            .single();
        assertThat(setByUserIdFromDb).isEqualTo(USER_UUID.toString());
    }

    @Test
    void testUpdate() {
        // Given
        MonthlyBudgetId monthlyBudgetId = new MonthlyBudgetId(BUDGET_UUID_1);
        Money newBudgetAmount = new Money(120000);

        // When
        MonthlyBudget monthlyBudget = monthlyBudgetRepository.findById(monthlyBudgetId).orElseThrow();
        monthlyBudget.updateBudgetAmount(newBudgetAmount, monthlyBudget.setByUserId());
        monthlyBudgetRepository.save(monthlyBudget);

        // Then
        // DBから直接確認
        Integer budgetAmountFromDb = jdbcClient
            .sql("SELECT budget_amount FROM monthly_budget WHERE id = ?")
            .param(monthlyBudgetId.toString())
            .query(Integer.class)
            .single();
        assertThat(budgetAmountFromDb).isEqualTo(120000);
    }

    @Test
    void testFindById() {
        // Given
        MonthlyBudgetId expectedId = new MonthlyBudgetId(BUDGET_UUID_1);
        UserGroupId expectedUserGroupId = new UserGroupId(USER_GRORUP_UUID);
        Year expectedYear = new Year(2024);
        Month expectedMonth = new Month(7);
        Money expectedBudgetAmount = new Money(100000);
        UserId expectedSetByUserId = new UserId(USER_UUID);
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2024, 7, 1, 10, 0, 0);
        LocalDateTime expectedUpdatedAt = null;
        Integer expectedVersion = 1;

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
        MonthlyBudgetId expectedId = new MonthlyBudgetId(BUDGET_UUID_1);
        UserGroupId expectedUserGroupId = new UserGroupId(USER_GRORUP_UUID);
        Year expectedYear = new Year(2024);
        Month expectedMonth = new Month(7);
        Money expectedBudgetAmount = new Money(100000);
        UserId expectedSetByUserId = new UserId(USER_UUID);
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2024, 7, 1, 10, 0, 0);
        LocalDateTime expectedUpdatedAt = null;
        Integer expectedVersion = 1;

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
        UserGroupId expectedUserGroupId = new UserGroupId(USER_GRORUP_UUID);
        Year expectedYear = new Year(2024);
        Month expectedMonth = new Month(7);

        boolean expectedExists = true;
        boolean expectedNotExists = false;

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
        UserGroupId expectedUserGroupId = new UserGroupId(USER_GRORUP_UUID);
        int expectedListSize = 2;

        // When
        List<MonthlyBudget> actualList = monthlyBudgetRepository.findByUserGroupId(expectedUserGroupId);

        // Then
        assertThat(actualList).hasSize(expectedListSize);
        assertThat(actualList.get(0).userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actualList.get(1).userGroupId()).isEqualTo(expectedUserGroupId);
    }
}
