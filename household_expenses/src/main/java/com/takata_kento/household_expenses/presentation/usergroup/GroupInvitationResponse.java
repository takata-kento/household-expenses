package com.takata_kento.household_expenses.presentation.usergroup;

import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;

/**
 * グループ招待作成結果のレスポンス表現。
 *
 * @param invitationId 招待ID（UUID文字列）
 */
public record GroupInvitationResponse(String invitationId) {
    public static GroupInvitationResponse from(GroupInvitationId invitationId) {
        return new GroupInvitationResponse(invitationId.toString());
    }
}
