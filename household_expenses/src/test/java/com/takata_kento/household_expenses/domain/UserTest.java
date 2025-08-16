package com.takata_kento.household_expenses.domain;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.dto.GroupInvitationInfo;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.InvitationStatus;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testUserCreation() {
        // Given
        UserId expectedUserId = new UserId(1L);
        Username expectedUsername = new Username("testuser");
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        Integer expectedVersion = null;

        // When
        User actual = new User(
            expectedUserId,
            expectedUsername,
            Optional.of(expectedUserGroupId),
            null,
            expectedVersion
        );

        // Then
        assertThat(actual.id()).isEqualTo(expectedUserId);
        assertThat(actual.name()).isEqualTo(expectedUsername);
        assertThat(actual.isBelongsToGroup()).isTrue();
        assertThat(actual.userGroupId()).isEqualTo(Optional.of(expectedUserGroupId));
        assertThat(actual.receivedInvitations()).isEmpty();
        assertThat(actual.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testUserCreationWithNullUserGroup() {
        // Given
        UserId expectedUserId = new UserId(1L);
        Username expectedUsername = new Username("testuser");
        Optional<UserGroupId> expectedUserGroup = Optional.empty();
        Integer expectedVersion = null;

        // When
        User actual = new User(expectedUserId, expectedUsername, expectedUserGroup, null, expectedVersion);

        // Then
        assertThat(actual.id()).isEqualTo(expectedUserId);
        assertThat(actual.name()).isEqualTo(expectedUsername);
        assertThat(actual.isBelongsToGroup()).isFalse();
        assertThat(actual.userGroupId()).isEqualTo(Optional.empty());
        assertThat(actual.receivedInvitations()).isEmpty();
        assertThat(actual.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testId() {
        // Given
        UserId expectedUserId = new UserId(1L);
        Username expectedUsername = new Username("testuser");
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        Integer expectedVersion = null;
        User user = new User(expectedUserId, expectedUsername, Optional.of(expectedUserGroupId), null, expectedVersion);

        // When
        UserId actual = user.id();

        // Then
        assertThat(actual).isEqualTo(expectedUserId);
        assertThat(user.name()).isEqualTo(expectedUsername);
        assertThat(user.isBelongsToGroup()).isTrue();
        assertThat(user.userGroupId()).isEqualTo(Optional.of(expectedUserGroupId));
        assertThat(user.receivedInvitations()).isEmpty();
        assertThat(user.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testUsername() {
        // Given
        UserId expectedUserId = new UserId(1L);
        Username expectedUsername = new Username("testuser");
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        Integer expectedVersion = null;
        User user = new User(expectedUserId, expectedUsername, Optional.of(expectedUserGroupId), null, expectedVersion);

        // When
        Username actual = user.name();

        // Then
        assertThat(actual).isEqualTo(expectedUsername);
        assertThat(user.id()).isEqualTo(expectedUserId);
        assertThat(user.isBelongsToGroup()).isTrue();
        assertThat(user.userGroupId()).isEqualTo(Optional.of(expectedUserGroupId));
        assertThat(user.receivedInvitations()).isEmpty();
        assertThat(user.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testGetPendingInvitations() {
        // Given
        UserId expectedUserId = new UserId(1L);
        Username expectedUsername = new Username("testuser");
        Integer expectedVersion = null;

        LocalDateTime now = LocalDateTime.now();
        GroupInvitation pendingInvitation = new GroupInvitation(
            new GroupInvitationId(1L),
            new UserGroupId(100L),
            expectedUserId,
            new UserId(2L),
            InvitationStatus.PENDING,
            now,
            null,
            now,
            now
        );

        GroupInvitation acceptedInvitation = new GroupInvitation(
            new GroupInvitationId(2L),
            new UserGroupId(200L),
            expectedUserId,
            new UserId(3L),
            InvitationStatus.ACCEPTED,
            now,
            now,
            now,
            now
        );

        Set<GroupInvitation> invitations = Set.of(pendingInvitation, acceptedInvitation);
        User user = new User(expectedUserId, expectedUsername, Optional.empty(), invitations, expectedVersion);

        // When
        Set<GroupInvitationInfo> actual = user.getPendingInvitations();

        // Then
        assertThat(actual).hasSize(1);
        actual.forEach(info -> {
            assertThat(info.groupInvitationId()).isEqualTo(new GroupInvitationId(1L));
            assertThat(info.userGroupId()).isEqualTo(new UserGroupId(100L));
            assertThat(info.invitedByUserId()).isEqualTo(new UserId(2L));
        });
        assertThat(user.id()).isEqualTo(expectedUserId);
        assertThat(user.name()).isEqualTo(expectedUsername);
        assertThat(user.isBelongsToGroup()).isFalse();
        assertThat(user.userGroupId()).isEqualTo(Optional.empty());
        assertThat(user.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testReceivedInvitations() {
        // Given
        UserId expectedUserId = new UserId(1L);
        Username expectedUsername = new Username("testuser");
        Integer expectedVersion = null;

        LocalDateTime now = LocalDateTime.now();
        GroupInvitation invitation1 = new GroupInvitation(
            new GroupInvitationId(1L),
            new UserGroupId(100L),
            expectedUserId,
            new UserId(2L),
            InvitationStatus.PENDING,
            now,
            null,
            now,
            now
        );

        GroupInvitation invitation2 = new GroupInvitation(
            new GroupInvitationId(2L),
            new UserGroupId(200L),
            expectedUserId,
            new UserId(3L),
            InvitationStatus.ACCEPTED,
            now,
            now,
            now,
            now
        );

        Set<GroupInvitation> invitations = Set.of(invitation1, invitation2);
        User user = new User(expectedUserId, expectedUsername, Optional.empty(), invitations, expectedVersion);

        // When
        Set<GroupInvitationInfo> actual = user.receivedInvitations();

        // Then
        assertThat(actual).hasSize(2);
        assertThat(
            actual
                .stream()
                .anyMatch(
                    info ->
                        info.groupInvitationId().equals(new GroupInvitationId(1L)) &&
                        info.userGroupId().equals(new UserGroupId(100L)) &&
                        info.invitedByUserId().equals(new UserId(2L))
                )
        ).isTrue();
        assertThat(
            actual
                .stream()
                .anyMatch(
                    info ->
                        info.groupInvitationId().equals(new GroupInvitationId(2L)) &&
                        info.userGroupId().equals(new UserGroupId(200L)) &&
                        info.invitedByUserId().equals(new UserId(3L))
                )
        ).isTrue();
        assertThat(user.id()).isEqualTo(expectedUserId);
        assertThat(user.name()).isEqualTo(expectedUsername);
        assertThat(user.isBelongsToGroup()).isFalse();
        assertThat(user.userGroupId()).isEqualTo(Optional.empty());
        assertThat(user.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testLeaveGroup() {
        // Given
        UserId expectedUserId = new UserId(1L);
        Username expectedUsername = new Username("testuser");
        UserGroupId userGroupId = new UserGroupId(100L);
        Integer expectedVersion = null;
        User user = new User(expectedUserId, expectedUsername, Optional.of(userGroupId), null, expectedVersion);

        // When
        user.leaveGroup();

        // Then
        assertThat(user.id()).isEqualTo(expectedUserId);
        assertThat(user.name()).isEqualTo(expectedUsername);
        assertThat(user.isBelongsToGroup()).isFalse();
        assertThat(user.userGroupId()).isEqualTo(Optional.empty());
        assertThat(user.receivedInvitations()).isEmpty();
        assertThat(user.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testCanCreateGroup() {
        // Given
        UserId expectedUserId = new UserId(1L);
        Username expectedUsername = new Username("testuser");
        Integer expectedVersion = null;
        User user = new User(expectedUserId, expectedUsername, Optional.empty(), null, expectedVersion);

        // When
        boolean actual = user.canCreateGroup();

        // Then
        assertThat(actual).isTrue();
        assertThat(user.id()).isEqualTo(expectedUserId);
        assertThat(user.name()).isEqualTo(expectedUsername);
        assertThat(user.isBelongsToGroup()).isFalse();
        assertThat(user.userGroupId()).isEqualTo(Optional.empty());
        assertThat(user.receivedInvitations()).isEmpty();
        assertThat(user.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testCanCreateGroupWhenAlreadyInGroup() {
        // Given
        UserId expectedUserId = new UserId(1L);
        Username expectedUsername = new Username("testuser");
        UserGroupId userGroupId = new UserGroupId(100L);
        Integer expectedVersion = null;
        User user = new User(expectedUserId, expectedUsername, Optional.of(userGroupId), null, expectedVersion);

        // When
        boolean actual = user.canCreateGroup();

        // Then
        assertThat(actual).isFalse();
        assertThat(user.id()).isEqualTo(expectedUserId);
        assertThat(user.name()).isEqualTo(expectedUsername);
        assertThat(user.isBelongsToGroup()).isTrue();
        assertThat(user.userGroupId()).isEqualTo(Optional.of(userGroupId));
        assertThat(user.receivedInvitations()).isEmpty();
        assertThat(user.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testCanLeaveGroup() {
        // Given
        UserId expectedUserId = new UserId(1L);
        Username expectedUsername = new Username("testuser");
        UserGroupId userGroupId = new UserGroupId(100L);
        Integer expectedVersion = null;
        User user = new User(expectedUserId, expectedUsername, Optional.of(userGroupId), null, expectedVersion);

        // When
        boolean actual = user.canLeaveGroup();

        // Then
        assertThat(actual).isTrue();
        assertThat(user.id()).isEqualTo(expectedUserId);
        assertThat(user.name()).isEqualTo(expectedUsername);
        assertThat(user.isBelongsToGroup()).isTrue();
        assertThat(user.userGroupId()).isEqualTo(Optional.of(userGroupId));
        assertThat(user.receivedInvitations()).isEmpty();
        assertThat(user.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testCanLeaveGroupWhenNotInGroup() {
        // Given
        UserId expectedUserId = new UserId(1L);
        Username expectedUsername = new Username("testuser");
        Integer expectedVersion = null;
        User user = new User(expectedUserId, expectedUsername, Optional.empty(), null, expectedVersion);

        // When
        boolean actual = user.canLeaveGroup();

        // Then
        assertThat(actual).isFalse();
        assertThat(user.id()).isEqualTo(expectedUserId);
        assertThat(user.name()).isEqualTo(expectedUsername);
        assertThat(user.isBelongsToGroup()).isFalse();
        assertThat(user.userGroupId()).isEqualTo(Optional.empty());
        assertThat(user.receivedInvitations()).isEmpty();
        assertThat(user.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testCanInvite() {
        // Given
        UserId inviterUserId = new UserId(1L);
        Username inviterUsername = new Username("inviter");
        UserGroupId userGroupId = new UserGroupId(100L);
        UserId inviteeUserId = new UserId(2L);
        Integer expectedVersion = null;
        User inviter = new User(inviterUserId, inviterUsername, Optional.of(userGroupId), null, expectedVersion);

        // When
        boolean actual = inviter.canInvite(inviteeUserId);

        // Then
        assertThat(actual).isTrue();
        assertThat(inviter.id()).isEqualTo(inviterUserId);
        assertThat(inviter.name()).isEqualTo(inviterUsername);
        assertThat(inviter.isBelongsToGroup()).isTrue();
        assertThat(inviter.userGroupId()).isEqualTo(Optional.of(userGroupId));
        assertThat(inviter.receivedInvitations()).isEmpty();
        assertThat(inviter.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testCanInviteWhenNotInGroup() {
        // Given
        UserId inviterUserId = new UserId(1L);
        Username inviterUsername = new Username("inviter");
        UserId inviteeUserId = new UserId(2L);
        Integer expectedVersion = null;
        User inviter = new User(inviterUserId, inviterUsername, Optional.empty(), null, expectedVersion);

        // When
        boolean actual = inviter.canInvite(inviteeUserId);

        // Then
        assertThat(actual).isFalse();
        assertThat(inviter.id()).isEqualTo(inviterUserId);
        assertThat(inviter.name()).isEqualTo(inviterUsername);
        assertThat(inviter.isBelongsToGroup()).isFalse();
        assertThat(inviter.userGroupId()).isEqualTo(Optional.empty());
        assertThat(inviter.receivedInvitations()).isEmpty();
        assertThat(inviter.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testInvite() {
        // Given
        UserId inviterUserId = new UserId(1L);
        Username inviterUsername = new Username("inviter");
        UserGroupId userGroupId = new UserGroupId(100L);
        UserId inviteeUserId = new UserId(2L);
        Username inviteeUsername = new Username("invitee");
        User invitee = new User(inviteeUserId, inviteeUsername, Optional.empty(), null, null);
        Integer expectedVersion = null;
        User inviter = new User(inviterUserId, inviterUsername, Optional.of(userGroupId), null, expectedVersion);

        // When
        inviter.invite(invitee);

        // Then
        // 招待者（inviter）の状態確認
        assertThat(inviter.id()).isEqualTo(inviterUserId);
        assertThat(inviter.name()).isEqualTo(inviterUsername);
        assertThat(inviter.isBelongsToGroup()).isTrue();
        assertThat(inviter.userGroupId()).isEqualTo(Optional.of(userGroupId));
        assertThat(inviter.receivedInvitations()).isEmpty();
        assertThat(inviter.version()).isEqualTo(expectedVersion);

        // 招待を受けたユーザー（invitee）の状態確認
        assertThat(invitee.id()).isEqualTo(inviteeUserId);
        assertThat(invitee.name()).isEqualTo(inviteeUsername);
        assertThat(invitee.isBelongsToGroup()).isFalse();
        assertThat(invitee.userGroupId()).isEqualTo(Optional.empty());
        assertThat(invitee.receivedInvitations()).hasSize(1);
        assertThat(invitee.version()).isNull();

        // 追加された招待の詳細確認
        Set<GroupInvitationInfo> receivedInvitations = invitee.receivedInvitations();
        GroupInvitationInfo invitation = receivedInvitations.iterator().next();
        assertThat(invitation.userGroupId()).isEqualTo(userGroupId);
        assertThat(invitation.invitedByUserId()).isEqualTo(inviterUserId);
    }

    @Test
    void testAccept() {
        // Given
        UserId invitedUserId = new UserId(1L);
        Username invitedUsername = new Username("invited");
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        GroupInvitationId invitationId = new GroupInvitationId(10L);
        LocalDateTime now = LocalDateTime.now();
        GroupInvitation pendingInvitation = new GroupInvitation(
            invitationId,
            expectedUserGroupId,
            invitedUserId,
            new UserId(2L),
            InvitationStatus.PENDING,
            now,
            null,
            now,
            now
        );
        Set<GroupInvitation> invitations = Set.of(pendingInvitation);
        Integer expectedVersion = null;
        User invitedUser = new User(invitedUserId, invitedUsername, Optional.empty(), invitations, expectedVersion);

        // When
        invitedUser.accept(invitationId);

        // Then
        assertThat(invitedUser.id()).isEqualTo(invitedUserId);
        assertThat(invitedUser.name()).isEqualTo(invitedUsername);
        assertThat(invitedUser.isBelongsToGroup()).isTrue(); // 招待を受け入れてグループに所属
        assertThat(invitedUser.userGroupId()).isEqualTo(Optional.of(expectedUserGroupId)); // グループIDが設定される
        assertThat(invitedUser.receivedInvitations()).hasSize(1);
        assertThat(invitedUser.version()).isEqualTo(expectedVersion);
    }

    @Test
    void testReject() {
        // Given
        UserId invitedUserId = new UserId(1L);
        Username invitedUsername = new Username("invited");
        GroupInvitationId invitationId = new GroupInvitationId(10L);
        LocalDateTime now = LocalDateTime.now();
        GroupInvitation pendingInvitation = new GroupInvitation(
            invitationId,
            new UserGroupId(100L),
            invitedUserId,
            new UserId(2L),
            InvitationStatus.PENDING,
            now,
            null,
            now,
            now
        );
        Set<GroupInvitation> invitations = Set.of(pendingInvitation);
        Integer expectedVersion = null;
        User invitedUser = new User(invitedUserId, invitedUsername, Optional.empty(), invitations, expectedVersion);

        // When
        invitedUser.reject(invitationId);

        // Then
        assertThat(invitedUser.id()).isEqualTo(invitedUserId);
        assertThat(invitedUser.name()).isEqualTo(invitedUsername);
        assertThat(invitedUser.isBelongsToGroup()).isFalse(); // 招待を拒否したのでグループに所属しない
        assertThat(invitedUser.userGroupId()).isEqualTo(Optional.empty()); // グループIDは設定されない
        assertThat(invitedUser.receivedInvitations()).hasSize(1);
        assertThat(invitedUser.version()).isEqualTo(expectedVersion);
    }
}
