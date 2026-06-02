package com.takata_kento.household_expenses.application.account;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.Mockito.*;

import com.takata_kento.household_expenses.domain.account.FinancialAccount;
import com.takata_kento.household_expenses.domain.account.FinancialAccountRepository;
import com.takata_kento.household_expenses.domain.valueobject.AccountName;
import com.takata_kento.household_expenses.domain.valueobject.BankName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private FinancialAccountRepository financialAccountRepository;

    @InjectMocks
    private AccountService accountService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final UserId OTHER_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

    private FinancialAccount buildAccount(
        FinancialAccountId id,
        UserId userId,
        Optional<AccountName> accountName,
        Money balance,
        Boolean isMainAccount
    ) {
        return new FinancialAccount(
            id,
            userId,
            new BankName("三菱UFJ銀行"),
            accountName,
            balance,
            isMainAccount,
            new HashSet<>(),
            null
        );
    }

    @Test
    void testCreateAccount() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("1234567");
        BankName bankName = new BankName("三菱UFJ銀行");
        AccountName accountName = new AccountName("メイン口座");
        Money initialBalance = new Money(100_000);
        Boolean isMainAccount = Boolean.TRUE;
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.empty());
        when(financialAccountRepository.findByUserId(CURRENT_USER_ID)).thenReturn(List.of());
        when(financialAccountRepository.save(any(FinancialAccount.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        FinancialAccount actual = accountService.createAccount(
            CURRENT_USER_ID,
            accountId,
            bankName,
            Optional.of(accountName),
            initialBalance,
            isMainAccount
        );

        // Then
        then(actual).isNotNull();
        then(actual.id()).isEqualTo(accountId);
        then(actual.userId()).isEqualTo(CURRENT_USER_ID);
        then(actual.bankName()).isEqualTo(bankName);
        then(actual.accountName()).isEqualTo(Optional.of(accountName));
        then(actual.balance()).isEqualTo(initialBalance);
        then(actual.isMainAccount()).isEqualTo(isMainAccount);
        verify(financialAccountRepository).save(any(FinancialAccount.class));
    }

    @Test
    void testCreateAccountWithoutAccountName() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("1234567");
        BankName bankName = new BankName("ゆうちょ銀行");
        Money initialBalance = new Money(100_000);
        Boolean isMainAccount = Boolean.FALSE;
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.empty());
        when(financialAccountRepository.save(any(FinancialAccount.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        FinancialAccount actual = accountService.createAccount(
            CURRENT_USER_ID,
            accountId,
            bankName,
            Optional.empty(),
            initialBalance,
            isMainAccount
        );

        // Then
        then(actual).isNotNull();
        then(actual.id()).isEqualTo(accountId);
        then(actual.bankName()).isEqualTo(bankName);
        then(actual.accountName()).isEmpty();
        verify(financialAccountRepository).save(any(FinancialAccount.class));
    }

    @Test
    void testCreateAccountWhenSameNumberAlreadyOwnedBySelf() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("1234567");
        BankName bankName = new BankName("三菱UFJ銀行");
        AccountName accountName = new AccountName("メイン口座");
        Money initialBalance = new Money(100_000);
        Boolean isMainAccount = Boolean.FALSE;
        FinancialAccount existingOwn = buildAccount(
            accountId,
            CURRENT_USER_ID,
            Optional.of(new AccountName("既存口座")),
            new Money(50_000),
            Boolean.FALSE
        );
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.of(existingOwn));

        // When / Then
        thenThrownBy(() ->
            accountService.createAccount(
                CURRENT_USER_ID,
                accountId,
                bankName,
                Optional.of(accountName),
                initialBalance,
                isMainAccount
            )
        ).isInstanceOf(IllegalStateException.class);
        verify(financialAccountRepository, never()).save(any());
    }

    @Test
    void testCreateAccountWhenSameNumberOwnedByOtherUser() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("1234567");
        BankName bankName = new BankName("三菱UFJ銀行");
        AccountName accountName = new AccountName("メイン口座");
        Money initialBalance = new Money(100_000);
        Boolean isMainAccount = Boolean.FALSE;
        FinancialAccount existingOther = buildAccount(
            accountId,
            OTHER_USER_ID,
            Optional.of(new AccountName("他人の口座")),
            new Money(50_000),
            Boolean.FALSE
        );
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.of(existingOther));

        // When / Then
        thenThrownBy(() ->
            accountService.createAccount(
                CURRENT_USER_ID,
                accountId,
                bankName,
                Optional.of(accountName),
                initialBalance,
                isMainAccount
            )
        ).isInstanceOf(IllegalStateException.class);
        verify(financialAccountRepository, never()).save(any());
    }

    @Test
    void testCreateAccountAsMainWhenAlreadyHasMain() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("2345678");
        BankName bankName = new BankName("三菱UFJ銀行");
        AccountName accountName = new AccountName("もう一つのメイン口座");
        Money initialBalance = new Money(50_000);
        Boolean isMainAccount = Boolean.TRUE;
        FinancialAccount existingMain = buildAccount(
            new FinancialAccountId("1111111"),
            CURRENT_USER_ID,
            Optional.of(new AccountName("既存メイン")),
            new Money(100_000),
            Boolean.TRUE
        );
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.empty());
        when(financialAccountRepository.findByUserId(CURRENT_USER_ID)).thenReturn(List.of(existingMain));

        // When / Then
        thenThrownBy(() ->
            accountService.createAccount(
                CURRENT_USER_ID,
                accountId,
                bankName,
                Optional.of(accountName),
                initialBalance,
                isMainAccount
            )
        ).isInstanceOf(IllegalStateException.class);
        verify(financialAccountRepository, never()).save(any());
    }

    @Test
    void testCreateAccountAsNonMain() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("3456789");
        BankName bankName = new BankName("ゆうちょ銀行");
        AccountName accountName = new AccountName("貯金口座");
        Money initialBalance = new Money(500_000);
        Boolean isMainAccount = Boolean.FALSE;
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.empty());
        when(financialAccountRepository.save(any(FinancialAccount.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        FinancialAccount actual = accountService.createAccount(
            CURRENT_USER_ID,
            accountId,
            bankName,
            Optional.of(accountName),
            initialBalance,
            isMainAccount
        );

        // Then
        then(actual).isNotNull();
        then(actual.id()).isEqualTo(accountId);
        then(actual.isMainAccount()).isFalse();
        verify(financialAccountRepository).save(any(FinancialAccount.class));
        verify(financialAccountRepository, never()).findByUserId(any());
    }

    @Test
    void testUpdateAccountName() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("1234567");
        AccountName newAccountName = new AccountName("更新後口座名");
        FinancialAccount account = buildAccount(
            accountId,
            CURRENT_USER_ID,
            Optional.of(new AccountName("元口座名")),
            new Money(100_000),
            Boolean.TRUE
        );
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialAccountRepository.save(account)).thenReturn(account);

        // When
        FinancialAccount actual = accountService.updateAccountName(CURRENT_USER_ID, accountId, newAccountName);

        // Then
        then(actual.accountName()).isEqualTo(Optional.of(newAccountName));
        verify(financialAccountRepository).save(account);
    }

    @Test
    void testUpdateAccountNameWhenNotOwner() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("1234567");
        AccountName newAccountName = new AccountName("更新後口座名");
        FinancialAccount account = buildAccount(
            accountId,
            OTHER_USER_ID,
            Optional.of(new AccountName("他人の口座名")),
            new Money(100_000),
            Boolean.TRUE
        );
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // When / Then
        thenThrownBy(() -> accountService.updateAccountName(CURRENT_USER_ID, accountId, newAccountName)).isInstanceOf(
            IllegalStateException.class
        );
        verify(financialAccountRepository, never()).save(any());
    }

    @Test
    void testUpdateAccountNameWhenAccountNotFound() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("9999999");
        AccountName newAccountName = new AccountName("更新後口座名");
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.empty());

        // When / Then
        thenThrownBy(() -> accountService.updateAccountName(CURRENT_USER_ID, accountId, newAccountName)).isInstanceOf(
            IllegalStateException.class
        );
        verify(financialAccountRepository, never()).save(any());
    }

    @Test
    void testUpdateBalanceWithoutDescription() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("4444444");
        Money newBalance = new Money(200_000);
        FinancialAccount account = buildAccount(
            accountId,
            CURRENT_USER_ID,
            Optional.of(new AccountName("口座")),
            new Money(100_000),
            Boolean.TRUE
        );
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialAccountRepository.save(account)).thenReturn(account);

        // When
        FinancialAccount actual = accountService.updateBalance(CURRENT_USER_ID, accountId, newBalance);

        // Then
        then(actual.balance()).isEqualTo(newBalance);
        then(actual.editHistories()).hasSize(1);
        then(actual.editHistories().iterator().next().editReason()).isEqualTo(Optional.empty());
        verify(financialAccountRepository).save(account);
    }

    @Test
    void testUpdateBalanceWithDescription() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("5555555");
        Money newBalance = new Money(200_000);
        Description reason = new Description("Balance correction");
        FinancialAccount account = buildAccount(
            accountId,
            CURRENT_USER_ID,
            Optional.of(new AccountName("口座")),
            new Money(100_000),
            Boolean.TRUE
        );
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialAccountRepository.save(account)).thenReturn(account);

        // When
        FinancialAccount actual = accountService.updateBalance(CURRENT_USER_ID, accountId, newBalance, reason);

        // Then
        then(actual.balance()).isEqualTo(newBalance);
        then(actual.editHistories()).hasSize(1);
        then(actual.editHistories().iterator().next().editReason()).isEqualTo(Optional.of(reason));
        verify(financialAccountRepository).save(account);
    }

    @Test
    void testUpdateBalanceWhenNotOwner() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("6666666");
        Money newBalance = new Money(200_000);
        FinancialAccount account = buildAccount(
            accountId,
            OTHER_USER_ID,
            Optional.of(new AccountName("他人の口座")),
            new Money(100_000),
            Boolean.TRUE
        );
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // When / Then
        thenThrownBy(() -> accountService.updateBalance(CURRENT_USER_ID, accountId, newBalance)).isInstanceOf(
            IllegalStateException.class
        );
        verify(financialAccountRepository, never()).save(any());
    }

    @Test
    void testUpdateBalanceWithDescriptionWhenNotOwner() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("7777777");
        Money newBalance = new Money(200_000);
        Description reason = new Description("Balance correction");
        FinancialAccount account = buildAccount(
            accountId,
            OTHER_USER_ID,
            Optional.of(new AccountName("他人の口座")),
            new Money(100_000),
            Boolean.TRUE
        );
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // When / Then
        thenThrownBy(() -> accountService.updateBalance(CURRENT_USER_ID, accountId, newBalance, reason)).isInstanceOf(
            IllegalStateException.class
        );
        verify(financialAccountRepository, never()).save(any());
    }

    @Test
    void testUpdateBalanceWhenAccountNotFound() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("8888888");
        Money newBalance = new Money(200_000);
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.empty());

        // When / Then
        thenThrownBy(() -> accountService.updateBalance(CURRENT_USER_ID, accountId, newBalance)).isInstanceOf(
            IllegalStateException.class
        );
        verify(financialAccountRepository, never()).save(any());
    }

    @Test
    void testCalculateNewBalance() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("9999999");
        Money currentBalance = new Money(100_000);
        Money income = new Money(300_000);
        Money totalExpense = new Money(50_000);
        Money fixedExpense = new Money(80_000);
        Money saving = new Money(20_000);
        Money expected = new Money(250_000);
        FinancialAccount account = buildAccount(
            accountId,
            CURRENT_USER_ID,
            Optional.of(new AccountName("口座")),
            currentBalance,
            Boolean.TRUE
        );
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // When
        Money actual = accountService.calculateNewBalance(
            CURRENT_USER_ID,
            accountId,
            income,
            totalExpense,
            fixedExpense,
            saving
        );

        // Then
        then(actual).isEqualTo(expected);
        verify(financialAccountRepository, never()).save(any());
    }

    @Test
    void testCalculateNewBalanceWhenNotOwner() {
        // Given
        FinancialAccountId accountId = new FinancialAccountId("1010101");
        FinancialAccount account = buildAccount(
            accountId,
            OTHER_USER_ID,
            Optional.of(new AccountName("他人の口座")),
            new Money(100_000),
            Boolean.TRUE
        );
        when(financialAccountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // When / Then
        thenThrownBy(() ->
            accountService.calculateNewBalance(
                CURRENT_USER_ID,
                accountId,
                new Money(300_000),
                new Money(50_000),
                new Money(80_000),
                new Money(20_000)
            )
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void testGetUserAccounts() {
        // Given
        FinancialAccount account1 = buildAccount(
            new FinancialAccountId("2020202"),
            CURRENT_USER_ID,
            Optional.of(new AccountName("メイン口座")),
            new Money(100_000),
            Boolean.TRUE
        );
        FinancialAccount account2 = buildAccount(
            new FinancialAccountId("3030303"),
            CURRENT_USER_ID,
            Optional.of(new AccountName("貯金口座")),
            new Money(500_000),
            Boolean.FALSE
        );
        List<FinancialAccount> accounts = List.of(account1, account2);
        when(financialAccountRepository.findByUserId(CURRENT_USER_ID)).thenReturn(accounts);

        // When
        List<FinancialAccount> actual = accountService.getUserAccounts(CURRENT_USER_ID);

        // Then
        then(actual).hasSize(2);
        then(actual).containsExactlyInAnyOrderElementsOf(accounts);
        verify(financialAccountRepository).findByUserId(CURRENT_USER_ID);
    }
}
