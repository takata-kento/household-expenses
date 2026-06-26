package com.takata_kento.household_expenses.presentation.expense;

import com.takata_kento.household_expenses.application.expense.ExpenseService;
import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.expense.category.FixedExpenseCategory;
import com.takata_kento.household_expenses.domain.expense.category.LivingExpenseCategory;
import com.takata_kento.household_expenses.domain.expense.history.FixedExpenseHistory;
import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 固定費・生活費分類の記録に関するエンドポイント。いずれもグループ共有データで、
 * グループメンバー全員が操作・閲覧できる。現在ユーザーは
 * {@link CognitoUserContext#currentUserId()} から取得する。
 */
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/living-categories")
    public ResponseEntity<LivingExpenseCategoryResponse> createLivingExpenseCategory(
        @Valid @RequestBody CreateLivingExpenseCategoryRequest request
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        LivingExpenseCategory category = expenseService.createLivingExpenseCategory(
            currentUserId,
            new CategoryName(request.name()),
            new Description(request.description())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(LivingExpenseCategoryResponse.from(category));
    }

    @PutMapping("/living-categories/{id}")
    public ResponseEntity<LivingExpenseCategoryResponse> updateLivingExpenseCategory(
        @PathVariable String id,
        @Valid @RequestBody CreateLivingExpenseCategoryRequest request
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        LivingExpenseCategory category = expenseService.updateLivingExpenseCategory(
            currentUserId,
            new LivingExpenseCategoryId(UUID.fromString(id)),
            new CategoryName(request.name()),
            new Description(request.description())
        );
        return ResponseEntity.ok(LivingExpenseCategoryResponse.from(category));
    }

    @DeleteMapping("/living-categories/{id}")
    public ResponseEntity<Void> deleteLivingExpenseCategory(@PathVariable String id) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        expenseService.deleteLivingExpenseCategory(currentUserId, new LivingExpenseCategoryId(UUID.fromString(id)));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/living-categories")
    public ResponseEntity<List<LivingExpenseCategoryResponse>> getLivingExpenseCategories() {
        UserId currentUserId = CognitoUserContext.currentUserId();
        List<LivingExpenseCategoryResponse> categories = expenseService
            .getLivingExpenseCategories(currentUserId)
            .stream()
            .map(LivingExpenseCategoryResponse::from)
            .toList();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/fixed-categories")
    public ResponseEntity<FixedExpenseCategoryResponse> createFixedExpenseCategory(
        @Valid @RequestBody CreateFixedExpenseCategoryRequest request
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        FixedExpenseCategory category = expenseService.createFixedExpenseCategory(
            currentUserId,
            new CategoryName(request.name()),
            new Description(request.description()),
            new Money(request.defaultAmount())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(FixedExpenseCategoryResponse.from(category));
    }

    @GetMapping("/fixed-categories")
    public ResponseEntity<List<FixedExpenseCategoryResponse>> getFixedExpenseCategories() {
        UserId currentUserId = CognitoUserContext.currentUserId();
        List<FixedExpenseCategoryResponse> categories = expenseService
            .getFixedExpenseCategories(currentUserId)
            .stream()
            .map(FixedExpenseCategoryResponse::from)
            .toList();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/fixed-categories/{categoryId}/amounts")
    public ResponseEntity<FixedExpenseHistoryResponse> setFixedExpenseAmount(
        @PathVariable String categoryId,
        @Valid @RequestBody SetFixedExpenseAmountRequest request
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        Optional<Description> memo = Optional.ofNullable(request.memo()).map(Description::new);
        FixedExpenseHistory history = expenseService.setFixedExpenseAmount(
            currentUserId,
            new FixedExpenseCategoryId(UUID.fromString(categoryId)),
            new Year(request.year()),
            new Month(request.month()),
            new Money(request.amount()),
            request.effectiveDate(),
            memo
        );
        return ResponseEntity.ok(FixedExpenseHistoryResponse.from(history));
    }

    @GetMapping("/fixed")
    public ResponseEntity<List<FixedExpenseHistoryResponse>> getFixedExpenses(
        @RequestParam int year,
        @RequestParam int month
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        List<FixedExpenseHistoryResponse> histories = expenseService
            .getFixedExpenses(currentUserId, new Year(year), new Month(month))
            .stream()
            .map(FixedExpenseHistoryResponse::from)
            .toList();
        return ResponseEntity.ok(histories);
    }
}
