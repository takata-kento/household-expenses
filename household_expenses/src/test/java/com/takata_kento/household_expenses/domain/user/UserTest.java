package com.takata_kento.household_expenses.domain.user;

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
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserTest {

    private static final UserId USER_ID_1 = new UserId(UUID.randomUUID());
    private static final UserId USER_ID_2 = new UserId(UUID.randomUUID());
    private static final UserId USER_ID_3 = new UserId(UUID.randomUUID());

    @Test
    void testUserCreation() {
        // Given
        UserId expectedUserId = USER_ID_1;
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
        UserId expectedUserId = USER_ID_1;
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
        UserId expectedUserId = USER_ID_1;
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
        UserId expectedUserId = USER_ID_1;
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
        UserId expectedUserId = USER_ID_1;
        Username expectedUsername = new Username("testuser");
        Integer expectedVersion = null;
        GroupInvitationId pendingInvitationId = new GroupInvitationId(UUID.randomUUID());
        GroupInvitationId acceptedInvitationId = new GroupInvitationId(UUID.randomUUID());

        LocalDateTime now = LocalDateTime.now();
        GroupInvitation pendingInvitation = new GroupInvitation(
            pendingInvitationId,
            new UserGroupId(100L),
            expectedUserId,
            USER_ID_2,
            InvitationStatus.PENDING,
            now,
            null,
            now,
            now
        );

        GroupInvitation acceptedInvitation = new GroupInvitation(
            acceptedInvitationId,
            new UserGroupId(200L),
            expectedUserId,
            USER_ID_3,
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
            assertThat(info.groupInvitationId()).isEqualTo(pendingInvitationId);
            assertThat(info.userGroupId()).isEqualTo(new UserGroupId(100L));
            assertThat(info.invitedByUserId()).isEqualTo(USER_ID_2);
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
        UserId expectedUserId = USER_ID_1;
        Username expectedUsername = new Username("testuser");
        Integer expectedVersion = null;
        GroupInvitationId invitationId1 = new GroupInvitationId(UUID.randomUUID());
        GroupInvitationId invitationId2 = new GroupInvitationId(UUID.randomUUID());

        LocalDateTime now = LocalDateTime.now();
        GroupInvitation invitation1 = new GroupInvitation(
            invitationId1,
            new UserGroupId(100L),
            expectedUserId,
            USER_ID_2,
            InvitationStatus.PENDING,
            now,
            null,
            now,
            now
        );

        GroupInvitation invitation2 = new GroupInvitation(
            invitationId2,
            new UserGroupId(200L),
            expectedUserId,
            USER_ID_3,
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
                        info.groupInvitationId().equals(invitationId1) &&
                        info.userGroupId().equals(new UserGroupId(100L)) &&
                        info.invitedByUserId().equals(USER_ID_2)
                )
        ).isTrue();
        assertThat(
            actual
                .stream()
                .anyMatch(
                    info ->
                        info.groupInvitationId().equals(invitationId2) &&
                        info.userGroupId().equals(new UserGroupId(200L)) &&
                        info.invitedByUserId().equals(USER_ID_3)
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
        UserId expectedUserId = USER_ID_1;
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
        UserId expectedUserId = USER_ID_1;
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
        UserId expectedUserId = USER_ID_1;
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
        UserId expectedUserId = USER_ID_1;
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
        UserId expectedUserId = USER_ID_1;
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
        UserId inviterUserId = USER_ID_1;
        Username inviterUsername = new Username("inviter");
        UserGroupId userGroupId = new UserGroupId(100L);
        UserId inviteeUserId = USER_ID_2;
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
        UserId inviterUserId = USER_ID_1;
        Username inviterUsername = new Username("inviter");
        UserId inviteeUserId = USER_ID_2;
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
        UserId inviterUserId = USER_ID_1;
        Username inviterUsername = new Username("inviter");
        UserGroupId userGroupId = new UserGroupId(100L);
        UserId inviteeUserId = USER_ID_2;
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
        UserId invitedUserId = USER_ID_1;
        Username invitedUsername = new Username("invited");
        UserGroupId expectedUserGroupId = new UserGroupId(100L);
        GroupInvitationId invitationId = new GroupInvitationId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        GroupInvitation pendingInvitation = new GroupInvitation(
            invitationId,
            expectedUserGroupId,
            invitedUserId,
            USER_ID_2,
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
        UserId invitedUserId = USER_ID_1;
        Username invitedUsername = new Username("invited");
        GroupInvitationId invitationId = new GroupInvitationId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        GroupInvitation pendingInvitation = new GroupInvitation(
            invitationId,
            new UserGroupId(100L),
            invitedUserId,
            USER_ID_2,
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
