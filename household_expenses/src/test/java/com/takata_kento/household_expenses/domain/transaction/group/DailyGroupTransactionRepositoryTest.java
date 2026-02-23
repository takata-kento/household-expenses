package com.takata_kento.household_expenses.domain.transaction.group;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.DailyGroupTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.HashSet;
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
class DailyGroupTransactionRepositoryTest {

    private static final UUID TEST_USER_GROUP_UUID = UUID.randomUUID();
    private static final UUID TEST_USER_UUID = UUID.randomUUID();
    private static final UUID TEST_CATEGORY_UUID = UUID.randomUUID();

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private DailyGroupTransactionRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        // テストユーザーグループを挿入
        jdbcClient
            .sql("INSERT INTO user_group (id, group_name, month_start_day) VALUES (:id, :groupName, :monthStartDay)")
            .param("id", TEST_USER_GROUP_UUID.toString())
            .param("groupName", "testgroup")
            .param("monthStartDay", 1)
            .update();

        // テストユーザーを挿入
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, enabled) VALUES (:id, :username, :password, :enabled)"
            )
            .param("id", TEST_USER_UUID.toString())
            .param("username", "testuser")
            .param("password", "hashedpassword")
            .param("enabled", true)
            .update();

        // 生活費カテゴリを挿入
        jdbcClient
            .sql(
                "INSERT INTO living_expense_category (id, user_group_id, category_name, is_default) VALUES (:id, :userGroupId, :categoryName, :isDefault)"
            )
            .param("id", TEST_CATEGORY_UUID.toString())
            .param("userGroupId", TEST_USER_GROUP_UUID.toString())
            .param("categoryName", "食費")
            .param("isDefault", false)
            .update();
    }

    @Test
    void testSave() {
        // Given
        UserGroupId userGroupId = new UserGroupId(TEST_USER_GROUP_UUID);
        DailyGroupTransactionId transactionId = new DailyGroupTransactionId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);

        DailyGroupTransaction transaction = new DailyGroupTransaction(
            transactionId,
            userGroupId,
            transactionDate,
            new HashSet<>(),
            null
        );

        // When
        DailyGroupTransaction savedTransaction = repository.save(transaction);

        // Then
        String idFromDb = jdbcClient
            .sql("SELECT id FROM daily_group_transaction WHERE id = ?")
            .param(savedTransaction.id().toString())
            .query(String.class)
            .single();
        then(idFromDb).isEqualTo(transactionId.toString());

        String userGroupIdFromDb = jdbcClient
            .sql("SELECT user_group_id FROM daily_group_transaction WHERE id = ?")
            .param(savedTransaction.id().toString())
            .query(String.class)
            .single();
        then(userGroupIdFromDb).isEqualTo(userGroupId.toString());

        LocalDate transactionDateFromDb = jdbcClient
            .sql("SELECT transaction_date FROM daily_group_transaction WHERE id = ?")
            .param(savedTransaction.id().toString())
            .query(LocalDate.class)
            .single();
        then(transactionDateFromDb).isEqualTo(transactionDate);
    }

    @Test
    void testSaveWithLivingExpenses() {
        // Given
        UserGroupId userGroupId = new UserGroupId(TEST_USER_GROUP_UUID);
        UserId userId = new UserId(TEST_USER_UUID);
        LivingExpenseCategoryId categoryId = new LivingExpenseCategoryId(TEST_CATEGORY_UUID);
        DailyGroupTransactionId transactionId = new DailyGroupTransactionId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);

        DailyGroupTransaction transaction = new DailyGroupTransaction(
            transactionId,
            userGroupId,
            transactionDate,
            new HashSet<>(),
            null
        );
        transaction.addLivingExpense(userId, categoryId, new Money(1_000), new Description("朝食"));
        transaction.addLivingExpense(userId, categoryId, new Money(2_000), new Description("夕食"));

        // When
        DailyGroupTransaction savedTransaction = repository.save(transaction);

        // Then
        Long expenseCountFromDb = jdbcClient
            .sql("SELECT COUNT(*) FROM daily_living_expense WHERE daily_group_transaction_id = ?")
            .param(savedTransaction.id().toString())
            .query(Long.class)
            .single();
        then(expenseCountFromDb).isEqualTo(2L);

        Integer totalAmountFromDb = jdbcClient
            .sql("SELECT SUM(amount) FROM daily_living_expense WHERE daily_group_transaction_id = ?")
            .param(savedTransaction.id().toString())
            .query(Integer.class)
            .single();
        then(totalAmountFromDb).isEqualTo(3_000);
    }

    @Test
    void testFindById() {
        // Given
        UserGroupId userGroupId = new UserGroupId(TEST_USER_GROUP_UUID);
        DailyGroupTransactionId transactionId = new DailyGroupTransactionId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);

        insertDailyGroupTransaction(transactionId, userGroupId, transactionDate);

        // When
        Optional<DailyGroupTransaction> actual = repository.findById(transactionId);

        // Then
        then(actual).isPresent();
        then(actual.get().id()).isEqualTo(transactionId);
        then(actual.get().userGroupId()).isEqualTo(userGroupId);
        then(actual.get().transactionDate()).isEqualTo(transactionDate);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        DailyGroupTransactionId transactionId = new DailyGroupTransactionId(UUID.randomUUID());

        // When
        Optional<DailyGroupTransaction> actual = repository.findById(transactionId);

        // Then
        then(actual).isEmpty();
    }

    @Test
    void testFindByUserGroupIdAndTransactionDate() {
        // Given
        UserGroupId userGroupId = new UserGroupId(TEST_USER_GROUP_UUID);
        DailyGroupTransactionId transactionId = new DailyGroupTransactionId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);

        insertDailyGroupTransaction(transactionId, userGroupId, transactionDate);

        // When
        Optional<DailyGroupTransaction> actual = repository.findByUserGroupIdAndTransactionDate(
            userGroupId,
            transactionDate
        );

        // Then
        then(actual).isPresent();
        then(actual.get().id()).isEqualTo(transactionId);
        then(actual.get().userGroupId()).isEqualTo(userGroupId);
        then(actual.get().transactionDate()).isEqualTo(transactionDate);
    }

    @Test
    void testFindByUserGroupIdAndTransactionDateNotFound() {
        // Given
        UserGroupId userGroupId = new UserGroupId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);

        // When
        Optional<DailyGroupTransaction> actual = repository.findByUserGroupIdAndTransactionDate(
            userGroupId,
            transactionDate
        );

        // Then
        then(actual).isEmpty();
    }

    @Test
    void testFindByUserGroupId() {
        // Given
        UserGroupId userGroupId = new UserGroupId(TEST_USER_GROUP_UUID);
        DailyGroupTransactionId transactionId1 = new DailyGroupTransactionId(UUID.randomUUID());
        DailyGroupTransactionId transactionId2 = new DailyGroupTransactionId(UUID.randomUUID());

        insertDailyGroupTransaction(transactionId1, userGroupId, LocalDate.of(2025, 12, 26));
        insertDailyGroupTransaction(transactionId2, userGroupId, LocalDate.of(2025, 12, 27));

        // When
        List<DailyGroupTransaction> actual = repository.findByUserGroupId(userGroupId);

        // Then
        then(actual).hasSize(2);
        then(actual).extracting(DailyGroupTransaction::id).containsExactlyInAnyOrder(transactionId1, transactionId2);
    }

    @Test
    void testDelete() {
        // Given
        UserGroupId userGroupId = new UserGroupId(TEST_USER_GROUP_UUID);
        DailyGroupTransactionId transactionId = new DailyGroupTransactionId(UUID.randomUUID());
        LocalDate transactionDate = LocalDate.of(2025, 12, 26);

        insertDailyGroupTransaction(transactionId, userGroupId, transactionDate);

        // When
        repository.deleteById(transactionId);

        // Then
        Optional<DailyGroupTransaction> actual = repository.findById(transactionId);
        then(actual).isEmpty();
    }

    // Helper methods
    private void insertDailyGroupTransaction(
        DailyGroupTransactionId transactionId,
        UserGroupId userGroupId,
        LocalDate transactionDate
    ) {
        jdbcClient
            .sql(
                "INSERT INTO daily_group_transaction (id, user_group_id, transaction_date) VALUES (:id, :userGroupId, :transactionDate)"
            )
            .param("id", transactionId.toString())
            .param("userGroupId", userGroupId.toString())
            .param("transactionDate", transactionDate)
            .update();
    }
}
