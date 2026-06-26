package com.takata_kento.household_expenses.presentation.budget;

import com.takata_kento.household_expenses.application.budget.BudgetService;
import com.takata_kento.household_expenses.application.budget.SetMonthlyBudgetResult;
import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.budget.MonthlyBudget;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 月の予算金（生活費の予算額）に関するエンドポイント。グループ共有データであり、
 * グループの全メンバーが設定・更新・閲覧できる。現在ユーザーは
 * {@link CognitoUserContext#currentUserId()} から取得する。
 */
@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<MonthlyBudgetResponse> setMonthlyBudget(@Valid @RequestBody SetMonthlyBudgetRequest request) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        SetMonthlyBudgetResult result = budgetService.setMonthlyBudget(
            currentUserId,
            new Year(request.year()),
            new Month(request.month()),
            new Money(request.budgetAmount())
        );
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(MonthlyBudgetResponse.from(result.budget()));
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<MonthlyBudgetResponse> getMonthlyBudget(@PathVariable int year, @PathVariable int month) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        MonthlyBudget budget = budgetService.getMonthlyBudget(currentUserId, new Year(year), new Month(month));
        return ResponseEntity.ok(MonthlyBudgetResponse.from(budget));
    }

    @GetMapping("/{year}")
    public ResponseEntity<List<MonthlyBudgetResponse>> getMonthlyBudgetsByYear(@PathVariable int year) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        List<MonthlyBudgetResponse> budgets = budgetService
            .getMonthlyBudgetsByYear(currentUserId, new Year(year))
            .stream()
            .map(MonthlyBudgetResponse::from)
            .toList();
        return ResponseEntity.ok(budgets);
    }
}
