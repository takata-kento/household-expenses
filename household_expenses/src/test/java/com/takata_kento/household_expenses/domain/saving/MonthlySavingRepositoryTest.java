package com.takata_kento.household_expenses.domain.saving;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.MonthlySavingId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
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
class MonthlySavingRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private MonthlySavingRepository monthlySavingRepository;

    @Autowired
    private JdbcClient jdbcClient;

    private final UUID USER_UUID_1 = UUID.randomUUID();
    private final UUID USER_UUID_2 = UUID.randomUUID();
    private final UUID FINANCIAL_ACCOUNT_UUID = UUID.randomUUID();
    private final UUID SAVING_UUID_1 = UUID.randomUUID();
    private final UUID SAVING_UUID_2 = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // ユーザーを挿入
        jdbcClient
            .sql(
                """
                INSERT INTO
                    "users" (id, username, password_hash, enabled, version)
                VALUES
                    (:id, :username, :password_hash, :enabled, :version)
                """
            )
            .param("id", USER_UUID_1.toString())
            .param("username", "testuser1")
            .param("password_hash", "hashedpassword1")
            .param("enabled", true)
            .param("version", 0)
            .update();

        jdbcClient
            .sql(
                """
                INSERT INTO
                    "users" (id, username, password_hash, enabled, version)
                VALUES
                    (:id, :username, :password_hash, :enabled, :version)
                """
            )
            .param("id", USER_UUID_2.toString())
            .param("username", "testuser2")
            .param("password_hash", "hashedpassword2")
            .param("enabled", true)
            .param("version", 0)
            .update();

        // 金融口座を挿入
        jdbcClient
            .sql(
                """
                INSERT INTO
                    financial_account (id, user_id, account_name, balance, is_main_account, version)
                VALUES
                    (:id, :user_id, :account_name, :balance, :is_main_account, :version)
                """
            )
            .param("id", FINANCIAL_ACCOUNT_UUID.toString())
            .param("user_id", USER_UUID_1.toString())
            .param("account_name", "テスト口座")
            .param("balance", 100000)
            .param("is_main_account", true)
            .param("version", 0)
            .update();

        // 月次貯金を挿入（2件: 2024/6, 2024/7 for USER_UUID_1）
        jdbcClient
            .sql(
                """
                INSERT INTO
                    monthly_saving (id, user_id, year, month, saving_amount, financial_account_id, memo, version)
                VALUES
                    (:id, :user_id, :year, :month, :saving_amount, :financial_account_id, :memo, :version)
                """
            )
            .param("id", SAVING_UUID_1.toString())
            .param("user_id", USER_UUID_1.toString())
            .param("year", 2024)
            .param("month", 6)
            .param("saving_amount", 50000)
            .param("financial_account_id", FINANCIAL_ACCOUNT_UUID.toString())
            .param("memo", "6月の貯金")
            .param("version", 0)
            .update();

        jdbcClient
            .sql(
                """
                INSERT INTO
                    monthly_saving (id, user_id, year, month, saving_amount, financial_account_id, memo, version)
                VALUES
                    (:id, :user_id, :year, :month, :saving_amount, :financial_account_id, :memo, :version)
                """
            )
            .param("id", SAVING_UUID_2.toString())
            .param("user_id", USER_UUID_1.toString())
            .param("year", 2024)
            .param("month", 7)
            .param("saving_amount", 60000)
            .param("financial_account_id", FINANCIAL_ACCOUNT_UUID.toString())
            .param("memo", (String) null)
            .param("version", 0)
            .update();
    }

    @Test
    void testFindByUserIdAndYearAndMonth() {
        // Given
        UserId userId = new UserId(USER_UUID_1);
        Year year = new Year(2024);
        Month month = new Month(6);
        Money expectedSavingAmount = new Money(50_000);
        Optional<Description> expectedMemo = Optional.of(new Description("6月の貯金"));

        // When
        Optional<MonthlySaving> actual = monthlySavingRepository.findByUserIdAndYearAndMonth(userId, year, month);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().userId()).isEqualTo(userId);
        assertThat(actual.get().year()).isEqualTo(year);
        assertThat(actual.get().month()).isEqualTo(month);
        assertThat(actual.get().savingAmount()).isEqualTo(expectedSavingAmount);
        assertThat(actual.get().memo()).isEqualTo(expectedMemo);
    }

    @Test
    void testFindByUserIdAndYear() {
        // Given
        UserId userId = new UserId(USER_UUID_1);
        Year year = new Year(2024);

        // When
        List<MonthlySaving> actual = monthlySavingRepository.findByUserIdAndYear(userId, year);

        // Then
        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(MonthlySaving::userId).containsOnly(userId);
    }

    @Test
    void testSave() {
        // Given
        UserId expectedUserId = new UserId(USER_UUID_1);
        Year expectedYear = new Year(2024);
        Month expectedMonth = new Month(8);
        Money expectedSavingAmount = new Money(70_000);
        FinancialAccountId expectedFinancialAccountId = new FinancialAccountId(FINANCIAL_ACCOUNT_UUID);
        Optional<Description> expectedMemo = Optional.of(new Description("8月の貯金"));

        MonthlySaving newSaving = MonthlySaving.create(
            expectedUserId,
            expectedYear,
            expectedMonth,
            expectedSavingAmount,
            expectedFinancialAccountId,
            expectedMemo
        );

        // When
        MonthlySaving savedSaving = monthlySavingRepository.save(newSaving);

        // Then
        String userIdFromDb = jdbcClient
            .sql("SELECT user_id FROM monthly_saving WHERE id = ?")
            .param(savedSaving.id().toString())
            .query(String.class)
            .single();
        assertThat(userIdFromDb).isEqualTo(USER_UUID_1.toString());

        Integer yearFromDb = jdbcClient
            .sql("SELECT year FROM monthly_saving WHERE id = ?")
            .param(savedSaving.id().toString())
            .query(Integer.class)
            .single();
        assertThat(yearFromDb).isEqualTo(2024);

        Integer monthFromDb = jdbcClient
            .sql("SELECT month FROM monthly_saving WHERE id = ?")
            .param(savedSaving.id().toString())
            .query(Integer.class)
            .single();
        assertThat(monthFromDb).isEqualTo(8);

        Integer savingAmountFromDb = jdbcClient
            .sql("SELECT saving_amount FROM monthly_saving WHERE id = ?")
            .param(savedSaving.id().toString())
            .query(Integer.class)
            .single();
        assertThat(savingAmountFromDb).isEqualTo(expectedSavingAmount.amount());

        String financialAccountIdFromDb = jdbcClient
            .sql("SELECT financial_account_id FROM monthly_saving WHERE id = ?")
            .param(savedSaving.id().toString())
            .query(String.class)
            .single();
        assertThat(financialAccountIdFromDb).isEqualTo(FINANCIAL_ACCOUNT_UUID.toString());

        String memoFromDb = jdbcClient
            .sql("SELECT memo FROM monthly_saving WHERE id = ?")
            .param(savedSaving.id().toString())
            .query(String.class)
            .single();
        assertThat(memoFromDb).isEqualTo(expectedMemo.get().value());
    }

    @Test
    void testUpdate() {
        // Given
        MonthlySavingId savingId = new MonthlySavingId(SAVING_UUID_1);
        Money newSavingAmount = new Money(55_000);
        Optional<Description> newMemo = Optional.of(new Description("更新後のメモ"));

        // When
        MonthlySaving saving = monthlySavingRepository.findById(savingId).orElseThrow();
        saving.updateSavingAmount(newSavingAmount);
        saving.updateMemo(newMemo);
        monthlySavingRepository.save(saving);

        // Then
        Integer savingAmountFromDb = jdbcClient
            .sql("SELECT saving_amount FROM monthly_saving WHERE id = ?")
            .param(savingId.toString())
            .query(Integer.class)
            .single();
        assertThat(savingAmountFromDb).isEqualTo(newSavingAmount.amount());

        String memoFromDb = jdbcClient
            .sql("SELECT memo FROM monthly_saving WHERE id = ?")
            .param(savingId.toString())
            .query(String.class)
            .single();
        assertThat(memoFromDb).isEqualTo(newMemo.get().value());

        Integer versionFromDb = jdbcClient
            .sql("SELECT version FROM monthly_saving WHERE id = ?")
            .param(savingId.toString())
            .query(Integer.class)
            .single();
        assertThat(versionFromDb).isEqualTo(1);
    }
}
