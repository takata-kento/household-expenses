package com.takata_kento.household_expenses.presentation.transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.takata_kento.household_expenses.application.transaction.DailyTransactionInfo;
import com.takata_kento.household_expenses.application.transaction.LivingExpenseInput;
import com.takata_kento.household_expenses.application.transaction.PersonalExpenseInput;
import com.takata_kento.household_expenses.application.transaction.TransactionService;
import com.takata_kento.household_expenses.config.WithMockCognitoUser;
import com.takata_kento.household_expenses.domain.transaction.group.DailyLivingExpenseInfo;
import com.takata_kento.household_expenses.domain.transaction.personal.DailyPersonalExpenseInfo;
import com.takata_kento.household_expenses.domain.valueobject.DailyLivingExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.presentation.common.GlobalExceptionHandler;
import java.time.LocalDate;
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

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final UUID CATEGORY_ID = UUID.fromString("00000000-0000-0000-0000-0000000000aa");

    private DailyTransactionInfo buildInfo(LocalDate date) {
        DailyLivingExpenseInfo living = new DailyLivingExpenseInfo(
            new DailyLivingExpenseId(UUID.fromString("00000000-0000-0000-0000-0000000000b1")),
            CURRENT_USER_ID,
            new LivingExpenseCategoryId(CATEGORY_ID),
            new Money(3000),
            new Description("スーパー")
        );
        DailyPersonalExpenseInfo personal = new DailyPersonalExpenseInfo(
            new DailyPersonalExpenseId(UUID.fromString("00000000-0000-0000-0000-0000000000c1")),
            new Money(1500),
            new Description("書籍")
        );
        return new DailyTransactionInfo(
            date,
            new Money(200_000),
            List.of(living),
            List.of(personal),
            new Money(3000),
            new Money(1500),
            new Money(3000),
            new Money(47_000)
        );
    }

    @Test
    @WithMockCognitoUser
    void testRecordTransaction() throws Exception {
        // Given
        LocalDate date = LocalDate.of(2026, 6, 23);
        when(
            transactionService.recordDailyTransaction(
                eq(CURRENT_USER_ID),
                eq(date),
                eq(new Money(200_000)),
                any(),
                any()
            )
        ).thenReturn(buildInfo(date));
        String body =
            """
            {
              "transactionDate": "2026-06-23",
              "income": 200000,
              "livingExpenses": [ { "categoryId": "00000000-0000-0000-0000-0000000000aa", "amount": 3000, "memo": "スーパー" } ],
              "personalExpenses": [ { "amount": 1500, "memo": "書籍" } ]
            }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.transactionDate").value("2026-06-23"))
            .andExpect(jsonPath("$.income").value(200_000))
            .andExpect(jsonPath("$.livingExpenses.length()").value(1))
            .andExpect(jsonPath("$.livingExpenses[0].categoryId").value("00000000-0000-0000-0000-0000000000aa"))
            .andExpect(jsonPath("$.personalExpenses[0].memo").value("書籍"))
            .andExpect(jsonPath("$.totalExpense").value(3000))
            .andExpect(jsonPath("$.budgetBalance").value(47_000));
    }

    @Test
    @WithMockCognitoUser
    void testRecordTransactionWithoutExpenses() throws Exception {
        // Given
        LocalDate date = LocalDate.of(2026, 6, 23);
        when(transactionService.recordDailyTransaction(eq(CURRENT_USER_ID), eq(date), any(), any(), any())).thenReturn(
            buildInfo(date)
        );
        String body =
            """
            { "transactionDate": "2026-06-23", "income": 200000 }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated());
        verify(transactionService).recordDailyTransaction(
            eq(CURRENT_USER_ID),
            eq(date),
            eq(new Money(200_000)),
            eq(List.<LivingExpenseInput>of()),
            eq(List.<PersonalExpenseInput>of())
        );
    }

    @Test
    @WithMockCognitoUser
    void testGetTransaction() throws Exception {
        // Given
        LocalDate date = LocalDate.of(2026, 6, 23);
        when(transactionService.getDailyTransaction(CURRENT_USER_ID, date)).thenReturn(buildInfo(date));

        // When / Then
        mockMvc
            .perform(get("/api/transactions/2026-06-23"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.transactionDate").value("2026-06-23"))
            .andExpect(jsonPath("$.income").value(200_000));
    }

    @Test
    @WithMockCognitoUser
    void testUpdateTransaction() throws Exception {
        // Given
        LocalDate date = LocalDate.of(2026, 6, 23);
        when(
            transactionService.updateDailyTransaction(eq(CURRENT_USER_ID), eq(date), eq(new Money(250_000)), any(), any())
        ).thenReturn(buildInfo(date));
        String body =
            """
            {
              "income": 250000,
              "livingExpenses": [ { "categoryId": "00000000-0000-0000-0000-0000000000aa", "amount": 3000, "memo": "スーパー" } ],
              "personalExpenses": []
            }
            """;

        // When / Then
        mockMvc
            .perform(put("/api/transactions/2026-06-23").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk());
        verify(transactionService).updateDailyTransaction(eq(CURRENT_USER_ID), eq(date), eq(new Money(250_000)), any(), any());
    }

    @Test
    @WithMockCognitoUser
    void testDeleteTransaction() throws Exception {
        // Given
        LocalDate date = LocalDate.of(2026, 6, 23);

        // When / Then
        mockMvc.perform(delete("/api/transactions/2026-06-23")).andExpect(status().isNoContent());
        verify(transactionService).deleteDailyTransaction(CURRENT_USER_ID, date);
    }
}
