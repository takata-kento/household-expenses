package com.takata_kento.household_expenses.presentation.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * 残高更新リクエスト。{@code reason} を指定した場合は手動編集として編集理由付きで記録する。
 *
 * @param balance 新しい残高
 * @param reason 編集理由（省略可）
 */
public record UpdateBalanceRequest(@NotNull @PositiveOrZero Integer balance, String reason) {}
