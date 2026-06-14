package com.takata_kento.household_expenses.application.transaction;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.takata_kento.household_expenses.application.budget.BudgetService;
import com.takata_kento.household_expenses.domain.transaction.group.DailyGroupTransaction;
import com.takata_kento.household_expenses.domain.transaction.group.DailyGroupTransactionRepository;
import com.takata_kento.household_expenses.domain.transaction.personal.DailyPersonalTransaction;
import com.takata_kento.household_expenses.domain.transaction.personal.DailyPersonalTransactionRepository;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.user.UserRepository;
import com.takata_kento.household_expenses.domain.valueobject.DailyGroupTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import java.time.LocalDate;
import java.util.ArrayList;
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
class TransactionServiceTest {

    @Mock
    private DailyGroupTransactionRepository dailyGroupTransactionRepository;

    @Mock
    private DailyPersonalTransactionRepository dailyPersonalTransactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private TransactionService transactionService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final UserId OTHER_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
    private static final UserGroupId USER_GROUP_ID = new UserGroupId(
        UUID.fromString("00000000-0000-0000-0000-000000000100")
    );
    private static final LivingExpenseCategoryId CATEGORY_ID = new LivingExpenseCategoryId(
        UUID.fromString("00000000-0000-0000-0000-000000000200")
    );
    private static final LocalDate DATE = LocalDate.of(2026, 5, 10);

    private User currentUser() {
        return new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
    }

    private User userWithoutGroup() {
        return new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
    }

    private DailyGroupTransaction emptyGroupTransaction() {
        return new DailyGroupTransaction(
            new DailyGroupTransactionId(UUID.fromString("00000000-0000-0000-0000-000000000300")),
            USER_GROUP_ID,
            DATE,
            new HashSet<>(),
            0
        );
    }

    private DailyPersonalTransaction personalTransactionWith(Money income, Money... expenseAmounts) {
        DailyPersonalTransaction transaction = new DailyPersonalTransaction(
            new DailyPersonalTransactionId(UUID.fromString("00000000-0000-0000-0000-000000000400")),
            CURRENT_USER_ID,
            DATE,
            income,
            new ArrayList<>(),
            0
        );
        for (Money amount : expenseAmounts) {
            transaction.addPersonalExpense(amount, new Description("personal"));
        }
        return transaction;
    }

    // ---- calculateTotalExpense ----

    @Test
    void testCalculateTotalExpenseRoundsUp() {
        // Given
        Money totalLiving = new Money(1000);
        Money totalPersonal = new Money(500);
        int memberCount = 3;
        Money expected = new Money(334 + 500); // ceil(1000/3)=334

        // When
        Money actual = transactionService.calculateTotalExpense(totalLiving, totalPersonal, memberCount);

        // Then
        then(actual).isEqualTo(expected);
    }

    @Test
    void testCalculateTotalExpenseDivisible() {
        // Given
        Money totalLiving = new Money(900);
        Money totalPersonal = new Money(100);
        int memberCount = 3;
        Money expected = new Money(300 + 100);

        // When
        Money actual = transactionService.calculateTotalExpense(totalLiving, totalPersonal, memberCount);

        // Then
        then(actual).isEqualTo(expected);
    }

    @Test
    void testCalculateTotalExpenseSingleMember() {
        // Given
        Money totalLiving = new Money(1000);
        Money totalPersonal = new Money(250);
        int memberCount = 1;
        Money expected = new Money(1250);

        // When
        Money actual = transactionService.calculateTotalExpense(totalLiving, totalPersonal, memberCount);

        // Then
        then(actual).isEqualTo(expected);
    }

    // ---- recordDailyTransaction ----

