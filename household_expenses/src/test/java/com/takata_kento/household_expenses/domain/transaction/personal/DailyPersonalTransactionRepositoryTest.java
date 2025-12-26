package com.takata_kento.household_expenses.domain.transaction.personal;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.ArrayList;
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
class DailyPersonalTransactionRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private DailyPersonalTransactionRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        // テストユーザーを挿入
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, enabled) VALUES (:id, :username, :password, :enabled)"
            )
            .param("id", 1L)
            .param("username", "testuser")
            .param("password", "hashedpassword")
            .param("enabled", true)
            .update();
    }

    @Test
    void testSave() {
        // Given
        UserId userId = new UserId(1L);
        DailyPersonalTransactionId transactionId = new DailyPersonalTransactionId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);
        Money income = new Money(10_000);

        DailyPersonalTransaction transaction = new DailyPersonalTransaction(
            transactionId,
            userId,
            transactionDate,
            income,
            new ArrayList<>(),
            null
        );

        // When
        DailyPersonalTransaction savedTransaction = repository.save(transaction);

        // Then
        // DBから直接確認
        String idFromDb = jdbcClient
            .sql("SELECT id FROM daily_personal_transaction WHERE id = ?")
            .param(savedTransaction.id().toString())
            .query(String.class)
            .single();
        then(idFromDb).isEqualTo(transactionId.toString());

        Long userIdFromDb = jdbcClient
            .sql("SELECT user_id FROM daily_personal_transaction WHERE id = ?")
            .param(savedTransaction.id().toString())
            .query(Long.class)
            .single();
        then(userIdFromDb).isEqualTo(userId.value());

        LocalDate transactionDateFromDb = jdbcClient
            .sql("SELECT transaction_date FROM daily_personal_transaction WHERE id = ?")
            .param(savedTransaction.id().toString())
            .query(LocalDate.class)
            .single();
        then(transactionDateFromDb).isEqualTo(transactionDate);

        Integer incomeFromDb = jdbcClient
            .sql("SELECT income FROM daily_personal_transaction WHERE id = ?")
            .param(savedTransaction.id().toString())
            .query(Integer.class)
            .single();
        then(incomeFromDb).isEqualTo(income.amount());
    }

    @Test
    void testSaveWithPersonalExpenses() {
        // Given
        UserId userId = new UserId(1L);
        DailyPersonalTransactionId transactionId = new DailyPersonalTransactionId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);
        Money income = new Money(10_000);

        DailyPersonalTransaction transaction = new DailyPersonalTransaction(
            transactionId,
            userId,
            transactionDate,
            income,
            new ArrayList<>(),
            null
        );
        transaction.addPersonalExpense(new Money(1_000), new Description("expense 1"));
        transaction.addPersonalExpense(new Money(2_000), new Description("expense 2"));

        // When
        DailyPersonalTransaction savedTransaction = repository.save(transaction);

        // Then
        // 個人支出の数を確認
        Long expenseCountFromDb = jdbcClient
            .sql("SELECT COUNT(*) FROM daily_personal_expense WHERE daily_personal_transaction_id = ?")
            .param(savedTransaction.id().toString())
            .query(Long.class)
            .single();
        then(expenseCountFromDb).isEqualTo(2L);

        // 個人支出の合計金額を確認
        Integer totalAmountFromDb = jdbcClient
            .sql("SELECT SUM(amount) FROM daily_personal_expense WHERE daily_personal_transaction_id = ?")
            .param(savedTransaction.id().toString())
            .query(Integer.class)
            .single();
        then(totalAmountFromDb).isEqualTo(3_000);
    }

    @Test
    void testFindById() {
        // Given
        UserId userId = new UserId(1L);
        DailyPersonalTransactionId transactionId = new DailyPersonalTransactionId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);
        Money income = new Money(10_000);

        insertDailyPersonalTransaction(transactionId, userId, transactionDate, income);

        // When
        Optional<DailyPersonalTransaction> actual = repository.findById(transactionId);

        // Then
        then(actual).isPresent();
        then(actual.get().id()).isEqualTo(transactionId);
        then(actual.get().userId()).isEqualTo(userId);
        then(actual.get().transactionDate()).isEqualTo(transactionDate);
        then(actual.get().income()).isEqualTo(income);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        DailyPersonalTransactionId transactionId = new DailyPersonalTransactionId(UUID.randomUUID());

        // When
        Optional<DailyPersonalTransaction> actual = repository.findById(transactionId);

        // Then
        then(actual).isEmpty();
    }

    @Test
    void testFindByUserIdAndTransactionDate() {
        // Given
        UserId userId = new UserId(1L);
        DailyPersonalTransactionId transactionId = new DailyPersonalTransactionId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);
        Money income = new Money(10_000);

        insertDailyPersonalTransaction(transactionId, userId, transactionDate, income);

        // When
        Optional<DailyPersonalTransaction> actual = repository.findByUserIdAndTransactionDate(userId, transactionDate);

        // Then
        then(actual).isPresent();
        then(actual.get().id()).isEqualTo(transactionId);
        then(actual.get().userId()).isEqualTo(userId);
        then(actual.get().transactionDate()).isEqualTo(transactionDate);
        then(actual.get().income()).isEqualTo(income);
    }

    @Test
    void testFindByUserIdAndTransactionDateNotFound() {
        // Given
        UserId userId = new UserId(1L);
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);

        // When
        Optional<DailyPersonalTransaction> actual = repository.findByUserIdAndTransactionDate(userId, transactionDate);

        // Then
        then(actual).isEmpty();
    }

    @Test
    void testFindByUserId() {
        // Given
        UserId userId = new UserId(1L);
        DailyPersonalTransactionId transactionId1 = new DailyPersonalTransactionId(UUID.randomUUID());
        DailyPersonalTransactionId transactionId2 = new DailyPersonalTransactionId(UUID.randomUUID());

        insertDailyPersonalTransaction(transactionId1, userId, LocalDate.of(2025, 12, 26), new Money(10_000));
        insertDailyPersonalTransaction(transactionId2, userId, LocalDate.of(2025, 12, 27), new Money(20_000));

        // When
        List<DailyPersonalTransaction> actual = repository.findByUserId(userId);

        // Then
        then(actual).hasSize(2);
        then(actual).extracting(DailyPersonalTransaction::id).containsExactlyInAnyOrder(transactionId1, transactionId2);
    }

    @Test
    void testFindByUserIdEmpty() {
        // Given
        UserId userId = new UserId(999L);

        // When
        List<DailyPersonalTransaction> actual = repository.findByUserId(userId);

        // Then
        then(actual).isEmpty();
    }

    @Test
    void testExistsByUserIdAndTransactionDate() {
        // Given
        UserId userId = new UserId(1L);
        DailyPersonalTransactionId transactionId = new DailyPersonalTransactionId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);

        insertDailyPersonalTransaction(transactionId, userId, transactionDate, new Money(10_000));

        // When
        boolean actual = repository.existsByUserIdAndTransactionDate(userId, transactionDate);

        // Then
        then(actual).isTrue();
    }

    @Test
    void testExistsByUserIdAndTransactionDateNotFound() {
        // Given
        UserId userId = new UserId(1L);
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);

        // When
        boolean actual = repository.existsByUserIdAndTransactionDate(userId, transactionDate);

        // Then
        then(actual).isFalse();
    }

    @Test
    void testDelete() {
        // Given
        UserId userId = new UserId(1L);
        DailyPersonalTransactionId transactionId = new DailyPersonalTransactionId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);

        insertDailyPersonalTransaction(transactionId, userId, transactionDate, new Money(10_000));

        // When
        repository.deleteById(transactionId);

        // Then
        Optional<DailyPersonalTransaction> actual = repository.findById(transactionId);
        then(actual).isEmpty();
    }

    // Helper methods
    private void insertDailyPersonalTransaction(
        DailyPersonalTransactionId transactionId,
        UserId userId,
        LocalDate transactionDate,
        Money income
    ) {
        jdbcClient
            .sql(
                "INSERT INTO daily_personal_transaction (id, user_id, transaction_date, income) VALUES (:id, :userId, :transactionDate, :income)"
            )
            .param("id", transactionId.toString())
            .param("userId", userId.value())
            .param("transactionDate", transactionDate)
            .param("income", income.amount())
            .update();
    }
}
