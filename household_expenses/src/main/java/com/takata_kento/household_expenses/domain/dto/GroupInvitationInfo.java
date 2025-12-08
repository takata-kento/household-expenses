package com.takata_kento.household_expenses.domain.dto;

import com.takata_kento.household_expenses.domain.user.GroupInvitation;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;

public record GroupInvitationInfo(
    GroupInvitationId groupInvitationId,
    UserGroupId userGroupId,
    UserId invitedByUserId
) {
    public static GroupInvitationInfo from(GroupInvitation groupInvitation) {
        return new GroupInvitationInfo(
            groupInvitation.id(),
            groupInvitation.userGroupId(),
            groupInvitation.invitedByUserId()
        );
    }
}
