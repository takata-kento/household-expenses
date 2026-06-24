package com.takata_kento.household_expenses.presentation.account;

import com.takata_kento.household_expenses.application.account.AccountService;
import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.account.FinancialAccount;
import com.takata_kento.household_expenses.domain.valueobject.AccountName;
import com.takata_kento.household_expenses.domain.valueobject.BankName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 預金額の管理（金融口座）に関するエンドポイント。
 *
 * <p>口座情報は非共有データであり、操作はすべて認証済みユーザー本人の口座に限定される。
 * 現在ユーザーは {@link CognitoUserContext#currentUserId()} から取得する。
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        FinancialAccount account = accountService.createAccount(
            currentUserId,
            new FinancialAccountId(request.id()),
            new BankName(request.bankName()),
            Optional.ofNullable(request.accountName()).map(AccountName::new),
            new Money(request.initialBalance()),
            request.isMainAccount()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(AccountResponse.from(account));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getUserAccounts() {
        UserId currentUserId = CognitoUserContext.currentUserId();
        List<AccountResponse> accounts = accountService
            .getUserAccounts(currentUserId)
            .stream()
            .map(AccountResponse::from)
            .toList();
        return ResponseEntity.ok(accounts);
    }

    @PatchMapping("/{id}/account-name")
    public ResponseEntity<AccountResponse> updateAccountName(
        @PathVariable String id,
        @Valid @RequestBody UpdateAccountNameRequest request
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        FinancialAccount account = accountService.updateAccountName(
            currentUserId,
            new FinancialAccountId(id),
            new AccountName(request.accountName())
        );
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<AccountResponse> updateBalance(
        @PathVariable String id,
        @Valid @RequestBody UpdateBalanceRequest request
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        FinancialAccountId accountId = new FinancialAccountId(id);
        Money balance = new Money(request.balance());
        boolean hasReason = request.reason() != null && !request.reason().isBlank();
        FinancialAccount account = hasReason
            ? accountService.updateBalance(currentUserId, accountId, balance, new Description(request.reason()))
            : accountService.updateBalance(currentUserId, accountId, balance);
        return ResponseEntity.ok(AccountResponse.from(account));
    }
}
