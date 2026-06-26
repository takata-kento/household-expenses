package com.takata_kento.household_expenses.presentation.user;

import com.takata_kento.household_expenses.application.user.UserService;
import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ユーザー本人へのグループ招待の承認・拒否に関するエンドポイント。現在ユーザーは
 * {@link CognitoUserContext#currentUserId()} から取得する。
 */
@RestController
@RequestMapping("/api/users/invitations")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<Void> acceptGroupInvitation(@PathVariable String invitationId) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        userService.acceptGroupInvitation(currentUserId, new GroupInvitationId(UUID.fromString(invitationId)));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{invitationId}/reject")
    public ResponseEntity<Void> rejectGroupInvitation(@PathVariable String invitationId) {
        UserId currentUserId = CognitoUserContext.currentUserId();
        userService.rejectGroupInvitation(currentUserId, new GroupInvitationId(UUID.fromString(invitationId)));
        return ResponseEntity.noContent().build();
    }
}
