package com.takata_kento.household_expenses.presentation.saving;

import com.takata_kento.household_expenses.domain.saving.MonthlySaving;
import com.takata_kento.household_expenses.domain.valueobject.Description;

/**
 * 月次貯金のレスポンス表現。
 *
 * @param id 貯金ID（UUID文字列）
 * @param year 年
 * @param month 月
 * @param savingAmount 貯金額
 * @param financialAccountId 貯金先口座番号
 * @param memo メモ（未設定の場合は null）
 */
public record MonthlySavingResponse(
    String id,
    int year,
    int month,
    int savingAmount,
    String financialAccountId,
    String memo
) {
    public static MonthlySavingResponse from(MonthlySaving saving) {
        return new MonthlySavingResponse(
            saving.id().toString(),
            saving.year().value(),
            saving.month().value(),
            saving.savingAmount().amount(),
            saving.financialAccountId().value(),
            saving.memo().map(Description::value).orElse(null)
        );
    }
}
