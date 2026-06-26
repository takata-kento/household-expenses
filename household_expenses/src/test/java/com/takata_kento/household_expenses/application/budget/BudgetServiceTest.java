package com.takata_kento.household_expenses.application.budget;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.takata_kento.household_expenses.application.exception.GroupMembershipRequiredException;
import com.takata_kento.household_expenses.application.exception.ResourceNotFoundException;
import com.takata_kento.household_expenses.domain.budget.MonthlyBudget;
import com.takata_kento.household_expenses.domain.budget.MonthlyBudgetRepository;
import com.takata_kento.household_expenses.domain.transaction.group.DailyGroupTransaction;
import com.takata_kento.household_expenses.domain.transaction.group.DailyGroupTransactionRepository;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.user.UserRepository;
import com.takata_kento.household_expenses.domain.usergroup.UserGroup;
import com.takata_kento.household_expenses.domain.usergroup.UserGroupRepository;
import com.takata_kento.household_expenses.domain.valueobject.DailyGroupTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Day;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.GroupName;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.time.LocalDate;
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
class BudgetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private MonthlyBudgetRepository monthlyBudgetRepository;

    @Mock
    private DailyGroupTransactionRepository dailyGroupTransactionRepository;

    @InjectMocks
    private BudgetService budgetService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final UserGroupId USER_GROUP_ID = new UserGroupId(
        UUID.fromString("00000000-0000-0000-0000-000000000010")
    );
    private static final UserId CATEGORY_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000099"));
    private static final LivingExpenseCategoryId CATEGORY_ID = new LivingExpenseCategoryId(
        UUID.fromString("00000000-0000-0000-0000-000000000aaa")
    );

    @Test
    void testSetMonthlyBudget() {
        // Given
        Year year = new Year(2024);
        Month month = new Month(6);
        Money budgetAmount = new Money(100_000);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(monthlyBudgetRepository.findByUserGroupIdAndYearAndMonth(USER_GROUP_ID, year, month)).thenReturn(
            Optional.empty()
        );
        when(monthlyBudgetRepository.save(any(MonthlyBudget.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        SetMonthlyBudgetResult actual = budgetService.setMonthlyBudget(CURRENT_USER_ID, year, month, budgetAmount);

        // Then
        assertThat(actual.created()).isTrue();
        assertThat(actual.budget().userGroupId()).isEqualTo(USER_GROUP_ID);
        assertThat(actual.budget().year()).isEqualTo(year);
        assertThat(actual.budget().month()).isEqualTo(month);
        assertThat(actual.budget().budgetAmount()).isEqualTo(budgetAmount);
        assertThat(actual.budget().setByUserId()).isEqualTo(CURRENT_USER_ID);
        verify(monthlyBudgetRepository).save(any(MonthlyBudget.class));
    }

    @Test
    void testSetMonthlyBudgetWhenAlreadyExists() {
        // Given
        Year year = new Year(2024);
        Month month = new Month(6);
        Money newBudgetAmount = new Money(120_000);
        UserId previousSetByUserId = new UserId(UUID.fromString("00000000-0000-0000-0000-0000000000ff"));
        MonthlyBudget existing = MonthlyBudget.create(
            USER_GROUP_ID,
            year,
            month,
            new Money(100_000),
            previousSetByUserId
        );
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(monthlyBudgetRepository.findByUserGroupIdAndYearAndMonth(USER_GROUP_ID, year, month)).thenReturn(
            Optional.of(existing)
        );
        when(monthlyBudgetRepository.save(existing)).thenReturn(existing);

        // When
        SetMonthlyBudgetResult actual = budgetService.setMonthlyBudget(CURRENT_USER_ID, year, month, newBudgetAmount);

        // Then
        assertThat(actual.created()).isFalse();
        assertThat(actual.budget().budgetAmount()).isEqualTo(newBudgetAmount);
        assertThat(actual.budget().setByUserId()).isEqualTo(CURRENT_USER_ID);
        verify(monthlyBudgetRepository).save(existing);
    }

    @Test
    void testSetMonthlyBudgetWhenNotInGroup() {
        // Given
        Year year = new Year(2024);
        Month month = new Month(6);
        Money budgetAmount = new Money(100_000);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));

        // When / Then
        assertThatThrownBy(() ->
            budgetService.setMonthlyBudget(CURRENT_USER_ID, year, month, budgetAmount)
        ).isInstanceOf(GroupMembershipRequiredException.class);
        verify(monthlyBudgetRepository, never()).save(any());
    }

    @Test
    void testGetMonthlyBudget() {
        // Given
        Year year = new Year(2024);
        Month month = new Month(6);
        MonthlyBudget budget = MonthlyBudget.create(USER_GROUP_ID, year, month, new Money(100_000), CURRENT_USER_ID);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(monthlyBudgetRepository.findByUserGroupIdAndYearAndMonth(USER_GROUP_ID, year, month)).thenReturn(
            Optional.of(budget)
        );

        // When
        MonthlyBudget actual = budgetService.getMonthlyBudget(CURRENT_USER_ID, year, month);

        // Then
        assertThat(actual).isEqualTo(budget);
    }

    @Test
    void testGetMonthlyBudgetWhenNotFound() {
        // Given
        Year year = new Year(2024);
        Month month = new Month(6);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(monthlyBudgetRepository.findByUserGroupIdAndYearAndMonth(USER_GROUP_ID, year, month)).thenReturn(
            Optional.empty()
        );

        // When / Then
        assertThatThrownBy(() -> budgetService.getMonthlyBudget(CURRENT_USER_ID, year, month)).isInstanceOf(
            ResourceNotFoundException.class
        );
    }

    @Test
    void testGetMonthlyBudgetWhenNotInGroup() {
        // Given
        Year year = new Year(2024);
        Month month = new Month(6);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));

        // When / Then
        assertThatThrownBy(() -> budgetService.getMonthlyBudget(CURRENT_USER_ID, year, month)).isInstanceOf(
            GroupMembershipRequiredException.class
        );
        verify(monthlyBudgetRepository, never()).findByUserGroupIdAndYearAndMonth(any(), any(), any());
    }

    @Test
    void testGetMonthlyBudgetsByYear() {
        // Given
        Year year = new Year(2024);
        MonthlyBudget june = MonthlyBudget.create(
            USER_GROUP_ID,
            year,
            new Month(6),
            new Money(100_000),
            CURRENT_USER_ID
        );
        MonthlyBudget july = MonthlyBudget.create(
            USER_GROUP_ID,
            year,
            new Month(7),
            new Money(110_000),
            CURRENT_USER_ID
        );
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(monthlyBudgetRepository.findByUserGroupIdAndYear(USER_GROUP_ID, year)).thenReturn(List.of(june, july));

        // When
        List<MonthlyBudget> actual = budgetService.getMonthlyBudgetsByYear(CURRENT_USER_ID, year);

        // Then
        assertThat(actual).containsExactly(june, july);
    }

    @Test
    void testGetMonthlyBudgetsByYearWhenNotInGroup() {
        // Given
        Year year = new Year(2024);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));

        // When / Then
        assertThatThrownBy(() -> budgetService.getMonthlyBudgetsByYear(CURRENT_USER_ID, year)).isInstanceOf(
            GroupMembershipRequiredException.class
        );
        verify(monthlyBudgetRepository, never()).findByUserGroupIdAndYear(any(), any());
    }

    @Test
    void testCalculateBudgetBalance() {
        // Given
        // monthStartDay=1, targetDate=2024-06-15 → 期間 2024-06-01 〜 2024-06-15
        Year year = new Year(2024);
        Month month = new Month(6);
        LocalDate targetDate = LocalDate.of(2024, 6, 15);
        LocalDate periodStart = LocalDate.of(2024, 6, 1);
        Money budgetAmount = new Money(100_000);

        UserGroup userGroup = new UserGroup(
            USER_GROUP_ID,
            new GroupName("テスト"),
            new Day(1),
            CURRENT_USER_ID,
            null,
            null,
            null
        );
        MonthlyBudget budget = MonthlyBudget.create(USER_GROUP_ID, year, month, budgetAmount, CURRENT_USER_ID);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);

        DailyGroupTransaction tx1 = new DailyGroupTransaction(
            new DailyGroupTransactionId(UUID.randomUUID()),
            USER_GROUP_ID,
            LocalDate.of(2024, 6, 5),
            new HashSet<>(),
            null
        );
        tx1.addLivingExpense(CATEGORY_USER_ID, CATEGORY_ID, new Money(2_000), new Description("食費"));
        tx1.addLivingExpense(CATEGORY_USER_ID, CATEGORY_ID, new Money(3_000), new Description("日用品"));
        DailyGroupTransaction tx2 = new DailyGroupTransaction(
            new DailyGroupTransactionId(UUID.randomUUID()),
            USER_GROUP_ID,
            LocalDate.of(2024, 6, 10),
            new HashSet<>(),
            null
        );
        tx2.addLivingExpense(CATEGORY_USER_ID, CATEGORY_ID, new Money(1500), new Description("食費"));

        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userGroupRepository.findById(USER_GROUP_ID)).thenReturn(Optional.of(userGroup));
        when(monthlyBudgetRepository.findByUserGroupIdAndYearAndMonth(USER_GROUP_ID, year, month)).thenReturn(
            Optional.of(budget)
        );
        when(
            dailyGroupTransactionRepository.findByUserGroupIdAndTransactionDateBetween(
                USER_GROUP_ID,
                periodStart,
                targetDate
            )
        ).thenReturn(List.of(tx1, tx2));

        // When
        Money actual = budgetService.calculateBudgetBalance(CURRENT_USER_ID, targetDate);

        // Then
        // 100,000 - (2,000 + 3,000 + 1,500) = 93,500
        assertThat(actual).isEqualTo(new Money(93_500));
    }

    @Test
    void testCalculateBudgetBalanceWithMonthStartDayMidMonthBefore() {
        // Given
        // monthStartDay=25, targetDate=2024-07-10 → 期間 2024-06-25 〜 2024-07-10
        // → MonthlyBudget(2024, 6) を参照
        Year year = new Year(2024);
        Month month = new Month(6);
        LocalDate targetDate = LocalDate.of(2024, 7, 10);
        LocalDate periodStart = LocalDate.of(2024, 6, 25);

        UserGroup userGroup = new UserGroup(
            USER_GROUP_ID,
            new GroupName("テスト"),
            new Day(25),
            CURRENT_USER_ID,
            null,
            null,
            null
        );
        MonthlyBudget budget = MonthlyBudget.create(USER_GROUP_ID, year, month, new Money(80_000), CURRENT_USER_ID);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);

        DailyGroupTransaction tx = new DailyGroupTransaction(
            new DailyGroupTransactionId(UUID.randomUUID()),
            USER_GROUP_ID,
            LocalDate.of(2024, 6, 30),
            new HashSet<>(),
            null
        );
        tx.addLivingExpense(CATEGORY_USER_ID, CATEGORY_ID, new Money(20_000), new Description("食費"));

        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userGroupRepository.findById(USER_GROUP_ID)).thenReturn(Optional.of(userGroup));
        when(monthlyBudgetRepository.findByUserGroupIdAndYearAndMonth(USER_GROUP_ID, year, month)).thenReturn(
            Optional.of(budget)
        );
        when(
            dailyGroupTransactionRepository.findByUserGroupIdAndTransactionDateBetween(
                USER_GROUP_ID,
                periodStart,
                targetDate
            )
        ).thenReturn(List.of(tx));

        // When
        Money actual = budgetService.calculateBudgetBalance(CURRENT_USER_ID, targetDate);

        // Then
        // 80,000 - 20,000 = 60,000
        assertThat(actual).isEqualTo(new Money(60_000));
    }

    @Test
    void testCalculateBudgetBalanceWithMonthStartDayMidMonthAfter() {
        // Given
        // monthStartDay=25, targetDate=2024-07-30 → 期間 2024-07-25 〜 2024-07-30
        // → MonthlyBudget(2024, 7) を参照
        Year year = new Year(2024);
        Month month = new Month(7);
        LocalDate targetDate = LocalDate.of(2024, 7, 30);
        LocalDate periodStart = LocalDate.of(2024, 7, 25);

        UserGroup userGroup = new UserGroup(
            USER_GROUP_ID,
            new GroupName("テスト"),
            new Day(25),
            CURRENT_USER_ID,
            null,
            null,
            null
        );
        MonthlyBudget budget = MonthlyBudget.create(USER_GROUP_ID, year, month, new Money(50_000), CURRENT_USER_ID);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);

        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userGroupRepository.findById(USER_GROUP_ID)).thenReturn(Optional.of(userGroup));
        when(monthlyBudgetRepository.findByUserGroupIdAndYearAndMonth(USER_GROUP_ID, year, month)).thenReturn(
            Optional.of(budget)
        );
        when(
            dailyGroupTransactionRepository.findByUserGroupIdAndTransactionDateBetween(
                USER_GROUP_ID,
                periodStart,
                targetDate
            )
        ).thenReturn(List.of());

        // When
        Money actual = budgetService.calculateBudgetBalance(CURRENT_USER_ID, targetDate);

        // Then
        assertThat(actual).isEqualTo(new Money(50_000));
    }

    @Test
    void testCalculateBudgetBalanceWhenBudgetNotFound() {
        // Given
        LocalDate targetDate = LocalDate.of(2024, 6, 15);
        UserGroup userGroup = new UserGroup(
            USER_GROUP_ID,
            new GroupName("テスト"),
            new Day(1),
            CURRENT_USER_ID,
            null,
            null,
            null
        );
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);

        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userGroupRepository.findById(USER_GROUP_ID)).thenReturn(Optional.of(userGroup));
        when(
            monthlyBudgetRepository.findByUserGroupIdAndYearAndMonth(USER_GROUP_ID, new Year(2024), new Month(6))
        ).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> budgetService.calculateBudgetBalance(CURRENT_USER_ID, targetDate)).isInstanceOf(
            ResourceNotFoundException.class
        );
        verify(dailyGroupTransactionRepository, never()).findByUserGroupIdAndTransactionDateBetween(
            any(),
            any(),
            any()
        );
    }

    @Test
    void testCalculateBudgetBalanceWhenNotInGroup() {
        // Given
        LocalDate targetDate = LocalDate.of(2024, 6, 15);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));

        // When / Then
        assertThatThrownBy(() -> budgetService.calculateBudgetBalance(CURRENT_USER_ID, targetDate)).isInstanceOf(
            GroupMembershipRequiredException.class
        );
        verify(userGroupRepository, never()).findById(any());
        verify(monthlyBudgetRepository, never()).findByUserGroupIdAndYearAndMonth(any(), any(), any());
    }
}
