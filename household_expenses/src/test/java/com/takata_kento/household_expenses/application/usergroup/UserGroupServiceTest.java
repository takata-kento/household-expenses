package com.takata_kento.household_expenses.application.usergroup;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.user.GroupInvitationInfo;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.user.UserRepository;
import com.takata_kento.household_expenses.domain.usergroup.UserGroup;
import com.takata_kento.household_expenses.domain.usergroup.UserGroupRepository;
import com.takata_kento.household_expenses.domain.valueobject.Day;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.GroupName;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserGroupServiceTest {

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private UserRepository userRepository;

    @AutoClose
    private MockedStatic<CognitoUserContext> cognitoUserContext;

    @InjectMocks
    private UserGroupService userGroupService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final UserId OTHER_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
    private static final UserGroupId USER_GROUP_ID = new UserGroupId(
        UUID.fromString("00000000-0000-0000-0000-000000000010")
    );

    @BeforeEach
    void setUp() {
        cognitoUserContext = Mockito.mockStatic(CognitoUserContext.class);
    }

    @Test
    void testCreateGroup() {
        // Given
        GroupName groupName = new GroupName("テストグループ");
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
        UserGroup savedUserGroup = UserGroup.create(groupName, new Day(1), CURRENT_USER_ID);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userGroupRepository.save(any(UserGroup.class))).thenReturn(savedUserGroup);
        when(userRepository.save(currentUser)).thenReturn(currentUser);

        // When
        UserGroup actual = userGroupService.createGroup(groupName);

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual.name()).isEqualTo(groupName);
        assertThat(actual.createdByUserId()).isEqualTo(CURRENT_USER_ID);
        assertThat(currentUser.isBelongsToGroup()).isTrue();
        verify(userGroupRepository).save(any(UserGroup.class));
        verify(userRepository).save(currentUser);
    }

    @Test
    void testCreateGroupWhenAlreadyInGroup() {
        // Given
        GroupName groupName = new GroupName("テストグループ");
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));

        // When / Then
        assertThatThrownBy(() -> userGroupService.createGroup(groupName)).isInstanceOf(IllegalStateException.class);
        verify(userGroupRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testInviteUser() {
        // Given
        Username inviteeUsername = new Username("invitee");
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        User invitee = new User(OTHER_USER_ID, inviteeUsername, Optional.empty(), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userRepository.findByUsername(inviteeUsername)).thenReturn(Optional.of(invitee));
        when(userRepository.save(invitee)).thenReturn(invitee);

        // When
        GroupInvitationId actual = userGroupService.inviteUser(inviteeUsername);

        // Then
        assertThat(actual).isNotNull();
        assertThat(invitee.receivedInvitations()).hasSize(1);
        GroupInvitationInfo invitation = invitee.receivedInvitations().iterator().next();
        assertThat(invitation.userGroupId()).isEqualTo(USER_GROUP_ID);
        assertThat(invitation.invitedByUserId()).isEqualTo(CURRENT_USER_ID);
        verify(userRepository).save(invitee);
    }

    @Test
    void testInviteUserWhenNotInGroup() {
        // Given
        Username inviteeUsername = new Username("invitee");
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
        User invitee = new User(OTHER_USER_ID, inviteeUsername, Optional.empty(), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userRepository.findByUsername(inviteeUsername)).thenReturn(Optional.of(invitee));

        // When / Then
        assertThatThrownBy(() -> userGroupService.inviteUser(inviteeUsername)).isInstanceOf(
            IllegalStateException.class
        );
        verify(userRepository, never()).save(any());
    }

    @Test
    void testInviteUserWhenInviteeNotFound() {
        // Given
        Username nonExistentUsername = new Username("nonexistent");
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> userGroupService.inviteUser(nonExistentUsername)).isInstanceOf(
            IllegalStateException.class
        );
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLeaveGroup() {
        // Given
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userRepository.save(currentUser)).thenReturn(currentUser);

        // When
        userGroupService.leaveGroup();

        // Then
        assertThat(currentUser.isBelongsToGroup()).isFalse();
        assertThat(currentUser.userGroupId()).isEqualTo(Optional.empty());
        verify(userRepository).save(currentUser);
    }

    @Test
    void testLeaveGroupWhenNotInGroup() {
        // Given
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));

        // When / Then
        assertThatThrownBy(() -> userGroupService.leaveGroup()).isInstanceOf(IllegalStateException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetGroupMembers() {
        // Given
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        User member2 = new User(OTHER_USER_ID, new Username("member2"), Optional.of(USER_GROUP_ID), null, null);
        List<User> members = List.of(currentUser, member2);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userRepository.findByUserGroupId(USER_GROUP_ID)).thenReturn(members);

        // When
        List<User> actual = userGroupService.getGroupMembers();

        // Then
        assertThat(actual).hasSize(2);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(members);
        verify(userRepository).findByUserGroupId(USER_GROUP_ID);
    }

    @Test
    void testGetGroupMembersWhenNotInGroup() {
        // Given
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));

        // When / Then
        assertThatThrownBy(() -> userGroupService.getGroupMembers()).isInstanceOf(IllegalStateException.class);
        verify(userRepository, never()).findByUserGroupId(any());
    }

    @Test
    void testUpdateGroupName() {
        // Given
        GroupName newGroupName = new GroupName("新グループ名");
        UserGroup userGroup = UserGroup.create(new GroupName("旧グループ名"), new Day(1), CURRENT_USER_ID);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userGroupRepository.findById(USER_GROUP_ID)).thenReturn(Optional.of(userGroup));
        when(userGroupRepository.save(userGroup)).thenReturn(userGroup);

        // When
        UserGroup actual = userGroupService.updateGroupName(newGroupName);

        // Then
        assertThat(actual.name()).isEqualTo(newGroupName);
        verify(userGroupRepository).save(userGroup);
    }

    @Test
    void testUpdateGroupNameWhenNotCreator() {
        // Given
        GroupName newGroupName = new GroupName("新グループ名");
        // OTHER_USER_ID がグループ作成者、CURRENT_USER_ID は作成者でない
        UserGroup userGroup = UserGroup.create(new GroupName("旧グループ名"), new Day(1), OTHER_USER_ID);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userGroupRepository.findById(USER_GROUP_ID)).thenReturn(Optional.of(userGroup));

        // When / Then
        assertThatThrownBy(() -> userGroupService.updateGroupName(newGroupName)).isInstanceOf(
            IllegalStateException.class
        );
        verify(userGroupRepository, never()).save(any());
    }

    @Test
    void testUpdateGroupNameWhenNotInGroup() {
        // Given
        GroupName newGroupName = new GroupName("新グループ名");
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));

        // When / Then
        assertThatThrownBy(() -> userGroupService.updateGroupName(newGroupName)).isInstanceOf(
            IllegalStateException.class
        );
        verify(userGroupRepository, never()).save(any());
    }

    @Test
    void testUpdateMonthStartDay() {
        // Given
        Day newDay = new Day(25);
        UserGroup userGroup = UserGroup.create(new GroupName("テストグループ"), new Day(1), CURRENT_USER_ID);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.of(USER_GROUP_ID), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));
        when(userGroupRepository.findById(USER_GROUP_ID)).thenReturn(Optional.of(userGroup));
        when(userGroupRepository.save(userGroup)).thenReturn(userGroup);

        // When
        UserGroup actual = userGroupService.updateMonthStartDay(newDay);

        // Then
        assertThat(actual.monthStartDay()).isEqualTo(newDay);
        verify(userGroupRepository).save(userGroup);
    }

    @Test
    void testUpdateMonthStartDayWhenNotInGroup() {
        // Given
        Day newDay = new Day(25);
        User currentUser = new User(CURRENT_USER_ID, new Username("testuser"), Optional.empty(), null, null);
        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(CURRENT_USER_ID);
        when(userRepository.findById(CURRENT_USER_ID)).thenReturn(Optional.of(currentUser));

        // When / Then
        assertThatThrownBy(() -> userGroupService.updateMonthStartDay(newDay)).isInstanceOf(
            IllegalStateException.class
        );
        verify(userGroupRepository, never()).save(any());
    }
}
