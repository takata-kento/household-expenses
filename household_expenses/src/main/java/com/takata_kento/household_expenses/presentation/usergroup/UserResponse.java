package com.takata_kento.household_expenses.presentation.usergroup;

import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;

/**
 * ユーザーのレスポンス表現（グループメンバー一覧などで使用）。
 *
 * @param id ユーザーID（UUID文字列）
 * @param username ユーザー名
 * @param userGroupId 所属グループID（未所属の場合は null）
 */
public record UserResponse(String id, String username, String userGroupId) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.id().toString(),
            user.name().value(),
            user.userGroupId().map(UserGroupId::toString).orElse(null)
        );
    }
}
