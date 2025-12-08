package com.takata_kento.household_expenses.domain.user;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class GroupInvitationTest {

    @Test
    void testConstructor() {
        // Given
        GroupInvitationId expectedId = new GroupInvitationId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        UserId expectedInvitedUserId = new UserId(200L);
        UserId expectedInvitedByUserId = new UserId(300L);
        InvitationStatus expectedStatus = InvitationStatus.PENDING;
        LocalDateTime expectedInvitedAt = LocalDateTime.of(2025, 8, 14, 10, 0, 0);
        LocalDateTime expectedRespondedAt = null;
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2025, 8, 14, 10, 0, 0);
        LocalDateTime expectedUpdatedAt = LocalDateTime.of(2025, 8, 14, 10, 0, 0);

        // When
        GroupInvitation actual = new GroupInvitation(
            expectedId,
            expectedUserGroupId,
            expectedInvitedUserId,
            expectedInvitedByUserId,
            expectedStatus,
            expectedInvitedAt,
            expectedRespondedAt,
            expectedCreatedAt,
            expectedUpdatedAt
        );

        // Then
        assertThat(actual.id()).isEqualTo(expectedId);
        assertThat(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actual.invitedUserId()).isEqualTo(expectedInvitedUserId);
        assertThat(actual.invitedByUserId()).isEqualTo(expectedInvitedByUserId);
        assertThat(actual.status()).isEqualTo(expectedStatus);
        assertThat(actual.invitedAt()).isEqualTo(expectedInvitedAt);
        assertThat(actual.respondedAt()).isNull();
        assertThat(actual.createdAt()).isEqualTo(expectedCreatedAt);
        assertThat(actual.updatedAt()).isEqualTo(expectedUpdatedAt);
    }

    @Test
    void testCreate() {
        // Given
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        UserId expectedInvitedUserId = new UserId(200L);
        UserId expectedInvitedByUserId = new UserId(300L);

        // When
        GroupInvitation actual = GroupInvitation.create(
            expectedUserGroupId,
            expectedInvitedUserId,
            expectedInvitedByUserId
        );

        // Then
        assertThat(actual.id()).isNotNull();
        assertThat(actual.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(actual.invitedUserId()).isEqualTo(expectedInvitedUserId);
        assertThat(actual.invitedByUserId()).isEqualTo(expectedInvitedByUserId);
        assertThat(actual.status()).isEqualTo(InvitationStatus.PENDING);
        assertThat(actual.invitedAt()).isNotNull();
        assertThat(actual.respondedAt()).isNull();
        assertThat(actual.createdAt()).isNotNull();
        assertThat(actual.updatedAt()).isNotNull();
    }

    @Test
    void testAccept() {
        // Given
        GroupInvitationId expectedId = new GroupInvitationId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        UserId expectedInvitedUserId = new UserId(200L);
        UserId expectedInvitedByUserId = new UserId(300L);
        LocalDateTime expectedInvitedAt = LocalDateTime.of(2025, 8, 14, 10, 0, 0);
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2025, 8, 14, 10, 0, 0);
        LocalDateTime expectedUpdatedAt = LocalDateTime.of(2025, 8, 14, 10, 0, 0);

        GroupInvitation invitation = new GroupInvitation(
            expectedId,
            expectedUserGroupId,
            expectedInvitedUserId,
            expectedInvitedByUserId,
            InvitationStatus.PENDING,
            expectedInvitedAt,
            null,
            expectedCreatedAt,
            expectedUpdatedAt
        );

        // When
        invitation.accept();

        // Then
        assertThat(invitation.id()).isEqualTo(expectedId);
        assertThat(invitation.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(invitation.invitedUserId()).isEqualTo(expectedInvitedUserId);
        assertThat(invitation.invitedByUserId()).isEqualTo(expectedInvitedByUserId);
        assertThat(invitation.status()).isEqualTo(InvitationStatus.ACCEPTED);
        assertThat(invitation.invitedAt()).isEqualTo(expectedInvitedAt);
        assertThat(invitation.respondedAt()).isNotNull();
        assertThat(invitation.createdAt()).isEqualTo(expectedCreatedAt);
        assertThat(invitation.updatedAt()).isNotNull();
    }

    @Test
    void testReject() {
        // Given
        GroupInvitationId expectedId = new GroupInvitationId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        UserId expectedInvitedUserId = new UserId(200L);
        UserId expectedInvitedByUserId = new UserId(300L);
        LocalDateTime expectedInvitedAt = LocalDateTime.of(2025, 8, 14, 10, 0, 0);
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2025, 8, 14, 10, 0, 0);
        LocalDateTime expectedUpdatedAt = LocalDateTime.of(2025, 8, 14, 10, 0, 0);

        GroupInvitation invitation = new GroupInvitation(
            expectedId,
            expectedUserGroupId,
            expectedInvitedUserId,
            expectedInvitedByUserId,
            InvitationStatus.PENDING,
            expectedInvitedAt,
            null,
            expectedCreatedAt,
            expectedUpdatedAt
        );

        // When
        invitation.reject();

        // Then
        assertThat(invitation.id()).isEqualTo(expectedId);
        assertThat(invitation.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(invitation.invitedUserId()).isEqualTo(expectedInvitedUserId);
        assertThat(invitation.invitedByUserId()).isEqualTo(expectedInvitedByUserId);
        assertThat(invitation.status()).isEqualTo(InvitationStatus.REJECTED);
        assertThat(invitation.invitedAt()).isEqualTo(expectedInvitedAt);
        assertThat(invitation.respondedAt()).isNotNull();
        assertThat(invitation.createdAt()).isEqualTo(expectedCreatedAt);
        assertThat(invitation.updatedAt()).isNotNull();
    }

    @Test
    void testIsPending() {
        // Given
        GroupInvitationId expectedId1 = new GroupInvitationId(1L);
        GroupInvitationId expectedId2 = new GroupInvitationId(2L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        UserId expectedInvitedUserId = new UserId(200L);
        UserId expectedInvitedByUserId = new UserId(300L);
        LocalDateTime expectedDateTime = LocalDateTime.of(2025, 8, 14, 10, 0, 0);

        GroupInvitation pendingInvitation = new GroupInvitation(
            expectedId1,
            expectedUserGroupId,
            expectedInvitedUserId,
            expectedInvitedByUserId,
            InvitationStatus.PENDING,
            expectedDateTime,
            null,
            expectedDateTime,
            expectedDateTime
        );

        GroupInvitation acceptedInvitation = new GroupInvitation(
            expectedId2,
            expectedUserGroupId,
            expectedInvitedUserId,
            expectedInvitedByUserId,
            InvitationStatus.ACCEPTED,
            expectedDateTime,
            expectedDateTime,
            expectedDateTime,
            expectedDateTime
        );

        // When / Then
        assertThat(pendingInvitation.isPending()).isTrue();
        assertThat(pendingInvitation.id()).isEqualTo(expectedId1);
        assertThat(pendingInvitation.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(pendingInvitation.invitedUserId()).isEqualTo(expectedInvitedUserId);
        assertThat(pendingInvitation.invitedByUserId()).isEqualTo(expectedInvitedByUserId);
        assertThat(pendingInvitation.status()).isEqualTo(InvitationStatus.PENDING);
        assertThat(pendingInvitation.invitedAt()).isEqualTo(expectedDateTime);
        assertThat(pendingInvitation.respondedAt()).isNull();
        assertThat(pendingInvitation.createdAt()).isEqualTo(expectedDateTime);
        assertThat(pendingInvitation.updatedAt()).isEqualTo(expectedDateTime);

        assertThat(acceptedInvitation.isPending()).isFalse();
        assertThat(acceptedInvitation.id()).isEqualTo(expectedId2);
        assertThat(acceptedInvitation.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(acceptedInvitation.invitedUserId()).isEqualTo(expectedInvitedUserId);
        assertThat(acceptedInvitation.invitedByUserId()).isEqualTo(expectedInvitedByUserId);
        assertThat(acceptedInvitation.status()).isEqualTo(InvitationStatus.ACCEPTED);
        assertThat(acceptedInvitation.invitedAt()).isEqualTo(expectedDateTime);
        assertThat(acceptedInvitation.respondedAt()).isEqualTo(expectedDateTime);
        assertThat(acceptedInvitation.createdAt()).isEqualTo(expectedDateTime);
        assertThat(acceptedInvitation.updatedAt()).isEqualTo(expectedDateTime);
    }

    @Test
    void testCanRespond() {
        // Given
        GroupInvitationId expectedId1 = new GroupInvitationId(1L);
        GroupInvitationId expectedId2 = new GroupInvitationId(2L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        UserId expectedInvitedUserId = new UserId(200L);
        UserId expectedInvitedByUserId = new UserId(300L);
        LocalDateTime expectedDateTime = LocalDateTime.of(2025, 8, 14, 10, 0, 0);

        GroupInvitation pendingInvitation = new GroupInvitation(
            expectedId1,
            expectedUserGroupId,
            expectedInvitedUserId,
            expectedInvitedByUserId,
            InvitationStatus.PENDING,
            expectedDateTime,
            null,
            expectedDateTime,
            expectedDateTime
        );

        GroupInvitation acceptedInvitation = new GroupInvitation(
            expectedId2,
            expectedUserGroupId,
            expectedInvitedUserId,
            expectedInvitedByUserId,
            InvitationStatus.ACCEPTED,
            expectedDateTime,
            expectedDateTime,
            expectedDateTime,
            expectedDateTime
        );

        // When / Then
        assertThat(pendingInvitation.canRespond()).isTrue();
        assertThat(pendingInvitation.id()).isEqualTo(expectedId1);
        assertThat(pendingInvitation.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(pendingInvitation.invitedUserId()).isEqualTo(expectedInvitedUserId);
        assertThat(pendingInvitation.invitedByUserId()).isEqualTo(expectedInvitedByUserId);
        assertThat(pendingInvitation.status()).isEqualTo(InvitationStatus.PENDING);
        assertThat(pendingInvitation.invitedAt()).isEqualTo(expectedDateTime);
        assertThat(pendingInvitation.respondedAt()).isNull();
        assertThat(pendingInvitation.createdAt()).isEqualTo(expectedDateTime);
        assertThat(pendingInvitation.updatedAt()).isEqualTo(expectedDateTime);

        assertThat(acceptedInvitation.canRespond()).isFalse();
        assertThat(acceptedInvitation.id()).isEqualTo(expectedId2);
        assertThat(acceptedInvitation.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(acceptedInvitation.invitedUserId()).isEqualTo(expectedInvitedUserId);
        assertThat(acceptedInvitation.invitedByUserId()).isEqualTo(expectedInvitedByUserId);
        assertThat(acceptedInvitation.status()).isEqualTo(InvitationStatus.ACCEPTED);
        assertThat(acceptedInvitation.invitedAt()).isEqualTo(expectedDateTime);
        assertThat(acceptedInvitation.respondedAt()).isEqualTo(expectedDateTime);
        assertThat(acceptedInvitation.createdAt()).isEqualTo(expectedDateTime);
        assertThat(acceptedInvitation.updatedAt()).isEqualTo(expectedDateTime);
    }

    @Test
    void testIsFrom() {
        // Given
        GroupInvitationId expectedId = new GroupInvitationId(1L);
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        UserGroupId differentUserGroupId = new UserGroupId(200L);
        UserId expectedInvitedUserId = new UserId(200L);
        UserId expectedInvitedByUserId = new UserId(300L);
        LocalDateTime expectedDateTime = LocalDateTime.of(2025, 8, 14, 10, 0, 0);

        GroupInvitation invitation = new GroupInvitation(
            expectedId,
            expectedUserGroupId,
            expectedInvitedUserId,
            expectedInvitedByUserId,
            InvitationStatus.PENDING,
            expectedDateTime,
            null,
            expectedDateTime,
            expectedDateTime
        );

        // When / Then
        assertThat(invitation.isFrom(expectedUserGroupId)).isTrue();
        assertThat(invitation.isFrom(differentUserGroupId)).isFalse();
        assertThat(invitation.id()).isEqualTo(expectedId);
        assertThat(invitation.userGroupId()).isEqualTo(expectedUserGroupId);
        assertThat(invitation.invitedUserId()).isEqualTo(expectedInvitedUserId);
        assertThat(invitation.invitedByUserId()).isEqualTo(expectedInvitedByUserId);
        assertThat(invitation.status()).isEqualTo(InvitationStatus.PENDING);
        assertThat(invitation.invitedAt()).isEqualTo(expectedDateTime);
        assertThat(invitation.respondedAt()).isNull();
        assertThat(invitation.createdAt()).isEqualTo(expectedDateTime);
        assertThat(invitation.updatedAt()).isEqualTo(expectedDateTime);
    }
}
