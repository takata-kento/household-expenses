package com.takata_kento.household_expenses.domain.account;

import static org.assertj.core.api.BDDAssertions.*;

import com.takata_kento.household_expenses.domain.valueobject.AccountName;
import com.takata_kento.household_expenses.domain.valueobject.BalanceEditHistoryId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
class FinancialAccountRepositoryTest {

    private static final UUID TEST_USER_UUID = UUID.randomUUID();

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private FinancialAccountRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, enabled) VALUES (:id, :username, :password, :enabled)"
            )
            .param("id", TEST_USER_UUID.toString())
            .param("username", "testuser")
            .param("password", "hashedpassword")
            .param("enabled", true)
            .update();
    }

    @Test
    void testSave() {
        // Given
        UserId userId = new UserId(TEST_USER_UUID);
        FinancialAccountId accountId = new FinancialAccountId(UUID.randomUUID());
        AccountName accountName = new AccountName("メイン口座");
        Money balance = new Money(100_000);
        Boolean isMainAccount = Boolean.TRUE;

        FinancialAccount account = new FinancialAccount(
            accountId,
            userId,
            accountName,
            balance,
            isMainAccount,
            new HashSet<>(),
            null
        );

        // When
        FinancialAccount savedAccount = repository.save(account);

        // Then
        String idFromDb = jdbcClient
            .sql("SELECT id FROM financial_account WHERE id = ?")
            .param(savedAccount.id().toString())
            .query(String.class)
            .single();
        then(idFromDb).isEqualTo(accountId.toString());

        String userIdFromDb = jdbcClient
            .sql("SELECT user_id FROM financial_account WHERE id = ?")
            .param(savedAccount.id().toString())
            .query(String.class)
            .single();
        then(userIdFromDb).isEqualTo(userId.toString());

        String accountNameFromDb = jdbcClient
            .sql("SELECT account_name FROM financial_account WHERE id = ?")
            .param(savedAccount.id().toString())
            .query(String.class)
            .single();
        then(accountNameFromDb).isEqualTo(accountName.value());

        Integer balanceFromDb = jdbcClient
            .sql("SELECT balance FROM financial_account WHERE id = ?")
            .param(savedAccount.id().toString())
            .query(Integer.class)
            .single();
        then(balanceFromDb).isEqualTo(balance.amount());

        Boolean isMainAccountFromDb = jdbcClient
            .sql("SELECT is_main_account FROM financial_account WHERE id = ?")
            .param(savedAccount.id().toString())
            .query(Boolean.class)
            .single();
        then(isMainAccountFromDb).isEqualTo(isMainAccount);
    }

    @Test
    void testSaveWithEditHistories() {
        // Given
        UserId userId = new UserId(TEST_USER_UUID);
        FinancialAccountId accountId = new FinancialAccountId(UUID.randomUUID());
        AccountName accountName = new AccountName("貯金口座");
        Money balance = new Money(500_000);

        FinancialAccount account = new FinancialAccount(
            accountId,
            userId,
            accountName,
            balance,
            Boolean.FALSE,
            new HashSet<>(),
            null
        );
        account.updateBalance(new Money(500_000), new Description("Deposit"), LocalDate.of(2026, 1, 1));
        account.updateBalance(new Money(550_000), new Description("Interest"), LocalDate.of(2026, 2, 1));

        // When
        FinancialAccount savedAccount = repository.save(account);

        // Then
        List<Map<String, Object>> histories = jdbcClient
            .sql("SELECT * FROM balance_edit_history WHERE financial_account_id = ? ORDER BY edited_at")
            .param(savedAccount.id().toString())
            .query((rs, rowNum) -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", rs.getString("id"));
                map.put("financial_account_id", rs.getString("financial_account_id"));
                map.put("old_balance", rs.getInt("old_balance"));
                map.put("new_balance", rs.getInt("new_balance"));
                map.put("edit_reason", rs.getString("edit_reason"));
                map.put("edited_at", rs.getDate("edited_at").toLocalDate());
                return map;
            })
            .list();

        then(histories).hasSize(2);

        then(histories.get(0).get("id")).isNotNull();
        then(histories.get(0).get("financial_account_id")).isEqualTo(accountId.toString());
        then(histories.get(0).get("old_balance")).isEqualTo(500_000);
        then(histories.get(0).get("new_balance")).isEqualTo(500_000);
        then(histories.get(0).get("edit_reason")).isEqualTo("Deposit");
        then(histories.get(0).get("edited_at")).isEqualTo(LocalDate.of(2026, 1, 1));

        then(histories.get(1).get("id")).isNotNull();
        then(histories.get(1).get("financial_account_id")).isEqualTo(accountId.toString());
        then(histories.get(1).get("old_balance")).isEqualTo(500_000);
        then(histories.get(1).get("new_balance")).isEqualTo(550_000);
        then(histories.get(1).get("edit_reason")).isEqualTo("Interest");
        then(histories.get(1).get("edited_at")).isEqualTo(LocalDate.of(2026, 2, 1));
    }

    @Test
    void testFindById() {
        // Given
        UserId userId = new UserId(TEST_USER_UUID);
        FinancialAccountId accountId = new FinancialAccountId(UUID.randomUUID());
        AccountName accountName = new AccountName("メイン口座");
        Money balance = new Money(100_000);
        Boolean isMainAccount = Boolean.TRUE;

        insertFinancialAccount(accountId, userId, accountName, balance, isMainAccount);

        UUID historyId1 = UUID.randomUUID();
        UUID historyId2 = UUID.randomUUID();
        LocalDate editedAt1 = LocalDate.of(2026, 1, 1);
        LocalDate editedAt2 = LocalDate.of(2026, 2, 1);

        insertBalanceEditHistory(historyId1, accountId, 50_000, 100_000, "Deposit", editedAt1);
        insertBalanceEditHistory(historyId2, accountId, 100_000, 150_000, "Interest", editedAt2);

        // When
        Optional<FinancialAccount> actual = repository.findById(accountId);

        // Then
        then(actual).isPresent();
        then(actual.get().id()).isEqualTo(accountId);
        then(actual.get().userId()).isEqualTo(userId);
        then(actual.get().accountName()).isEqualTo(accountName);
        then(actual.get().balance()).isEqualTo(balance);
        then(actual.get().isMainAccount()).isEqualTo(isMainAccount);

        then(actual.get().editHistories()).hasSize(2);
        then(actual.get().editHistories())
            .anySatisfy(history -> {
                then(history.id()).isEqualTo(new BalanceEditHistoryId(historyId1));
                then(history.financialAccountId()).isEqualTo(accountId);
                then(history.oldBalance()).isEqualTo(new Money(50_000));
                then(history.newBalance()).isEqualTo(new Money(100_000));
                then(history.editReason()).isEqualTo(new Description("Deposit"));
                then(history.editedAt()).isEqualTo(editedAt1);
            });
        then(actual.get().editHistories())
            .anySatisfy(history -> {
                then(history.id()).isEqualTo(new BalanceEditHistoryId(historyId2));
                then(history.financialAccountId()).isEqualTo(accountId);
                then(history.oldBalance()).isEqualTo(new Money(100_000));
                then(history.newBalance()).isEqualTo(new Money(150_000));
                then(history.editReason()).isEqualTo(new Description("Interest"));
                then(history.editedAt()).isEqualTo(editedAt2);
            });
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId(UUID.randomUUID());

        // When
        Optional<FinancialAccount> actual = repository.findById(accountId);

        // Then
        then(actual).isEmpty();
    }

    @Test
    void testFindByUserId() {
        // Given
        UserId userId = new UserId(TEST_USER_UUID);
        FinancialAccountId accountId1 = new FinancialAccountId(UUID.randomUUID());
        FinancialAccountId accountId2 = new FinancialAccountId(UUID.randomUUID());

        insertFinancialAccount(accountId1, userId, new AccountName("メイン口座"), new Money(100_000), Boolean.TRUE);
        insertFinancialAccount(accountId2, userId, new AccountName("貯金口座"), new Money(500_000), Boolean.FALSE);

        // When
        List<FinancialAccount> actual = repository.findByUserId(userId);

        // Then
        then(actual).hasSize(2);
        then(actual).extracting(FinancialAccount::id).containsExactlyInAnyOrder(accountId1, accountId2);
    }

    @Test
    void testFindByUserIdEmpty() {
        // Given
        UserId userId = new UserId(UUID.randomUUID());

        // When
        List<FinancialAccount> actual = repository.findByUserId(userId);

        // Then
        then(actual).isEmpty();
    }

    @Test
    void testDelete() {
        // Given
        UserId userId = new UserId(TEST_USER_UUID);
        FinancialAccountId accountId = new FinancialAccountId(UUID.randomUUID());

        insertFinancialAccount(accountId, userId, new AccountName("メイン口座"), new Money(100_000), Boolean.TRUE);

        // When
        repository.deleteById(accountId);

        // Then
        Optional<FinancialAccount> actual = repository.findById(accountId);
        then(actual).isEmpty();
    }

    // Helper methods
    private void insertFinancialAccount(
        FinancialAccountId accountId,
        UserId userId,
        AccountName accountName,
        Money balance,
        Boolean isMainAccount
    ) {
        jdbcClient
            .sql(
                "INSERT INTO financial_account (id, user_id, account_name, balance, is_main_account) VALUES (:id, :userId, :accountName, :balance, :isMainAccount)"
            )
            .param("id", accountId.toString())
            .param("userId", userId.toString())
            .param("accountName", accountName.value())
            .param("balance", balance.amount())
            .param("isMainAccount", isMainAccount)
            .update();
    }

    private void insertBalanceEditHistory(
        UUID historyId,
        FinancialAccountId financialAccountId,
        int oldBalance,
        int newBalance,
        String editReason,
        LocalDate editedAt
    ) {
        jdbcClient
            .sql(
                "INSERT INTO balance_edit_history (id, financial_account_id, old_balance, new_balance, edit_reason, edited_at) VALUES (:id, :financialAccountId, :oldBalance, :newBalance, :editReason, :editedAt)"
            )
            .param("id", historyId.toString())
            .param("financialAccountId", financialAccountId.toString())
            .param("oldBalance", oldBalance)
            .param("newBalance", newBalance)
            .param("editReason", editReason)
            .param("editedAt", editedAt)
            .update();
    }
}
