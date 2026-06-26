package com.takata_kento.household_expenses.presentation.usergroup;

import com.takata_kento.household_expenses.application.usergroup.UserGroupService;
import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.usergroup.UserGroup;
import com.takata_kento.household_expenses.domain.valueobject.Day;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.GroupName;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ユーザーグループの作成・招待・脱退・メンバー閲覧・設定変更に関するエンドポイント。
 * 現在ユーザーは {@link CognitoUserContext#currentUserId()} から取得する。
 */
@RestController
@RequestMapping("/api/user-groups")
public class UserGroupController {

    private final UserGroupService userGroupService;

    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @PostMapping
    public ResponseEntity<UserGroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        UserGroup userGroup = userGroupService.createGroup(currentUserId, new GroupName(request.groupName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserGroupResponse.from(userGroup));
    }

    @PostMapping("/invitations")
    public ResponseEntity<GroupInvitationResponse> inviteUser(@Valid @RequestBody InviteUserRequest request) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        GroupInvitationId invitationId = userGroupService.inviteUser(
            currentUserId,
            new Username(request.invitedUsername())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupInvitationResponse.from(invitationId));
    }

    @DeleteMapping("/membership")
    public ResponseEntity<Void> leaveGroup() {
        UserId currentUserId = CognitoUserContext.currentUserId();
        userGroupService.leaveGroup(currentUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members")
    public ResponseEntity<List<UserResponse>> getGroupMembers() {
        UserId currentUserId = CognitoUserContext.currentUserId();
        List<UserResponse> members = userGroupService
            .getGroupMembers(currentUserId)
            .stream()
            .map(UserResponse::from)
            .toList();
        return ResponseEntity.ok(members);
    }

    @PatchMapping("/name")
    public ResponseEntity<UserGroupResponse> updateGroupName(@Valid @RequestBody CreateGroupRequest request) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        UserGroup userGroup = userGroupService.updateGroupName(currentUserId, new GroupName(request.groupName()));
        return ResponseEntity.ok(UserGroupResponse.from(userGroup));
    }

    @PatchMapping("/month-start-day")
    public ResponseEntity<UserGroupResponse> updateMonthStartDay(
        @Valid @RequestBody UpdateMonthStartDayRequest request
    ) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        UserGroup userGroup = userGroupService.updateMonthStartDay(currentUserId, new Day(request.monthStartDay()));
        return ResponseEntity.ok(UserGroupResponse.from(userGroup));
    }
}
