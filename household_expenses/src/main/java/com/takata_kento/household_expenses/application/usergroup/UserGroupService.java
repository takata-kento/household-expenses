package com.takata_kento.household_expenses.application.usergroup;

import com.takata_kento.household_expenses.config.CognitoUserContext;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final CognitoUserContext cognitoUserContext;

    public UserGroupService(
        UserGroupRepository userGroupRepository,
        UserRepository userRepository,
        CognitoUserContext cognitoUserContext
    ) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
        this.cognitoUserContext = cognitoUserContext;
    }

    private User getCurrentUser() {
        UserId userId = cognitoUserContext.currentUserId();
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalStateException("User not found: " + userId));
    }

    public UserGroup createGroup(GroupName groupName) {
        User currentUser = getCurrentUser();
        if (!currentUser.canCreateGroup()) {
            throw new IllegalStateException("User already belongs to a group");
        }
        UserGroup userGroup = UserGroup.create(groupName, new Day(1), currentUser.id());
        UserGroup savedUserGroup = userGroupRepository.save(userGroup);
        currentUser.joinGroup(savedUserGroup.id());
        userRepository.save(currentUser);
        return savedUserGroup;
    }

    public GroupInvitationId inviteUser(Username username) {
        User currentUser = getCurrentUser();
        User invitee = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalStateException("User not found: " + username));
        GroupInvitationId invitationId = currentUser.invite(invitee);
        userRepository.save(invitee);
        return invitationId;
    }

    public void leaveGroup() {
        User currentUser = getCurrentUser();
        if (!currentUser.canLeaveGroup()) {
            throw new IllegalStateException("User does not belong to any group");
        }
        currentUser.leaveGroup();
        userRepository.save(currentUser);
    }

    public List<User> getGroupMembers() {
        User currentUser = getCurrentUser();
        UserGroupId userGroupId = currentUser.userGroupId()
            .orElseThrow(() -> new IllegalStateException("User does not belong to any group"));
        return userRepository.findByUserGroupId(userGroupId);
    }

    public UserGroup updateGroupName(GroupName groupName) {
        User currentUser = getCurrentUser();
        UserGroupId userGroupId = currentUser.userGroupId()
            .orElseThrow(() -> new IllegalStateException("User does not belong to any group"));
        UserGroup userGroup = userGroupRepository.findById(userGroupId)
            .orElseThrow(() -> new IllegalStateException("UserGroup not found: " + userGroupId));
        if (!userGroup.canBeModifiedBy(currentUser.id())) {
            throw new IllegalStateException("Only the group creator can update the group name");
        }
        userGroup.updateGroupName(groupName);
        return userGroupRepository.save(userGroup);
    }

    public UserGroup updateMonthStartDay(Day day) {
        User currentUser = getCurrentUser();
        UserGroupId userGroupId = currentUser.userGroupId()
            .orElseThrow(() -> new IllegalStateException("User does not belong to any group"));
        UserGroup userGroup = userGroupRepository.findById(userGroupId)
            .orElseThrow(() -> new IllegalStateException("UserGroup not found: " + userGroupId));
        userGroup.updateMonthStartDay(day);
        return userGroupRepository.save(userGroup);
    }
}
