package com.takata_kento.household_expenses.presentation.expense;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.takata_kento.household_expenses.application.expense.ExpenseService;
import com.takata_kento.household_expenses.config.WithMockCognitoUser;
import com.takata_kento.household_expenses.domain.expense.category.FixedExpenseCategory;
import com.takata_kento.household_expenses.domain.expense.category.LivingExpenseCategory;
import com.takata_kento.household_expenses.domain.expense.history.FixedExpenseHistory;
import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import com.takata_kento.household_expenses.presentation.common.GlobalExceptionHandler;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExpenseController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final UserGroupId USER_GROUP_ID = new UserGroupId(
        UUID.fromString("00000000-0000-0000-0000-0000000000ff")
    );
    private static final String CATEGORY_ID = "00000000-0000-0000-0000-0000000000aa";

    @Test
    @WithMockCognitoUser
    void testCreateLivingExpenseCategory() throws Exception {
        // Given
        LivingExpenseCategory created = LivingExpenseCategory.create(
            new CategoryName("外食費"),
            new Description("外食関連費用"),
            USER_GROUP_ID
        );
        when(
            expenseService.createLivingExpenseCategory(
                CURRENT_USER_ID,
                new CategoryName("外食費"),
                new Description("外食関連費用")
            )
        ).thenReturn(created);
        String body =
            """
            { "name": "外食費", "description": "外食関連費用" }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/expenses/living-categories").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.categoryName").value("外食費"))
            .andExpect(jsonPath("$.description").value("外食関連費用"))
            .andExpect(jsonPath("$.isDefault").value(false));
    }

    @Test
    @WithMockCognitoUser
    void testUpdateLivingExpenseCategory() throws Exception {
        // Given
        LivingExpenseCategory updated = new LivingExpenseCategory(
            new LivingExpenseCategoryId(UUID.fromString(CATEGORY_ID)),
            USER_GROUP_ID,
            new CategoryName("更新後分類"),
            new Description("更新後説明"),
            false,
            null
        );
        when(
            expenseService.updateLivingExpenseCategory(
                CURRENT_USER_ID,
                new LivingExpenseCategoryId(UUID.fromString(CATEGORY_ID)),
                new CategoryName("更新後分類"),
                new Description("更新後説明")
            )
        ).thenReturn(updated);
        String body =
            """
            { "name": "更新後分類", "description": "更新後説明" }
            """;

        // When / Then
        mockMvc
            .perform(
                put("/api/expenses/living-categories/" + CATEGORY_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.categoryName").value("更新後分類"));
    }

    @Test
    @WithMockCognitoUser
    void testDeleteLivingExpenseCategory() throws Exception {
        // When / Then
        mockMvc.perform(delete("/api/expenses/living-categories/" + CATEGORY_ID)).andExpect(status().isNoContent());
        verify(expenseService).deleteLivingExpenseCategory(
            CURRENT_USER_ID,
            new LivingExpenseCategoryId(UUID.fromString(CATEGORY_ID))
        );
    }

    @Test
    @WithMockCognitoUser
    void testCreateFixedExpenseCategory() throws Exception {
        // Given
        FixedExpenseCategory created = FixedExpenseCategory.create(
            new CategoryName("家賃"),
            new Description("毎月の家賃"),
            new Money(80_000),
            USER_GROUP_ID
        );
        when(
            expenseService.createFixedExpenseCategory(
                CURRENT_USER_ID,
                new CategoryName("家賃"),
                new Description("毎月の家賃"),
                new Money(80_000)
            )
        ).thenReturn(created);
        String body =
            """
            { "name": "家賃", "description": "毎月の家賃", "defaultAmount": 80000 }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/expenses/fixed-categories").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.categoryName").value("家賃"))
            .andExpect(jsonPath("$.defaultAmount").value(80_000));
    }

    @Test
    @WithMockCognitoUser
    void testSetFixedExpenseAmount() throws Exception {
        // Given
        FixedExpenseHistory history = FixedExpenseHistory.create(
            new FixedExpenseCategoryId(UUID.fromString(CATEGORY_ID)),
            new Year(2026),
            new Month(6),
            new Money(85_000),
            LocalDate.of(2026, 6, 1),
            Optional.of(new Description("値上げ"))
        );
        when(
            expenseService.setFixedExpenseAmount(
                eq(CURRENT_USER_ID),
                eq(new FixedExpenseCategoryId(UUID.fromString(CATEGORY_ID))),
                eq(new Year(2026)),
                eq(new Month(6)),
                eq(new Money(85_000)),
                eq(LocalDate.of(2026, 6, 1)),
                eq(Optional.of(new Description("値上げ")))
            )
        ).thenReturn(history);
        String body =
            """
            { "year": 2026, "month": 6, "amount": 85000, "effectiveDate": "2026-06-01", "memo": "値上げ" }
            """;

        // When / Then
        mockMvc
            .perform(
                put("/api/expenses/fixed-categories/" + CATEGORY_ID + "/amounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.amount").value(85_000))
            .andExpect(jsonPath("$.effectiveDate").value("2026-06-01"))
            .andExpect(jsonPath("$.memo").value("値上げ"));
    }

    @Test
    @WithMockCognitoUser
    void testGetFixedExpenses() throws Exception {
        // Given
        FixedExpenseHistory history = FixedExpenseHistory.create(
            new FixedExpenseCategoryId(UUID.fromString(CATEGORY_ID)),
            new Year(2026),
            new Month(6),
            new Money(85_000),
            LocalDate.of(2026, 6, 1),
            Optional.empty()
        );
        when(expenseService.getFixedExpenses(CURRENT_USER_ID, new Year(2026), new Month(6))).thenReturn(
            List.of(history)
        );

        // When / Then
        mockMvc
            .perform(get("/api/expenses/fixed").param("year", "2026").param("month", "6"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].amount").value(85_000))
            .andExpect(jsonPath("$[0].memo").doesNotExist());
    }
}
