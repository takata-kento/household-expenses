package com.takata_kento.household_expenses.presentation.budget;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.takata_kento.household_expenses.application.budget.BudgetService;
import com.takata_kento.household_expenses.application.budget.SetMonthlyBudgetResult;
import com.takata_kento.household_expenses.config.WithMockCognitoUser;
import com.takata_kento.household_expenses.domain.budget.MonthlyBudget;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import com.takata_kento.household_expenses.presentation.common.GlobalExceptionHandler;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BudgetController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BudgetService budgetService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final UserGroupId USER_GROUP_ID = new UserGroupId(
        UUID.fromString("00000000-0000-0000-0000-0000000000ff")
    );

    private MonthlyBudget buildBudget(int year, int month, int amount) {
        return MonthlyBudget.create(
            USER_GROUP_ID,
            new Year(year),
            new Month(month),
            new Money(amount),
            CURRENT_USER_ID
        );
    }

    @Test
    @WithMockCognitoUser
    void testSetMonthlyBudgetWhenCreatedReturnsCreated() throws Exception {
        // Given
        when(
            budgetService.setMonthlyBudget(CURRENT_USER_ID, new Year(2026), new Month(6), new Money(150_000))
        ).thenReturn(new SetMonthlyBudgetResult(buildBudget(2026, 6, 150_000), true));
        String body = """
            { "year": 2026, "month": 6, "budgetAmount": 150000 }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/budgets").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.year").value(2026))
            .andExpect(jsonPath("$.month").value(6))
            .andExpect(jsonPath("$.budgetAmount").value(150_000));
    }

    @Test
    @WithMockCognitoUser
    void testSetMonthlyBudgetWhenUpdatedReturnsOk() throws Exception {
        // Given
        when(
            budgetService.setMonthlyBudget(CURRENT_USER_ID, new Year(2026), new Month(6), new Money(150_000))
        ).thenReturn(new SetMonthlyBudgetResult(buildBudget(2026, 6, 150_000), false));
        String body = """
            { "year": 2026, "month": 6, "budgetAmount": 150000 }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/budgets").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.budgetAmount").value(150_000));
    }

    @Test
    @WithMockCognitoUser
    void testSetMonthlyBudgetWithInvalidMonthReturnsBadRequest() throws Exception {
        // Given
        String body = """
            { "year": 2026, "month": 13, "budgetAmount": 150000 }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/budgets").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCognitoUser
    void testGetMonthlyBudget() throws Exception {
        // Given
        when(budgetService.getMonthlyBudget(CURRENT_USER_ID, new Year(2026), new Month(6))).thenReturn(
            buildBudget(2026, 6, 150_000)
        );

        // When / Then
        mockMvc
            .perform(get("/api/budgets/2026/6"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.year").value(2026))
            .andExpect(jsonPath("$.budgetAmount").value(150_000));
    }

    @Test
    @WithMockCognitoUser
    void testGetMonthlyBudgetsByYear() throws Exception {
        // Given
        when(budgetService.getMonthlyBudgetsByYear(CURRENT_USER_ID, new Year(2026))).thenReturn(
            List.of(buildBudget(2026, 5, 140_000), buildBudget(2026, 6, 150_000))
        );

        // When / Then
        mockMvc
            .perform(get("/api/budgets/2026"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].month").value(5))
            .andExpect(jsonPath("$[1].month").value(6));
    }
}
