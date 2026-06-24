package com.takata_kento.household_expenses.presentation.account;

import jakarta.validation.constraints.NotBlank;

/**
 * 口座名更新リクエスト。
 *
 * @param accountName 新しい口座名
 */
public record UpdateAccountNameRequest(@NotBlank String accountName) {}
