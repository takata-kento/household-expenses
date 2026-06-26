package com.takata_kento.household_expenses.presentation.saving;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * 月次貯金の記録リクエスト。
 *
 * @param year 年
 * @param month 月
 * @param savingAmount 貯金額
 * @param financialAccountId 貯金先口座番号（6〜8桁の数字文字列）
 * @param memo メモ（省略可）
 */
public record RecordMonthlySavingRequest(
    @NotNull Integer year,
    @NotNull Integer month,
    @NotNull @PositiveOrZero Integer savingAmount,
    @NotBlank String financialAccountId,
    String memo
) {}
