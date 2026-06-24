package com.takata_kento.household_expenses.presentation.usergroup;

import jakarta.validation.constraints.NotBlank;

/**
 * グループ招待リクエスト。
 *
 * @param invitedUsername 招待するユーザーのユーザー名
 */
public record InviteUserRequest(@NotBlank String invitedUsername) {}
