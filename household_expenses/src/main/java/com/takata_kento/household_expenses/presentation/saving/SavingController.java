package com.takata_kento.household_expenses.presentation.saving;

import com.takata_kento.household_expenses.application.saving.SavingService;
import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.saving.MonthlySaving;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
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
 * 貯金額の記録（月次貯金）に関するエンドポイント。貯金額は非共有データであり、
 * 操作はすべて認証済みユーザー本人の記録に限定される。現在ユーザーは
 * {@link CognitoUserContext#currentUserId()} から取得する。
 */
@RestController
@RequestMapping("/api/savings")
public class SavingController {

    private final SavingService savingService;

    public SavingController(SavingService savingService) {
        this.savingService = savingService;
    }

    @PostMapping
    public ResponseEntity<MonthlySavingResponse> recordMonthlySaving(
        @Valid @RequestBody RecordMonthlySavingRequest request
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        MonthlySaving saving = savingService.recordMonthlySaving(
            currentUserId,
            new Year(request.year()),
            new Month(request.month()),
            new Money(request.savingAmount()),
            new FinancialAccountId(request.financialAccountId()),
            Optional.ofNullable(request.memo()).map(Description::new)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(MonthlySavingResponse.from(saving));
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<MonthlySavingResponse> getMonthlySaving(@PathVariable int year, @PathVariable int month) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        MonthlySaving saving = savingService.getMonthlySaving(currentUserId, new Year(year), new Month(month));
        return ResponseEntity.ok(MonthlySavingResponse.from(saving));
    }

    @PutMapping("/{year}/{month}")
    public ResponseEntity<MonthlySavingResponse> updateMonthlySaving(
        @PathVariable int year,
        @PathVariable int month,
        @Valid @RequestBody UpdateMonthlySavingRequest request
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        MonthlySaving saving = savingService.updateMonthlySaving(
            currentUserId,
            new Year(year),
            new Month(month),
            new Money(request.savingAmount()),
            new FinancialAccountId(request.financialAccountId()),
            Optional.ofNullable(request.memo()).map(Description::new)
        );
        return ResponseEntity.ok(MonthlySavingResponse.from(saving));
    }

    @DeleteMapping("/{year}/{month}")
    public ResponseEntity<Void> deleteMonthlySaving(@PathVariable int year, @PathVariable int month) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        savingService.deleteMonthlySaving(currentUserId, new Year(year), new Month(month));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{year}")
    public ResponseEntity<List<MonthlySavingResponse>> getSavingsByYear(@PathVariable int year) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        List<MonthlySavingResponse> savings = savingService
            .getSavingsByYear(currentUserId, new Year(year))
            .stream()
            .map(MonthlySavingResponse::from)
            .toList();
        return ResponseEntity.ok(savings);
    }
}
