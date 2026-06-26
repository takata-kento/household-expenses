package com.takata_kento.household_expenses.presentation.usergroup;

import jakarta.validation.constraints.NotBlank;

/**
 * ユーザーグループ作成・グループ名更新リクエスト。
 *
 * @param groupName グループ名
 */
public record CreateGroupRequest(@NotBlank String groupName) {}
