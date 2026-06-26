package com.takata_kento.household_expenses.application.expense;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.takata_kento.household_expenses.application.exception.ForbiddenException;
import com.takata_kento.household_expenses.application.exception.GroupMembershipRequiredException;
import com.takata_kento.household_expenses.application.exception.ResourceNotFoundException;
import com.takata_kento.household_expenses.domain.expense.category.FixedExpenseCategory;
import com.takata_kento.household_expenses.domain.expense.category.FixedExpenseCategoryRepository;
import com.takata_kento.household_expenses.domain.expense.category.LivingExpenseCategory;
import com.takata_kento.household_expenses.domain.expense.category.LivingExpenseCategoryRepository;
import com.takata_kento.household_expenses.domain.expense.history.FixedExpenseHistory;
import com.takata_kento.household_expenses.domain.expense.history.FixedExpenseHistoryRepository;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.user.UserRepository;
import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseHistoryId;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LivingExpenseCategoryRepository livingExpenseCategoryRepository;

    @Mock
    private FixedExpenseCategoryRepository fixedExpenseCategoryRepository;

    @Mock
    private FixedExpenseHistoryRepository fixedExpenseHistoryRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final UserGroupId USER_GROUP_ID = new UserGroupId(
        UUID.fromString("00000000-0000-0000-0000-000000000010")
    );
    private static final UserGroupId OTHER_USER_GROUP_ID = new UserGroupId(
        UUID.fromString("00000000-0000-0000-0000-000000000020")
    );

    private void mockCurrentUserInGroup() {
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
    }

    private void mockCurrentUserNotInGroup() {
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
    }

    // ===== createLivingExpenseCategory =====

    @Test
    void testCreateLivingExpenseCategory() {
        // Given
        CategoryName expectedName = new CategoryName("食費");
        Description expectedDescription = new Description("食材費");
        mockCurrentUserInGroup();
        when(livingExpenseCategoryRepository.save(any(LivingExpenseCategory.class))).thenAnswer(inv ->
            inv.getArgument(0)
        );

        // When
        LivingExpenseCategory actual = expenseService.createLivingExpenseCategory(
            CURRENT_USER_ID,
            expectedName,
            expectedDescription
        );

        // Then
        then(actual.categoryName()).isEqualTo(expectedName);
        then(actual.description()).isEqualTo(expectedDescription);
        then(actual.userGroupId()).isEqualTo(USER_GROUP_ID);
        then(actual.isDefault()).isFalse();
        verify(livingExpenseCategoryRepository).save(any(LivingExpenseCategory.class));
    }

    @Test
    void testCreateLivingExpenseCategoryWhenNotInGroup() {
        // Given
        CategoryName name = new CategoryName("食費");
        Description description = new Description("食材費");
        mockCurrentUserNotInGroup();

        // When / Then
        thenThrownBy(() -> expenseService.createLivingExpenseCategory(CURRENT_USER_ID, name, description)).isInstanceOf(
            GroupMembershipRequiredException.class
        );
        verify(livingExpenseCategoryRepository, never()).save(any());
    }

    // ===== updateLivingExpenseCategory =====

    @Test
    void testUpdateLivingExpenseCategory() {
        // Given
        LivingExpenseCategoryId categoryId = new LivingExpenseCategoryId(UUID.randomUUID());
        CategoryName expectedName = new CategoryName("新分類名");
        Description expectedDescription = new Description("新説明");
        LivingExpenseCategory existing = new LivingExpenseCategory(
            categoryId,
            USER_GROUP_ID,
            new CategoryName("旧分類名"),
            new Description("旧説明"),
            false,
            1
        );
        mockCurrentUserInGroup();
        when(livingExpenseCategoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        when(livingExpenseCategoryRepository.save(existing)).thenReturn(existing);

        // When
        LivingExpenseCategory actual = expenseService.updateLivingExpenseCategory(
            CURRENT_USER_ID,
            categoryId,
            expectedName,
            expectedDescription
        );

        // Then
        then(actual.categoryName()).isEqualTo(expectedName);
        then(actual.description()).isEqualTo(expectedDescription);
        then(actual.userGroupId()).isEqualTo(USER_GROUP_ID);
        verify(livingExpenseCategoryRepository).save(existing);
    }

    @Test
    void testUpdateLivingExpenseCategoryWhenNotFound() {
        // Given
        LivingExpenseCategoryId categoryId = new LivingExpenseCategoryId(UUID.randomUUID());
        CategoryName name = new CategoryName("分類名");
        Description description = new Description("説明");
        mockCurrentUserInGroup();
        when(livingExpenseCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When / Then
        thenThrownBy(() ->
            expenseService.updateLivingExpenseCategory(CURRENT_USER_ID, categoryId, name, description)
        ).isInstanceOf(ResourceNotFoundException.class);
        verify(livingExpenseCategoryRepository, never()).save(any());
    }

    @Test
    void testUpdateLivingExpenseCategoryWhenBelongsToOtherGroup() {
        // Given
        LivingExpenseCategoryId categoryId = new LivingExpenseCategoryId(UUID.randomUUID());
        CategoryName name = new CategoryName("分類名");
        Description description = new Description("説明");
        LivingExpenseCategory otherGroupCategory = new LivingExpenseCategory(
            categoryId,
            OTHER_USER_GROUP_ID,
            new CategoryName("他グループ分類"),
            new Description("他グループ説明"),
            false,
            1
        );
        mockCurrentUserInGroup();
        when(livingExpenseCategoryRepository.findById(categoryId)).thenReturn(Optional.of(otherGroupCategory));

        // When / Then
        thenThrownBy(() ->
            expenseService.updateLivingExpenseCategory(CURRENT_USER_ID, categoryId, name, description)
        ).isInstanceOf(ForbiddenException.class);
        verify(livingExpenseCategoryRepository, never()).save(any());
    }

    @Test
    void testUpdateLivingExpenseCategoryWhenNotInGroup() {
        // Given
        LivingExpenseCategoryId categoryId = new LivingExpenseCategoryId(UUID.randomUUID());
        CategoryName name = new CategoryName("分類名");
        Description description = new Description("説明");
        mockCurrentUserNotInGroup();

        // When / Then
        thenThrownBy(() ->
            expenseService.updateLivingExpenseCategory(CURRENT_USER_ID, categoryId, name, description)
        ).isInstanceOf(GroupMembershipRequiredException.class);
        verify(livingExpenseCategoryRepository, never()).save(any());
    }

    // ===== deleteLivingExpenseCategory =====

    @Test
    void testDeleteLivingExpenseCategory() {
        // Given
        LivingExpenseCategoryId categoryId = new LivingExpenseCategoryId(UUID.randomUUID());
        LivingExpenseCategory existing = new LivingExpenseCategory(
            categoryId,
            USER_GROUP_ID,
            new CategoryName("食費"),
            new Description("食材費"),
            false,
            1
        );
        mockCurrentUserInGroup();
        when(livingExpenseCategoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));

        // When
        expenseService.deleteLivingExpenseCategory(CURRENT_USER_ID, categoryId);

        // Then
        verify(livingExpenseCategoryRepository).findById(categoryId);
        verify(livingExpenseCategoryRepository).deleteById(categoryId);
    }

    @Test
    void testDeleteLivingExpenseCategoryWhenNotFound() {
        // Given
        LivingExpenseCategoryId categoryId = new LivingExpenseCategoryId(UUID.randomUUID());
        mockCurrentUserInGroup();
        when(livingExpenseCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When / Then
        thenThrownBy(() -> expenseService.deleteLivingExpenseCategory(CURRENT_USER_ID, categoryId)).isInstanceOf(
            ResourceNotFoundException.class
        );
        verify(livingExpenseCategoryRepository).findById(categoryId);
        verify(livingExpenseCategoryRepository, never()).deleteById(any(LivingExpenseCategoryId.class));
    }

    @Test
    void testDeleteLivingExpenseCategoryWhenBelongsToOtherGroup() {
        // Given
        LivingExpenseCategoryId categoryId = new LivingExpenseCategoryId(UUID.randomUUID());
        LivingExpenseCategory otherGroupCategory = new LivingExpenseCategory(
            categoryId,
            OTHER_USER_GROUP_ID,
            new CategoryName("他グループ分類"),
            new Description("他グループ説明"),
            false,
            1
        );
        mockCurrentUserInGroup();
        when(livingExpenseCategoryRepository.findById(categoryId)).thenReturn(Optional.of(otherGroupCategory));

        // When / Then
        thenThrownBy(() -> expenseService.deleteLivingExpenseCategory(CURRENT_USER_ID, categoryId)).isInstanceOf(
            ForbiddenException.class
        );
        verify(livingExpenseCategoryRepository).findById(categoryId);
        verify(livingExpenseCategoryRepository, never()).deleteById(any(LivingExpenseCategoryId.class));
    }

    @Test
    void testDeleteLivingExpenseCategoryWhenNotInGroup() {
        // Given
        LivingExpenseCategoryId categoryId = new LivingExpenseCategoryId(UUID.randomUUID());
        mockCurrentUserNotInGroup();

        // When / Then
        thenThrownBy(() -> expenseService.deleteLivingExpenseCategory(CURRENT_USER_ID, categoryId)).isInstanceOf(
            GroupMembershipRequiredException.class
        );
        verify(livingExpenseCategoryRepository, never()).deleteById(any(LivingExpenseCategoryId.class));
    }

    // ===== createFixedExpenseCategory =====

    @Test
    void testCreateFixedExpenseCategory() {
        // Given
        CategoryName expectedName = new CategoryName("家賃");
        Description expectedDescription = new Description("毎月の家賃");
        Money expectedDefaultAmount = new Money(80000);
        mockCurrentUserInGroup();
        when(fixedExpenseCategoryRepository.save(any(FixedExpenseCategory.class))).thenAnswer(inv ->
            inv.getArgument(0)
        );

        // When
        FixedExpenseCategory actual = expenseService.createFixedExpenseCategory(
            CURRENT_USER_ID,
            expectedName,
            expectedDescription,
            expectedDefaultAmount
        );

        // Then
        then(actual.categoryName()).isEqualTo(expectedName);
        then(actual.description()).isEqualTo(expectedDescription);
        then(actual.defaultAmount()).isEqualTo(expectedDefaultAmount);
        then(actual.userGroupId()).isEqualTo(USER_GROUP_ID);
        verify(fixedExpenseCategoryRepository).save(any(FixedExpenseCategory.class));
    }

    @Test
    void testCreateFixedExpenseCategoryWhenNotInGroup() {
        // Given
        CategoryName name = new CategoryName("家賃");
        Description description = new Description("毎月の家賃");
        Money defaultAmount = new Money(80000);
        mockCurrentUserNotInGroup();

        // When / Then
        thenThrownBy(() ->
            expenseService.createFixedExpenseCategory(CURRENT_USER_ID, name, description, defaultAmount)
        ).isInstanceOf(GroupMembershipRequiredException.class);
        verify(fixedExpenseCategoryRepository, never()).save(any());
    }

    // ===== setFixedExpenseAmount =====

    @Test
    void testSetFixedExpenseAmountCreatesNewWhenNotExists() {
        // Given
        FixedExpenseCategoryId categoryId = new FixedExpenseCategoryId(UUID.randomUUID());
        Year expectedYear = new Year(2026);
        Month expectedMonth = new Month(5);
        Money expectedAmount = new Money(80000);
        LocalDate expectedEffectiveDate = LocalDate.of(2026, 5, 1);
        Optional<Description> expectedMemo = Optional.of(new Description("5月家賃"));
        FixedExpenseCategory category = new FixedExpenseCategory(
            categoryId,
            USER_GROUP_ID,
            new CategoryName("家賃"),
            new Description("毎月の家賃"),
            new Money(80000),
            1
        );
        mockCurrentUserInGroup();
        when(fixedExpenseCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(
            fixedExpenseHistoryRepository.findByFixedExpenseCategoryIdAndYearAndMonth(
                categoryId,
                expectedYear,
                expectedMonth
            )
        ).thenReturn(Optional.empty());
        when(fixedExpenseHistoryRepository.save(any(FixedExpenseHistory.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        FixedExpenseHistory actual = expenseService.setFixedExpenseAmount(
            CURRENT_USER_ID,
            categoryId,
            expectedYear,
            expectedMonth,
            expectedAmount,
            expectedEffectiveDate,
            expectedMemo
        );

        // Then
        then(actual.fixedExpenseCategoryId()).isEqualTo(categoryId);
        then(actual.year()).isEqualTo(expectedYear);
        then(actual.month()).isEqualTo(expectedMonth);
        then(actual.amount()).isEqualTo(expectedAmount);
        then(actual.effectiveDate()).isEqualTo(expectedEffectiveDate);
        then(actual.memo()).isEqualTo(expectedMemo);
        verify(fixedExpenseHistoryRepository).save(any(FixedExpenseHistory.class));
    }

    @Test
    void testSetFixedExpenseAmountUpdatesWhenExists() {
        // Given
        FixedExpenseCategoryId categoryId = new FixedExpenseCategoryId(UUID.randomUUID());
        Year year = new Year(2026);
        Month month = new Month(5);
        Money expectedNewAmount = new Money(85000);
        LocalDate expectedNewEffectiveDate = LocalDate.of(2026, 5, 15);
        Optional<Description> expectedNewMemo = Optional.of(new Description("値上げ後の家賃"));
        FixedExpenseCategory category = new FixedExpenseCategory(
            categoryId,
            USER_GROUP_ID,
            new CategoryName("家賃"),
            new Description("毎月の家賃"),
            new Money(80000),
            1
        );
        FixedExpenseHistory existing = new FixedExpenseHistory(
            new FixedExpenseHistoryId(UUID.randomUUID()),
            categoryId,
            year,
            month,
            new Money(80000),
            LocalDate.of(2026, 5, 1),
            Optional.of(new Description("旧メモ")),
            1
        );
        mockCurrentUserInGroup();
        when(fixedExpenseCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(
            fixedExpenseHistoryRepository.findByFixedExpenseCategoryIdAndYearAndMonth(categoryId, year, month)
        ).thenReturn(Optional.of(existing));
        when(fixedExpenseHistoryRepository.save(existing)).thenReturn(existing);

        // When
        FixedExpenseHistory actual = expenseService.setFixedExpenseAmount(
            CURRENT_USER_ID,
            categoryId,
            year,
            month,
            expectedNewAmount,
            expectedNewEffectiveDate,
            expectedNewMemo
        );

        // Then
        then(actual.fixedExpenseCategoryId()).isEqualTo(categoryId);
        then(actual.effectiveDate()).isEqualTo(expectedNewEffectiveDate);
        then(actual.memo()).isEqualTo(expectedNewMemo);
        then(actual.year()).isEqualTo(year);
        then(actual.month()).isEqualTo(month);
        verify(fixedExpenseCategoryRepository).findById(categoryId);
        verify(fixedExpenseHistoryRepository).findByFixedExpenseCategoryIdAndYearAndMonth(categoryId, year, month);
        verify(fixedExpenseHistoryRepository).save(existing);
    }

    @Test
    void testSetFixedExpenseAmountWhenCategoryNotFound() {
        // Given
        FixedExpenseCategoryId categoryId = new FixedExpenseCategoryId(UUID.randomUUID());
        mockCurrentUserInGroup();
        when(fixedExpenseCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When / Then
        thenThrownBy(() ->
            expenseService.setFixedExpenseAmount(
                CURRENT_USER_ID,
                categoryId,
                new Year(2026),
                new Month(5),
                new Money(80000),
                LocalDate.of(2026, 5, 1),
                Optional.empty()
            )
        ).isInstanceOf(ResourceNotFoundException.class);
        verify(fixedExpenseCategoryRepository).findById(categoryId);
        verify(fixedExpenseHistoryRepository, never()).save(any());
    }

    @Test
    void testSetFixedExpenseAmountWhenCategoryBelongsToOtherGroup() {
        // Given
        FixedExpenseCategoryId categoryId = new FixedExpenseCategoryId(UUID.randomUUID());
        FixedExpenseCategory otherGroupCategory = new FixedExpenseCategory(
            categoryId,
            OTHER_USER_GROUP_ID,
            new CategoryName("他グループ家賃"),
            new Description("他グループ"),
            new Money(80000),
            1
        );
        mockCurrentUserInGroup();
        when(fixedExpenseCategoryRepository.findById(categoryId)).thenReturn(Optional.of(otherGroupCategory));

        // When / Then
        thenThrownBy(() ->
            expenseService.setFixedExpenseAmount(
                CURRENT_USER_ID,
                categoryId,
                new Year(2026),
                new Month(5),
                new Money(80000),
                LocalDate.of(2026, 5, 1),
                Optional.empty()
            )
        ).isInstanceOf(ForbiddenException.class);
        verify(fixedExpenseCategoryRepository).findById(categoryId);
        verify(fixedExpenseHistoryRepository, never()).save(any());
    }

    @Test
    void testSetFixedExpenseAmountWhenNotInGroup() {
        // Given
        FixedExpenseCategoryId categoryId = new FixedExpenseCategoryId(UUID.randomUUID());
        mockCurrentUserNotInGroup();

        // When / Then
        thenThrownBy(() ->
            expenseService.setFixedExpenseAmount(
                CURRENT_USER_ID,
                categoryId,
                new Year(2026),
                new Month(5),
                new Money(80000),
                LocalDate.of(2026, 5, 1),
                Optional.empty()
            )
        ).isInstanceOf(GroupMembershipRequiredException.class);
        verify(fixedExpenseHistoryRepository, never()).save(any());
    }

    // ===== getFixedExpenses =====

    @Test
    void testGetFixedExpenses() {
        // Given
        Year expectedYear = new Year(2026);
        Month expectedMonth = new Month(5);
        FixedExpenseCategoryId categoryId1 = new FixedExpenseCategoryId(UUID.randomUUID());
        FixedExpenseCategoryId categoryId2 = new FixedExpenseCategoryId(UUID.randomUUID());
        FixedExpenseCategory category1 = new FixedExpenseCategory(
            categoryId1,
            USER_GROUP_ID,
            new CategoryName("家賃"),
            new Description("毎月の家賃"),
            new Money(80000),
            1
        );
        FixedExpenseCategory category2 = new FixedExpenseCategory(
            categoryId2,
            USER_GROUP_ID,
            new CategoryName("光熱費"),
            new Description("電気・ガス・水道"),
            new Money(15000),
            1
        );
        FixedExpenseHistory history1 = FixedExpenseHistory.create(
            categoryId1,
            expectedYear,
            expectedMonth,
            new Money(80000),
            LocalDate.of(2026, 5, 1),
            Optional.empty()
        );
        FixedExpenseHistory history2 = FixedExpenseHistory.create(
            categoryId2,
            expectedYear,
            expectedMonth,
            new Money(15000),
            LocalDate.of(2026, 5, 1),
            Optional.empty()
        );
        mockCurrentUserInGroup();
        when(fixedExpenseCategoryRepository.findByUserGroupId(USER_GROUP_ID)).thenReturn(List.of(category1, category2));
        when(
            fixedExpenseHistoryRepository.findByFixedExpenseCategoryIdInAndYearAndMonth(
                List.of(categoryId1, categoryId2),
                expectedYear,
                expectedMonth
            )
        ).thenReturn(List.of(history1, history2));

        // When
        List<FixedExpenseHistory> actual = expenseService.getFixedExpenses(
            CURRENT_USER_ID,
            expectedYear,
            expectedMonth
        );

        // Then
        then(actual).containsExactlyInAnyOrder(history1, history2);
        verify(fixedExpenseCategoryRepository).findByUserGroupId(USER_GROUP_ID);
        verify(fixedExpenseHistoryRepository).findByFixedExpenseCategoryIdInAndYearAndMonth(
            List.of(categoryId1, categoryId2),
            expectedYear,
            expectedMonth
        );
    }

    @Test
    void testGetFixedExpensesWhenNoCategories() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        mockCurrentUserInGroup();
        when(fixedExpenseCategoryRepository.findByUserGroupId(USER_GROUP_ID)).thenReturn(List.of());

        // When
        List<FixedExpenseHistory> actual = expenseService.getFixedExpenses(CURRENT_USER_ID, year, month);

        // Then
        then(actual).isEmpty();
        verify(fixedExpenseHistoryRepository, never()).findByFixedExpenseCategoryIdInAndYearAndMonth(
            any(),
            any(),
            any()
        );
    }

    @Test
    void testGetFixedExpensesWhenNotInGroup() {
        // Given
        Year year = new Year(2026);
        Month month = new Month(5);
        mockCurrentUserNotInGroup();

        // When / Then
        thenThrownBy(() -> expenseService.getFixedExpenses(CURRENT_USER_ID, year, month)).isInstanceOf(
            GroupMembershipRequiredException.class
        );
        verify(fixedExpenseCategoryRepository, never()).findByUserGroupId(any());
    }

    // ===== getLivingExpenseCategories =====

    @Test
    void testGetLivingExpenseCategories() {
        // Given
        LivingExpenseCategory category1 = new LivingExpenseCategory(
            new LivingExpenseCategoryId(UUID.randomUUID()),
            USER_GROUP_ID,
            new CategoryName("食費"),
            new Description("食材費"),
            false,
            1
        );
        LivingExpenseCategory category2 = new LivingExpenseCategory(
            new LivingExpenseCategoryId(UUID.randomUUID()),
            USER_GROUP_ID,
            new CategoryName("日用品"),
            new Description("日用品費"),
            true,
            1
        );
        mockCurrentUserInGroup();
        when(livingExpenseCategoryRepository.findByUserGroupId(USER_GROUP_ID)).thenReturn(
            List.of(category1, category2)
        );

        // When
        List<LivingExpenseCategory> actual = expenseService.getLivingExpenseCategories(CURRENT_USER_ID);

        // Then
        then(actual).containsExactly(category1, category2);
        verify(livingExpenseCategoryRepository).findByUserGroupId(USER_GROUP_ID);
    }

    @Test
    void testGetLivingExpenseCategoriesWhenNotInGroup() {
        // Given
        mockCurrentUserNotInGroup();

        // When / Then
        thenThrownBy(() -> expenseService.getLivingExpenseCategories(CURRENT_USER_ID)).isInstanceOf(
            GroupMembershipRequiredException.class
        );
        verify(livingExpenseCategoryRepository, never()).findByUserGroupId(any());
    }

    // ===== getFixedExpenseCategories =====

    @Test
    void testGetFixedExpenseCategories() {
        // Given
        FixedExpenseCategory category1 = new FixedExpenseCategory(
            new FixedExpenseCategoryId(UUID.randomUUID()),
            USER_GROUP_ID,
            new CategoryName("家賃"),
            new Description("毎月の家賃"),
            new Money(80000),
            1
        );
        FixedExpenseCategory category2 = new FixedExpenseCategory(
            new FixedExpenseCategoryId(UUID.randomUUID()),
            USER_GROUP_ID,
            new CategoryName("光熱費"),
            new Description("電気・ガス・水道"),
            new Money(15000),
            1
        );
        mockCurrentUserInGroup();
        when(fixedExpenseCategoryRepository.findByUserGroupId(USER_GROUP_ID)).thenReturn(List.of(category1, category2));

        // When
        List<FixedExpenseCategory> actual = expenseService.getFixedExpenseCategories(CURRENT_USER_ID);

        // Then
        then(actual).containsExactly(category1, category2);
        verify(fixedExpenseCategoryRepository).findByUserGroupId(USER_GROUP_ID);
    }

    @Test
    void testGetFixedExpenseCategoriesWhenNotInGroup() {
        // Given
        mockCurrentUserNotInGroup();

        // When / Then
        thenThrownBy(() -> expenseService.getFixedExpenseCategories(CURRENT_USER_ID)).isInstanceOf(
            GroupMembershipRequiredException.class
        );
        verify(fixedExpenseCategoryRepository, never()).findByUserGroupId(any());
    }
}
