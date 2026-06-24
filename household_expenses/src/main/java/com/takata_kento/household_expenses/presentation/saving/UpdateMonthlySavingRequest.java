package com.takata_kento.household_expenses.presentation.saving;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * 月次貯金の更新リクエスト。年月はパスで指定する。
 *
 * @param savingAmount 貯金額
 * @param financialAccountId 貯金先口座番号（6〜8桁の数字文字列）
 * @param memo メモ（省略可）
 */
public record UpdateMonthlySavingRequest(
    @NotNull @PositiveOrZero Integer savingAmount,
    @NotBlank String financialAccountId,
    String memo
) {}
