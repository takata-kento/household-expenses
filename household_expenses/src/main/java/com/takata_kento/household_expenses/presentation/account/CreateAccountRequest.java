package com.takata_kento.household_expenses.presentation.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * 金融口座作成リクエスト。
 *
 * @param id 口座番号（6〜8桁の数字文字列）
 * @param bankName 銀行名
 * @param accountName 口座名（任意ラベル、省略可）
 * @param initialBalance 初期残高
 * @param isMainAccount メイン口座とするか
 */
public record CreateAccountRequest(
    @NotBlank String id,
    @NotBlank String bankName,
    String accountName,
    @NotNull @PositiveOrZero Integer initialBalance,
    @NotNull Boolean isMainAccount
) {}
