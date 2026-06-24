package com.takata_kento.household_expenses.presentation.usergroup;

import jakarta.validation.constraints.NotNull;

/**
 * 月の始まり日の更新リクエスト。
 *
 * @param monthStartDay 月の始まり日（1〜31）
 */
public record UpdateMonthStartDayRequest(@NotNull Integer monthStartDay) {}
