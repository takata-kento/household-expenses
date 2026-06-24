package com.takata_kento.household_expenses.presentation.transaction;

import com.takata_kento.household_expenses.application.transaction.DailyTransactionInfo;
import com.takata_kento.household_expenses.application.transaction.LivingExpenseInput;
import com.takata_kento.household_expenses.application.transaction.PersonalExpenseInput;
import com.takata_kento.household_expenses.application.transaction.TransactionService;
import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 変動費の記録（1日の収支）に関するエンドポイント。
 *
 * <p>生活費（共有データ）と収入・個人支出（非共有データ）を2集約にまたいで記録する。
 * 現在ユーザーは {@link CognitoUserContext#currentUserId()} から取得する。
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<DailyTransactionResponse> recordTransaction(
        @Valid @RequestBody RecordTransactionRequest request
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        DailyTransactionInfo info = transactionService.recordDailyTransaction(
            currentUserId,
            request.transactionDate(),
            new Money(request.income()),
            toLivingExpenseInputs(request.livingExpenses()),
            toPersonalExpenseInputs(request.personalExpenses())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(DailyTransactionResponse.from(info));
    }

    @GetMapping("/{date}")
    public ResponseEntity<DailyTransactionResponse> getTransaction(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        DailyTransactionInfo info = transactionService.getDailyTransaction(currentUserId, date);
        return ResponseEntity.ok(DailyTransactionResponse.from(info));
    }

    @PutMapping("/{date}")
    public ResponseEntity<DailyTransactionResponse> updateTransaction(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @Valid @RequestBody UpdateTransactionRequest request
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        DailyTransactionInfo info = transactionService.updateDailyTransaction(
            currentUserId,
            date,
            new Money(request.income()),
            toLivingExpenseInputs(request.livingExpenses()),
            toPersonalExpenseInputs(request.personalExpenses())
        );
        return ResponseEntity.ok(DailyTransactionResponse.from(info));
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<Void> deleteTransaction(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        transactionService.deleteDailyTransaction(currentUserId, date);
        return ResponseEntity.noContent().build();
    }

    private List<LivingExpenseInput> toLivingExpenseInputs(List<LivingExpenseRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests
            .stream()
            .map(request ->
                new LivingExpenseInput(
                    new LivingExpenseCategoryId(UUID.fromString(request.categoryId())),
                    new Money(request.amount()),
                    new Description(request.memo())
                )
            )
            .toList();
    }

    private List<PersonalExpenseInput> toPersonalExpenseInputs(List<PersonalExpenseRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests
            .stream()
            .map(request -> new PersonalExpenseInput(new Money(request.amount()), new Description(request.memo())))
            .toList();
    }
}
