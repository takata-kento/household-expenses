package com.takata_kento.household_expenses.domain.dto;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.user.GroupInvitation;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.InvitationStatus;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GroupInvitationInfoTest {

    @Test
    void testFromGroupInvitation() {
        // Given
        GroupInvitationId expectedGroupInvitationId = new GroupInvitationId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        UserId invitedUserId = new UserId(UUID.randomUUID());
        UserId expectedInvitedByUserId = new UserId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();

        GroupInvitation groupInvitation = new GroupInvitation(
            expectedGroupInvitationId,
            expectedUserGroupId,
            invitedUserId,
            expectedInvitedByUserId,
            InvitationStatus.PENDING,
            now,
            null,
            now,
            now
        );

        // When
        GroupInvitationInfo actual = GroupInvitationInfo.from(groupInvitation);

        // Then
        assertThat(actual.groupInvitationId()).isEqualTo(expectedGroupInvitationId);
        assertThat(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actual.invitedByUserId()).isEqualTo(expectedInvitedByUserId);
    }
}