    @Test
    void testRecordDailyTransactionCreatesNewGroupTransaction() {
        // Given
        Money income = new Money(10_000);
        List<LivingExpenseInput> living = List.of(
            new LivingExpenseInput(CATEGORY_ID, new Money(1000), new Description("food"))
        );
        List<PersonalExpenseInput> personal = List.of(
            new PersonalExpenseInput(new Money(500), new Description("coffee"))
        );
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(dailyPersonalTransactionRepository.existsByUserIdAndTransactionDate(CURRENT_USER_ID, DATE)).thenReturn(
            false
        );
        when(dailyGroupTransactionRepository.findByUserGroupIdAndTransactionDate(USER_GROUP_ID, DATE)).thenReturn(
            Optional.empty()
        );
        when(dailyGroupTransactionRepository.save(any(DailyGroupTransaction.class))).thenAnswer(inv ->
            inv.getArgument(0)
        );
        when(dailyPersonalTransactionRepository.save(any(DailyPersonalTransaction.class))).thenAnswer(inv ->
            inv.getArgument(0)
        );
        when(userRepository.findByUserGroupId(USER_GROUP_ID)).thenReturn(List.of(currentUser()));
        when(budgetService.calculateBudgetBalance(CURRENT_USER_ID, DATE)).thenReturn(new Money(49_000));

        // When
        DailyTransactionInfo actual = transactionService.recordDailyTransaction(
            CURRENT_USER_ID,
            DATE,
            income,
            living,
            personal
        );

        // Then
        then(actual.transactionDate()).isEqualTo(DATE);
        then(actual.income()).isEqualTo(income);
        then(actual.livingExpenses()).hasSize(1);
        then(actual.personalExpenses()).hasSize(1);
        then(actual.totalLivingExpense()).isEqualTo(new Money(1000));
        then(actual.totalPersonalExpense()).isEqualTo(new Money(500));
        then(actual.totalExpense()).isEqualTo(new Money(1500)); // ceil(1000/1)+500
        then(actual.budgetBalance()).isEqualTo(new Money(49_000));
        verify(dailyGroupTransactionRepository).save(any(DailyGroupTransaction.class));
        verify(dailyPersonalTransactionRepository).save(any(DailyPersonalTransaction.class));
    }

    @Test
    void testRecordDailyTransactionAppendsToExistingGroupTransaction() {
        // Given
        DailyGroupTransaction existing = emptyGroupTransaction();
        existing.addLivingExpense(OTHER_USER_ID, CATEGORY_ID, new Money(2000), new Description("other member"));
        Money income = new Money(0);
        List<LivingExpenseInput> living = List.of(
            new LivingExpenseInput(CATEGORY_ID, new Money(1000), new Description("food"))
        );
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(dailyPersonalTransactionRepository.existsByUserIdAndTransactionDate(CURRENT_USER_ID, DATE)).thenReturn(
            false
        );
        when(dailyGroupTransactionRepository.findByUserGroupIdAndTransactionDate(USER_GROUP_ID, DATE)).thenReturn(
            Optional.of(existing)
        );
        when(dailyGroupTransactionRepository.save(any(DailyGroupTransaction.class))).thenAnswer(inv ->
            inv.getArgument(0)
        );
        when(dailyPersonalTransactionRepository.save(any(DailyPersonalTransaction.class))).thenAnswer(inv ->
            inv.getArgument(0)
        );
        when(userRepository.findByUserGroupId(USER_GROUP_ID)).thenReturn(List.of(currentUser(), currentUser()));
        when(budgetService.calculateBudgetBalance(CURRENT_USER_ID, DATE)).thenReturn(new Money(1000));

        // When
        DailyTransactionInfo actual = transactionService.recordDailyTransaction(
            CURRENT_USER_ID,
            DATE,
            income,
            living,
            List.of()
        );

        // Then
        then(actual.livingExpenses()).hasSize(2);
        then(actual.totalLivingExpense()).isEqualTo(new Money(3000));
        then(actual.totalExpense()).isEqualTo(new Money(1500)); // ceil(3000/2)+0
        verify(dailyGroupTransactionRepository).save(existing);
    }

    @Test
    void testRecordDailyTransactionWhenPersonalAlreadyExists() {
        // Given
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(dailyPersonalTransactionRepository.existsByUserIdAndTransactionDate(CURRENT_USER_ID, DATE)).thenReturn(
            true
        );

        // When / Then
        thenThrownBy(() ->
            transactionService.recordDailyTransaction(CURRENT_USER_ID, DATE, new Money(0), List.of(), List.of())
        ).isInstanceOf(IllegalStateException.class);
        verify(dailyPersonalTransactionRepository, never()).save(any());
        verify(dailyGroupTransactionRepository, never()).save(any());
    }

    @Test
    void testRecordDailyTransactionWhenUserHasNoGroup() {
        // Given
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(userWithoutGroup()));

