package com.takata_kento.household_expenses.presentation.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.takata_kento.household_expenses.application.account.AccountService;
import com.takata_kento.household_expenses.application.exception.ConflictException;
import com.takata_kento.household_expenses.config.WithMockCognitoUser;
import com.takata_kento.household_expenses.domain.account.FinancialAccount;
import com.takata_kento.household_expenses.domain.valueobject.AccountName;
import com.takata_kento.household_expenses.domain.valueobject.BankName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.presentation.common.GlobalExceptionHandler;
import java.util.HashSet;
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

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

    private FinancialAccount buildAccount(
        String id,
        UserId userId,
        Optional<AccountName> accountName,
        Money balance,
        Boolean isMainAccount
    ) {
        return new FinancialAccount(
            new FinancialAccountId(id),
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
    @WithMockCognitoUser
    void testCreateAccount() throws Exception {
        // Given
        FinancialAccount created = buildAccount(
            "1234567",
            CURRENT_USER_ID,
            Optional.of(new AccountName("メイン口座")),
            new Money(100_000),
            Boolean.TRUE
        );
        when(
            accountService.createAccount(
                eq(CURRENT_USER_ID),
                eq(new FinancialAccountId("1234567")),
                eq(new BankName("三菱UFJ銀行")),
                eq(Optional.of(new AccountName("メイン口座"))),
                eq(new Money(100_000)),
                eq(Boolean.TRUE)
            )
        ).thenReturn(created);
        String body = """
            {
              "id": "1234567",
              "bankName": "三菱UFJ銀行",
              "accountName": "メイン口座",
              "initialBalance": 100000,
              "isMainAccount": true
            }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("1234567"))
            .andExpect(jsonPath("$.bankName").value("三菱UFJ銀行"))
            .andExpect(jsonPath("$.accountName").value("メイン口座"))
            .andExpect(jsonPath("$.balance").value(100_000))
            .andExpect(jsonPath("$.mainAccount").value(true));
    }

    @Test
    @WithMockCognitoUser
    void testCreateAccountWithInvalidIdReturnsBadRequest() throws Exception {
        // Given
        String body = """
            {
              "id": "12",
              "bankName": "三菱UFJ銀行",
              "initialBalance": 100000,
              "isMainAccount": false
            }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isBadRequest());
        verify(accountService, never()).createAccount(any(), any(), any(), any(), any(), any());
    }

    @Test
    @WithMockCognitoUser
    void testCreateAccountWhenAlreadyRegisteredReturnsConflict() throws Exception {
        // Given
        when(accountService.createAccount(any(), any(), any(), any(), any(), any())).thenThrow(
            new ConflictException("Account is already registered: 1234567")
        );
        String body = """
            {
              "id": "1234567",
              "bankName": "三菱UFJ銀行",
              "initialBalance": 100000,
              "isMainAccount": false
            }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value("リクエストが現在のリソースの状態と競合しています。"));
    }

    @Test
    @WithMockCognitoUser
    void testGetUserAccounts() throws Exception {
        // Given
        FinancialAccount account1 = buildAccount(
            "2020202",
            CURRENT_USER_ID,
            Optional.of(new AccountName("メイン口座")),
            new Money(100_000),
            Boolean.TRUE
        );
        FinancialAccount account2 = buildAccount(
            "3030303",
            CURRENT_USER_ID,
            Optional.empty(),
            new Money(500_000),
            Boolean.FALSE
        );
        when(accountService.getUserAccounts(CURRENT_USER_ID)).thenReturn(List.of(account1, account2));

        // When / Then
        mockMvc
            .perform(get("/api/accounts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value("2020202"))
            .andExpect(jsonPath("$[1].id").value("3030303"))
            .andExpect(jsonPath("$[1].accountName").doesNotExist());
    }

    @Test
    @WithMockCognitoUser
    void testUpdateAccountName() throws Exception {
        // Given
        FinancialAccount updated = buildAccount(
            "1234567",
            CURRENT_USER_ID,
            Optional.of(new AccountName("更新後口座名")),
            new Money(100_000),
            Boolean.TRUE
        );
        when(
            accountService.updateAccountName(
                CURRENT_USER_ID,
                new FinancialAccountId("1234567"),
                new AccountName("更新後口座名")
            )
        ).thenReturn(updated);
        String body = """
            { "accountName": "更新後口座名" }
            """;

        // When / Then
        mockMvc
            .perform(patch("/api/accounts/1234567/account-name").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountName").value("更新後口座名"));
    }

    @Test
    @WithMockCognitoUser
    void testUpdateBalanceWithoutReason() throws Exception {
        // Given
        FinancialAccount updated = buildAccount(
            "4444444",
            CURRENT_USER_ID,
            Optional.of(new AccountName("口座")),
            new Money(200_000),
            Boolean.TRUE
        );
        when(
            accountService.updateBalance(CURRENT_USER_ID, new FinancialAccountId("4444444"), new Money(200_000))
        ).thenReturn(updated);
        String body = """
            { "balance": 200000 }
            """;

        // When / Then
        mockMvc
            .perform(put("/api/accounts/4444444/balance").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(200_000));
        verify(accountService).updateBalance(CURRENT_USER_ID, new FinancialAccountId("4444444"), new Money(200_000));
    }

    @Test
    @WithMockCognitoUser
    void testUpdateBalanceWithReason() throws Exception {
        // Given
        FinancialAccount updated = buildAccount(
            "5555555",
            CURRENT_USER_ID,
            Optional.of(new AccountName("口座")),
            new Money(200_000),
            Boolean.TRUE
        );
        when(
            accountService.updateBalance(
                CURRENT_USER_ID,
                new FinancialAccountId("5555555"),
                new Money(200_000),
                new Description("実残高に合わせて修正")
            )
        ).thenReturn(updated);
        String body = """
            { "balance": 200000, "reason": "実残高に合わせて修正" }
            """;

        // When / Then
        mockMvc
            .perform(put("/api/accounts/5555555/balance").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk());
        verify(accountService).updateBalance(
            CURRENT_USER_ID,
            new FinancialAccountId("5555555"),
            new Money(200_000),
            new Description("実残高に合わせて修正")
        );
    }
}
