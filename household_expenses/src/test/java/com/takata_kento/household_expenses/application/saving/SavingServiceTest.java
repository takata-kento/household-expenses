package com.takata_kento.household_expenses.application.saving;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.account.FinancialAccount;
import com.takata_kento.household_expenses.domain.account.FinancialAccountRepository;
import com.takata_kento.household_expenses.domain.saving.MonthlySaving;
import com.takata_kento.household_expenses.domain.saving.MonthlySavingRepository;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.user.UserRepository;
import com.takata_kento.household_expenses.domain.valueobject.AccountName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.MonthlySavingId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SavingServiceTest {

    @Mock
    private MonthlySavingRepository monthlySavingRepository;

    @Mock
    private FinancialAccountRepository financialAccountRepository;

    @Mock
    private UserRepository userRepository;

    @AutoClose
    private MockedStatic<CognitoUserContext> cognitoUserContext;

    @InjectMocks
    private SavingService savingService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final UserId OTHER_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
    private static final FinancialAccountId ACCOUNT_ID = new FinancialAccountId(
        UUID.fromString("00000000-0000-0000-0000-000000000010")
    );
    private static final FinancialAccountId OTHER_ACCOUNT_ID = new FinancialAccountId(
        UUID.fromString("00000000-0000-0000-0000-000000000011")
    );

    @BeforeEach
    void setUp() {
        cognitoUserContext = Mockito.mockStatic(CognitoUserContext.class);
    }

    private User currentUser() {
        return new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
    }

    private FinancialAccount ownedAccount() {
        return new FinancialAccount(
            ACCOUNT_ID,
            CURRENT_USER_ID,
            new AccountName("メイン口座"),
            new Money(0),
            false,
            null,
            null
        );
    }

    private FinancialAccount otherUserAccount() {
        return new FinancialAccount(
            OTHER_ACCOUNT_ID,
            OTHER_USER_ID,
            new AccountName("他人の口座"),
            new Money(0),
            false,
            null,
            null
        );
    }

    private MonthlySaving existingSaving(Year year, Month month) {
        return new MonthlySaving(
            new MonthlySavingId(UUID.fromString("00000000-0000-0000-0000-000000000100")),
            CURRENT_USER_ID,
            year,
            month,
            new Money(50_000),
            ACCOUNT_ID,
            Optional.of(new Description("貯金メモ")),
            0
        );
    }

    @Test
    void testRecordMonthlySaving() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        Money amount = new Money(30_000);
        Optional<Description> memo = Optional.of(new Description("5月の貯金"));
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.empty()
        );
        when(financialAccountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(ownedAccount()));
        when(monthlySavingRepository.save(any(MonthlySaving.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        MonthlySaving actual = savingService.recordMonthlySaving(year, month, amount, ACCOUNT_ID, memo);

        // Then
        then(actual.userId()).isEqualTo(CURRENT_USER_ID);
        then(actual.year()).isEqualTo(year);
        then(actual.month()).isEqualTo(month);
        then(actual.savingAmount()).isEqualTo(amount);
        then(actual.financialAccountId()).isEqualTo(ACCOUNT_ID);
        then(actual.memo()).isEqualTo(memo);
        verify(financialAccountRepository).findById(ACCOUNT_ID);
        verify(monthlySavingRepository).save(any(MonthlySaving.class));
    }

    @Test
    void testRecordMonthlySavingWithEmptyMemo() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        Money amount = new Money(30_000);
        Optional<Description> memo = Optional.empty();
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.empty()
        );
        when(financialAccountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(ownedAccount()));
        when(monthlySavingRepository.save(any(MonthlySaving.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        MonthlySaving actual = savingService.recordMonthlySaving(year, month, amount, ACCOUNT_ID, memo);

        // Then
        then(actual.userId()).isEqualTo(CURRENT_USER_ID);
        then(actual.year()).isEqualTo(year);
        then(actual.month()).isEqualTo(month);
        then(actual.savingAmount()).isEqualTo(amount);
        then(actual.financialAccountId()).isEqualTo(ACCOUNT_ID);
        then(actual.memo()).isEqualTo(Optional.empty());
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month);
        verify(financialAccountRepository).findById(ACCOUNT_ID);
        verify(monthlySavingRepository).save(any(MonthlySaving.class));
    }

    @Test
    void testRecordMonthlySavingWhenAlreadyExists() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        Money amount = new Money(30_000);
        Optional<Description> memo = Optional.of(new Description("memo"));
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.of(existingSaving(year, month))
        );

        // When / Then
        thenThrownBy(() -> savingService.recordMonthlySaving(year, month, amount, ACCOUNT_ID, memo)).isInstanceOf(
            IllegalStateException.class
        );
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month);
        verify(monthlySavingRepository, never()).save(any());
    }

    @Test
    void testRecordMonthlySavingWhenAccountNotFound() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        Money amount = new Money(30_000);
        Optional<Description> memo = Optional.of(new Description("memo"));
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.empty()
        );
        when(financialAccountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // When / Then
        thenThrownBy(() -> savingService.recordMonthlySaving(year, month, amount, ACCOUNT_ID, memo)).isInstanceOf(
            IllegalStateException.class
        );
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month);
        verify(financialAccountRepository).findById(ACCOUNT_ID);
        verify(monthlySavingRepository, never()).save(any());
    }

    @Test
    void testRecordMonthlySavingWhenAccountOwnedByOther() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        Money amount = new Money(30_000);
        Optional<Description> memo = Optional.of(new Description("memo"));
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.empty()
        );
        when(financialAccountRepository.findById(OTHER_ACCOUNT_ID)).thenReturn(Optional.of(otherUserAccount()));

        // When / Then
        thenThrownBy(() -> savingService.recordMonthlySaving(year, month, amount, OTHER_ACCOUNT_ID, memo)).isInstanceOf(
            IllegalStateException.class
        );
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month);
        verify(financialAccountRepository).findById(OTHER_ACCOUNT_ID);
        verify(monthlySavingRepository, never()).save(any());
    }

    @Test
    void testGetMonthlySaving() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        MonthlySaving expected = existingSaving(year, month);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.of(expected)
        );

        // When
        MonthlySaving actual = savingService.getMonthlySaving(year, month);

        // Then
        then(actual).isSameAs(expected);
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month);
    }

    @Test
    void testGetMonthlySavingWhenNotFound() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.empty()
        );

        // When / Then
        thenThrownBy(() -> savingService.getMonthlySaving(year, month)).isInstanceOf(IllegalStateException.class);
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month);
    }

    @Test
    void testUpdateMonthlySaving() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        Money newAmount = new Money(80_000);
        FinancialAccountId newAccountId = new FinancialAccountId(
            UUID.fromString("00000000-0000-0000-0000-000000000020")
        );
        Optional<Description> newMemo = Optional.of(new Description("更新後メモ"));
        MonthlySaving saving = existingSaving(year, month);
        FinancialAccount newAccount = new FinancialAccount(
            newAccountId,
            CURRENT_USER_ID,
            new AccountName("貯金口座"),
            new Money(0),
            false,
            null,
            null
        );
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.of(saving)
        );
        when(financialAccountRepository.findById(newAccountId)).thenReturn(Optional.of(newAccount));
        when(monthlySavingRepository.save(saving)).thenReturn(saving);

        // When
        MonthlySaving actual = savingService.updateMonthlySaving(year, month, newAmount, newAccountId, newMemo);

        // Then
        then(actual.savingAmount()).isEqualTo(newAmount);
        then(actual.financialAccountId()).isEqualTo(newAccountId);
        then(actual.memo()).isEqualTo(newMemo);
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month);
        verify(financialAccountRepository).findById(newAccountId);
        verify(monthlySavingRepository).save(saving);
    }

    @Test
    void testUpdateMonthlySavingWithEmptyMemo() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        Money newAmount = new Money(80_000);
        Optional<Description> newMemo = Optional.empty();
        MonthlySaving saving = existingSaving(year, month);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.of(saving)
        );
        when(financialAccountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(ownedAccount()));
        when(monthlySavingRepository.save(saving)).thenReturn(saving);

        // When
        MonthlySaving actual = savingService.updateMonthlySaving(year, month, newAmount, ACCOUNT_ID, newMemo);

        // Then
        then(actual.memo()).isEqualTo(Optional.empty());
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month);
        verify(financialAccountRepository).findById(ACCOUNT_ID);
        verify(monthlySavingRepository).save(saving);
    }

    @Test
    void testUpdateMonthlySavingWhenNotFound() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.empty()
        );

        // When / Then
        thenThrownBy(() ->
            savingService.updateMonthlySaving(
                year,
                month,
                new Money(1),
                ACCOUNT_ID,
                Optional.of(new Description("memo"))
            )
        ).isInstanceOf(IllegalStateException.class);
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository, never()).save(any());
    }

    @Test
    void testUpdateMonthlySavingWhenAccountOwnedByOther() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        MonthlySaving saving = existingSaving(year, month);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.of(saving)
        );
        when(financialAccountRepository.findById(OTHER_ACCOUNT_ID)).thenReturn(Optional.of(otherUserAccount()));

        // When / Then
        thenThrownBy(() ->
            savingService.updateMonthlySaving(
                year,
                month,
                new Money(1),
                OTHER_ACCOUNT_ID,
                Optional.of(new Description("memo"))
            )
        ).isInstanceOf(IllegalStateException.class);
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month);
        verify(monthlySavingRepository, never()).save(any());
    }

    @Test
    void testDeleteMonthlySaving() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        MonthlySaving saving = existingSaving(year, month);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.of(saving)
        );

        // When
        savingService.deleteMonthlySaving(year, month);

        // Then
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month);
        verify(monthlySavingRepository).delete(saving);
    }

    @Test
    void testDeleteMonthlySavingWhenNotFound() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month)).thenReturn(
            Optional.empty()
        );

        // When / Then
        thenThrownBy(() -> savingService.deleteMonthlySaving(year, month)).isInstanceOf(IllegalStateException.class);
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYearAndMonth(CURRENT_USER_ID, year, month);
        verify(monthlySavingRepository, never()).delete(any());
    }

    @Test
    void testGetSavingsByYear() {
        // Given
        Year year = new Year(2026);
        List<MonthlySaving> expected = List.of(existingSaving(year, new Month(1)), existingSaving(year, new Month(2)));
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYear(CURRENT_USER_ID, year)).thenReturn(expected);

        // When
        List<MonthlySaving> actual = savingService.getSavingsByYear(year);

        // Then
        then(actual).hasSize(2);
        then(actual).containsExactlyInAnyOrderElementsOf(expected);
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYear(CURRENT_USER_ID, year);
    }

    @Test
    void testGetSavingsByYearWhenEmpty() {
        // Given
        Year year = new Year(2026);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(monthlySavingRepository.findByUserIdAndYear(CURRENT_USER_ID, year)).thenReturn(List.of());

        // When
        List<MonthlySaving> actual = savingService.getSavingsByYear(year);

        // Then
        then(actual).isEmpty();
        verify(userRepository).findById(CURRENT_USER_ID);
        verify(monthlySavingRepository).findByUserIdAndYear(CURRENT_USER_ID, year);
    }
}
