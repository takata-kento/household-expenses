package com.takata_kento.household_expenses.presentation.usergroup;

import com.takata_kento.household_expenses.domain.usergroup.UserGroup;

/**
 * ユーザーグループのレスポンス表現。
 *
 * @param id グループID（UUID文字列）
 * @param groupName グループ名
 * @param monthStartDay 月の始まり日
 * @param createdByUserId 作成者ユーザーID
 */
public record UserGroupResponse(String id, String groupName, int monthStartDay, String createdByUserId) {
    public static UserGroupResponse from(UserGroup userGroup) {
        return new UserGroupResponse(
            userGroup.id().toString(),
            userGroup.name().value(),
            userGroup.monthStartDay().value(),
            userGroup.createdByUserId().toString()
        );
    }
}