        // When / Then
        thenThrownBy(() ->
            transactionService.recordDailyTransaction(CURRENT_USER_ID, DATE, new Money(0), List.of(), List.of())
        ).isInstanceOf(IllegalStateException.class);
        verify(dailyPersonalTransactionRepository, never()).save(any());
        verify(dailyGroupTransactionRepository, never()).save(any());
    }

    // ---- getDailyTransaction ----

    @Test
    void testGetDailyTransaction() {
        // Given
        DailyGroupTransaction group = emptyGroupTransaction();
        group.addLivingExpense(CURRENT_USER_ID, CATEGORY_ID, new Money(1000), new Description("food"));
        DailyPersonalTransaction personal = personalTransactionWith(new Money(10_000), new Money(500));
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(dailyGroupTransactionRepository.findByUserGroupIdAndTransactionDate(USER_GROUP_ID, DATE)).thenReturn(
            Optional.of(group)
        );
        when(dailyPersonalTransactionRepository.findByUserIdAndTransactionDate(CURRENT_USER_ID, DATE)).thenReturn(
            Optional.of(personal)
        );
        when(userRepository.findByUserGroupId(USER_GROUP_ID)).thenReturn(List.of(currentUser(), currentUser()));
        when(budgetService.calculateBudgetBalance(CURRENT_USER_ID, DATE)).thenReturn(new Money(40_000));

        // When
        DailyTransactionInfo actual = transactionService.getDailyTransaction(CURRENT_USER_ID, DATE);

        // Then
        then(actual.income()).isEqualTo(new Money(10_000));
        then(actual.totalLivingExpense()).isEqualTo(new Money(1000));
        then(actual.totalPersonalExpense()).isEqualTo(new Money(500));
        then(actual.totalExpense()).isEqualTo(new Money(1000)); // ceil(1000/2)=500, +500
        then(actual.budgetBalance()).isEqualTo(new Money(40_000));
    }

    @Test
    void testGetDailyTransactionWhenNothingRecorded() {
        // Given
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(dailyGroupTransactionRepository.findByUserGroupIdAndTransactionDate(USER_GROUP_ID, DATE)).thenReturn(
            Optional.empty()
        );
        when(dailyPersonalTransactionRepository.findByUserIdAndTransactionDate(CURRENT_USER_ID, DATE)).thenReturn(
            Optional.empty()
        );
        when(userRepository.findByUserGroupId(USER_GROUP_ID)).thenReturn(List.of(currentUser()));
        when(budgetService.calculateBudgetBalance(CURRENT_USER_ID, DATE)).thenReturn(new Money(50_000));

        // When
        DailyTransactionInfo actual = transactionService.getDailyTransaction(CURRENT_USER_ID, DATE);

        // Then
        then(actual.income()).isEqualTo(new Money(0));
        then(actual.livingExpenses()).isEmpty();
        then(actual.personalExpenses()).isEmpty();
        then(actual.totalExpense()).isEqualTo(new Money(0));
        then(actual.budgetBalance()).isEqualTo(new Money(50_000));
    }

    // ---- updateDailyTransaction ----

    @Test
    void testUpdateDailyTransaction() {
        // Given
        DailyGroupTransaction group = emptyGroupTransaction();
        group.addLivingExpense(CURRENT_USER_ID, CATEGORY_ID, new Money(1000), new Description("old"));
        group.addLivingExpense(OTHER_USER_ID, CATEGORY_ID, new Money(5000), new Description("other member"));
        DailyPersonalTransaction personal = personalTransactionWith(new Money(10_000), new Money(500));
        Money newIncome = new Money(20_000);
        List<LivingExpenseInput> living = List.of(
            new LivingExpenseInput(CATEGORY_ID, new Money(3000), new Description("new"))
        );
        List<PersonalExpenseInput> newPersonal = List.of(
            new PersonalExpenseInput(new Money(700), new Description("lunch"))
        );
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(dailyPersonalTransactionRepository.findByUserIdAndTransactionDate(CURRENT_USER_ID, DATE)).thenReturn(
            Optional.of(personal)
        );
        when(dailyGroupTransactionRepository.findByUserGroupIdAndTransactionDate(USER_GROUP_ID, DATE)).thenReturn(
            Optional.of(group)
        );
        when(dailyPersonalTransactionRepository.save(any(DailyPersonalTransaction.class))).thenAnswer(inv ->
            inv.getArgument(0)
        );
        when(dailyGroupTransactionRepository.save(any(DailyGroupTransaction.class))).thenAnswer(inv ->
            inv.getArgument(0)
        );
        when(userRepository.findByUserGroupId(USER_GROUP_ID)).thenReturn(List.of(currentUser(), currentUser()));
        when(budgetService.calculateBudgetBalance(CURRENT_USER_ID, DATE)).thenReturn(new Money(1000));

        // When
        DailyTransactionInfo actual = transactionService.updateDailyTransaction(
            CURRENT_USER_ID,
            DATE,
            newIncome,
            living,
            newPersonal
        );

        // Then
        then(actual.income()).isEqualTo(newIncome);
        then(actual.totalPersonalExpense()).isEqualTo(new Money(700));
        then(actual.totalLivingExpense()).isEqualTo(new Money(8000)); // 3000(current) + 5000(other)
        then(actual.livingExpenses()).hasSize(2);
        then(actual.personalExpenses()).hasSize(1);
        verify(dailyPersonalTransactionRepository).save(personal);
        verify(dailyGroupTransactionRepository).save(group);
    }

    @Test
    void testUpdateDailyTransactionWhenPersonalNotFound() {
        // Given
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(dailyPersonalTransactionRepository.findByUserIdAndTransactionDate(CURRENT_USER_ID, DATE)).thenReturn(
            Optional.empty()
        );

        // When / Then
        thenThrownBy(() ->
            transactionService.updateDailyTransaction(CURRENT_USER_ID, DATE, new Money(0), List.of(), List.of())
        ).isInstanceOf(IllegalStateException.class);
        verify(dailyPersonalTransactionRepository, never()).save(any());
    }

    // ---- deleteDailyTransaction ----

    @Test
    void testDeleteDailyTransactionRemovesPersonalAndOwnLivingExpenses() {
        // Given
        DailyGroupTransaction group = emptyGroupTransaction();
        group.addLivingExpense(CURRENT_USER_ID, CATEGORY_ID, new Money(1000), new Description("mine"));
        group.addLivingExpense(OTHER_USER_ID, CATEGORY_ID, new Money(2000), new Description("other member"));
        DailyPersonalTransaction personal = personalTransactionWith(new Money(0));
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(dailyPersonalTransactionRepository.findByUserIdAndTransactionDate(CURRENT_USER_ID, DATE)).thenReturn(
            Optional.of(personal)
        );
        when(dailyGroupTransactionRepository.findByUserGroupIdAndTransactionDate(USER_GROUP_ID, DATE)).thenReturn(
            Optional.of(group)
        );

        // When
        transactionService.deleteDailyTransaction(CURRENT_USER_ID, DATE);

        // Then
        verify(dailyPersonalTransactionRepository).delete(personal);
        verify(dailyGroupTransactionRepository).save(group);
        verify(dailyGroupTransactionRepository, never()).delete(any());
        then(group.livingExpenses()).hasSize(1);
    }

    @Test
    void testDeleteDailyTransactionDeletesGroupWhenEmptyAfterRemoval() {
        // Given
        DailyGroupTransaction group = emptyGroupTransaction();
        group.addLivingExpense(CURRENT_USER_ID, CATEGORY_ID, new Money(1000), new Description("mine"));
        DailyPersonalTransaction personal = personalTransactionWith(new Money(0));
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(dailyPersonalTransactionRepository.findByUserIdAndTransactionDate(CURRENT_USER_ID, DATE)).thenReturn(
            Optional.of(personal)
        );
        when(dailyGroupTransactionRepository.findByUserGroupIdAndTransactionDate(USER_GROUP_ID, DATE)).thenReturn(
            Optional.of(group)
        );

        // When
        transactionService.deleteDailyTransaction(CURRENT_USER_ID, DATE);

        // Then
        verify(dailyPersonalTransactionRepository).delete(personal);
        verify(dailyGroupTransactionRepository).delete(group);
        verify(dailyGroupTransactionRepository, never()).save(any());
    }

    @Test
    void testDeleteDailyTransactionWhenPersonalNotFound() {
        // Given
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser()));
        when(dailyPersonalTransactionRepository.findByUserIdAndTransactionDate(CURRENT_USER_ID, DATE)).thenReturn(
            Optional.empty()
        );

        // When / Then
        thenThrownBy(() -> transactionService.deleteDailyTransaction(CURRENT_USER_ID, DATE)).isInstanceOf(
            IllegalStateException.class
        );
        verify(dailyPersonalTransactionRepository, never()).delete(any());
        verify(dailyGroupTransactionRepository, never()).delete(any());
    }
}
