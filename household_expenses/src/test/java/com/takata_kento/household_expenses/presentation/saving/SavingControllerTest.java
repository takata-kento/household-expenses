package com.takata_kento.household_expenses.presentation.saving;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.takata_kento.household_expenses.application.saving.SavingService;
import com.takata_kento.household_expenses.config.WithMockCognitoUser;
import com.takata_kento.household_expenses.domain.saving.MonthlySaving;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import com.takata_kento.household_expenses.presentation.common.GlobalExceptionHandler;
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

@WebMvcTest(SavingController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class SavingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SavingService savingService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final FinancialAccountId ACCOUNT_ID = new FinancialAccountId("1234567");

    private MonthlySaving buildSaving(int year, int month, int amount, Optional<Description> memo) {
        return MonthlySaving.create(CURRENT_USER_ID, new Year(year), new Month(month), new Money(amount), ACCOUNT_ID, memo);
    }

    @Test
    @WithMockCognitoUser
    void testRecordMonthlySaving() throws Exception {
        // Given
        when(
            savingService.recordMonthlySaving(
                CURRENT_USER_ID,
                new Year(2026),
                new Month(6),
                new Money(30_000),
                ACCOUNT_ID,
                Optional.of(new Description("ボーナス貯金"))
            )
        ).thenReturn(buildSaving(2026, 6, 30_000, Optional.of(new Description("ボーナス貯金"))));
        String body =
            """
            { "year": 2026, "month": 6, "savingAmount": 30000, "financialAccountId": "1234567", "memo": "ボーナス貯金" }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/savings").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.savingAmount").value(30_000))
            .andExpect(jsonPath("$.financialAccountId").value("1234567"))
            .andExpect(jsonPath("$.memo").value("ボーナス貯金"));
    }

    @Test
    @WithMockCognitoUser
    void testGetMonthlySaving() throws Exception {
        // Given
        when(savingService.getMonthlySaving(CURRENT_USER_ID, new Year(2026), new Month(6))).thenReturn(
            buildSaving(2026, 6, 30_000, Optional.empty())
        );

        // When / Then
        mockMvc
            .perform(get("/api/savings/2026/6"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.savingAmount").value(30_000))
            .andExpect(jsonPath("$.memo").doesNotExist());
    }

    @Test
    @WithMockCognitoUser
    void testUpdateMonthlySaving() throws Exception {
        // Given
        when(
            savingService.updateMonthlySaving(
                CURRENT_USER_ID,
                new Year(2026),
                new Month(6),
                new Money(45_000),
                ACCOUNT_ID,
                Optional.empty()
            )
        ).thenReturn(buildSaving(2026, 6, 45_000, Optional.empty()));
        String body =
            """
            { "savingAmount": 45000, "financialAccountId": "1234567" }
            """;

        // When / Then
        mockMvc
            .perform(put("/api/savings/2026/6").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.savingAmount").value(45_000));
    }

    @Test
    @WithMockCognitoUser
    void testDeleteMonthlySaving() throws Exception {
        // When / Then
        mockMvc.perform(delete("/api/savings/2026/6")).andExpect(status().isNoContent());
        verify(savingService).deleteMonthlySaving(CURRENT_USER_ID, new Year(2026), new Month(6));
    }

    @Test
    @WithMockCognitoUser
    void testGetSavingsByYear() throws Exception {
        // Given
        when(savingService.getSavingsByYear(CURRENT_USER_ID, new Year(2026))).thenReturn(
            List.of(
                buildSaving(2026, 5, 30_000, Optional.empty()),
                buildSaving(2026, 6, 45_000, Optional.empty())
            )
        );

        // When / Then
        mockMvc
            .perform(get("/api/savings/2026"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].month").value(5))
            .andExpect(jsonPath("$[1].savingAmount").value(45_000));
    }
}
