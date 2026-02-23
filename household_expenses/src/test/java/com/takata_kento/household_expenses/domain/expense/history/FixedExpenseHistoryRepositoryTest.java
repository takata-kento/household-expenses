package com.takata_kento.household_expenses.domain.expense.history;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseHistoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
class FixedExpenseHistoryRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private FixedExpenseHistoryRepository fixedExpenseHistoryRepository;

    @Autowired
    private JdbcClient jdbcClient;

    private final UUID USER_GROUP_UUID = UUID.randomUUID();
    private final UUID CATEGORY_UUID_1 = UUID.randomUUID();
    private final UUID CATEGORY_UUID_2 = UUID.randomUUID();
    private final UUID HISTORY_UUID_1 = UUID.randomUUID();

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
            .param("id", USER_GROUP_UUID.toString())
            .param("group_name", "Test Group")
            .param("month_start_day", 1)
            .param("version", 0)
            .update();

        // 固定費分類1を挿入
        jdbcClient
            .sql(
                """
                INSERT INTO fixed_expense_category (id, user_group_id, category_name, description, default_amount, created_at, updated_at, version)
                VALUES (:id, :user_group_id, '家賃', '毎月の家賃', 80000, :created_at, :updated_at, 0)
                """
            )
            .param("id", CATEGORY_UUID_1.toString())
            .param("user_group_id", USER_GROUP_UUID.toString())
            .param("created_at", now)
            .param("updated_at", now)
            .update();

        // 固定費分類2を挿入
        jdbcClient
            .sql(
                """
                INSERT INTO fixed_expense_category (id, user_group_id, category_name, description, default_amount, created_at, updated_at, version)
                VALUES (:id, :user_group_id, '光熱費', '電気・ガス・水道', 15000, :created_at, :updated_at, 0)
                """
            )
            .param("id", CATEGORY_UUID_2.toString())
            .param("user_group_id", USER_GROUP_UUID.toString())
            .param("created_at", now)
            .param("updated_at", now)
            .update();

        // 固定費履歴を挿入
        jdbcClient
            .sql(
                """
                INSERT INTO fixed_expense_history (id, fixed_expense_category_id, year, month, amount, effective_date, memo, created_at, updated_at, version)
                VALUES (:id, :fixed_expense_category_id, :year, :month, :amount, :effective_date, :memo, :created_at, :updated_at, 0)
                """
            )
            .param("id", HISTORY_UUID_1.toString())
            .param("fixed_expense_category_id", CATEGORY_UUID_1.toString())
            .param("year", 2026)
            .param("month", 2)
            .param("amount", 80_000)
            .param("effective_date", LocalDate.of(2026, 2, 1))
            .param("memo", "毎月の家賃")
            .param("created_at", now)
            .param("updated_at", now)
            .update();
    }

    @Test
    void testSave() {
        // Given
        FixedExpenseCategoryId expectedCategoryId = new FixedExpenseCategoryId(CATEGORY_UUID_1);
        Year expectedYear = new Year(2026);
        Month expectedMonth = new Month(3);
        Money expectedAmount = new Money(80_000);
        LocalDate expectedEffectiveDate = LocalDate.of(2026, 3, 1);
        Optional<Description> expectedMemo = Optional.of(new Description("3月の家賃"));

        FixedExpenseHistory newHistory = FixedExpenseHistory.create(
            expectedCategoryId,
            expectedYear,
            expectedMonth,
            expectedAmount,
            expectedEffectiveDate,
            expectedMemo
        );

        // When
        FixedExpenseHistory savedHistory = fixedExpenseHistoryRepository.save(newHistory);

        // Then
        String categoryIdFromDb = jdbcClient
            .sql("SELECT fixed_expense_category_id FROM fixed_expense_history WHERE id = ?")
            .param(savedHistory.id().toString())
            .query(String.class)
            .single();
        assertThat(categoryIdFromDb).isEqualTo(CATEGORY_UUID_1.toString());

        Integer yearFromDb = jdbcClient
            .sql("SELECT year FROM fixed_expense_history WHERE id = ?")
            .param(savedHistory.id().toString())
            .query(Integer.class)
            .single();
        assertThat(yearFromDb).isEqualTo(expectedYear.value());

        Integer monthFromDb = jdbcClient
            .sql("SELECT month FROM fixed_expense_history WHERE id = ?")
            .param(savedHistory.id().toString())
            .query(Integer.class)
            .single();
        assertThat(monthFromDb).isEqualTo(expectedMonth.value());

        Integer amountFromDb = jdbcClient
            .sql("SELECT amount FROM fixed_expense_history WHERE id = ?")
            .param(savedHistory.id().toString())
            .query(Integer.class)
            .single();
        assertThat(amountFromDb).isEqualTo(expectedAmount.amount());

        LocalDate effectiveDateFromDb = jdbcClient
            .sql("SELECT effective_date FROM fixed_expense_history WHERE id = ?")
            .param(savedHistory.id().toString())
            .query(LocalDate.class)
            .single();
        assertThat(effectiveDateFromDb).isEqualTo(expectedEffectiveDate);

        String memoFromDb = jdbcClient
            .sql("SELECT memo FROM fixed_expense_history WHERE id = ?")
            .param(savedHistory.id().toString())
            .query(String.class)
            .single();
        assertThat(memoFromDb).isEqualTo(expectedMemo.get().value());
    }

    @Test
    void testUpdate() {
        // Given
        FixedExpenseHistoryId historyId = new FixedExpenseHistoryId(HISTORY_UUID_1);
        Money newAmount = new Money(85_000);
        LocalDate newEffectiveDate = LocalDate.of(2026, 2, 15);
        Optional<Description> newMemo = Optional.of(new Description("更新後のメモ"));

        // When
        FixedExpenseHistory history = fixedExpenseHistoryRepository.findById(historyId).orElseThrow();
        history.updateAmount(newAmount);
        history.updateEffectiveDate(newEffectiveDate);
        history.updateMemo(newMemo);
        fixedExpenseHistoryRepository.save(history);

        // Then
        Integer amountFromDb = jdbcClient
            .sql("SELECT amount FROM fixed_expense_history WHERE id = ?")
            .param(historyId.toString())
            .query(Integer.class)
            .single();
        assertThat(amountFromDb).isEqualTo(newAmount.amount());

        LocalDate effectiveDateFromDb = jdbcClient
            .sql("SELECT effective_date FROM fixed_expense_history WHERE id = ?")
            .param(historyId.toString())
            .query(LocalDate.class)
            .single();
        assertThat(effectiveDateFromDb).isEqualTo(newEffectiveDate);

        String memoFromDb = jdbcClient
            .sql("SELECT memo FROM fixed_expense_history WHERE id = ?")
            .param(historyId.toString())
            .query(String.class)
            .single();
        assertThat(memoFromDb).isEqualTo(newMemo.get().value());
    }

    @Test
    void testFindByFixedExpenseCategoryIdAndYearAndMonth() {
        // Given
        FixedExpenseCategoryId expectedCategoryId = new FixedExpenseCategoryId(CATEGORY_UUID_1);
        Year expectedYear = new Year(2026);
        Month expectedMonth = new Month(2);

        // 別カテゴリの履歴を追加（検索対象外）
        LocalDateTime now = LocalDateTime.now();
        jdbcClient
            .sql(
                """
                INSERT INTO fixed_expense_history (id, fixed_expense_category_id, year, month, amount, effective_date, memo, created_at, updated_at, version)
                VALUES (:id, :fixed_expense_category_id, :year, :month, :amount, :effective_date, :memo, :created_at, :updated_at, 0)
                """
            )
            .param("id", UUID.randomUUID().toString())
            .param("fixed_expense_category_id", CATEGORY_UUID_2.toString())
            .param("year", 2026)
            .param("month", 2)
            .param("amount", 15_000)
            .param("effective_date", LocalDate.of(2026, 2, 1))
            .param("memo", "光熱費")
            .param("created_at", now)
            .param("updated_at", now)
            .update();

        // 同カテゴリ別月の履歴を追加（検索対象外）
        jdbcClient
            .sql(
                """
                INSERT INTO fixed_expense_history (id, fixed_expense_category_id, year, month, amount, effective_date, memo, created_at, updated_at, version)
                VALUES (:id, :fixed_expense_category_id, :year, :month, :amount, :effective_date, :memo, :created_at, :updated_at, 0)
                """
            )
            .param("id", UUID.randomUUID().toString())
            .param("fixed_expense_category_id", CATEGORY_UUID_1.toString())
            .param("year", 2026)
            .param("month", 3)
            .param("amount", 80_000)
            .param("effective_date", LocalDate.of(2026, 3, 1))
            .param("memo", "3月の家賃")
            .param("created_at", now)
            .param("updated_at", now)
            .update();

        // When
        Optional<FixedExpenseHistory> actual =
            fixedExpenseHistoryRepository.findByFixedExpenseCategoryIdAndYearAndMonth(
                expectedCategoryId,
                expectedYear,
                expectedMonth
            );

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().fixedExpenseCategoryId()).isEqualTo(expectedCategoryId);
        assertThat(actual.get().year()).isEqualTo(expectedYear);
        assertThat(actual.get().month()).isEqualTo(expectedMonth);
        assertThat(actual.get().amount()).isEqualTo(new Money(80_000));
    }
}
