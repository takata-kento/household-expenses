package com.takata_kento.household_expenses.presentation.transaction;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * 日次収支の更新リクエスト。取引日はパスで指定する。
 *
 * @param income 収入
 * @param livingExpenses 生活費（共有データ、省略可）
 * @param personalExpenses 個人支出（非共有データ、省略可）
 */
public record UpdateTransactionRequest(
    @NotNull @PositiveOrZero Integer income,
    @Valid List<LivingExpenseRequest> livingExpenses,
    @Valid List<PersonalExpenseRequest> personalExpenses
) {}
