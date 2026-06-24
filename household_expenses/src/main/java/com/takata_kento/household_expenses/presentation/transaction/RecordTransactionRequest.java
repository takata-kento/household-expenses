package com.takata_kento.household_expenses.presentation.transaction;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.List;

/**
 * 日次収支の記録リクエスト。
 *
 * @param transactionDate 取引日
 * @param income 収入
 * @param livingExpenses 生活費（共有データ、省略可）
 * @param personalExpenses 個人支出（非共有データ、省略可）
 */
public record RecordTransactionRequest(
    @NotNull LocalDate transactionDate,
    @NotNull @PositiveOrZero Integer income,
    @Valid List<LivingExpenseRequest> livingExpenses,
    @Valid List<PersonalExpenseRequest> personalExpenses
) {}
