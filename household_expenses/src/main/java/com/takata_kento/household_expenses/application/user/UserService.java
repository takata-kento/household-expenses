package com.takata_kento.household_expenses.application.user;

import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.user.UserRepository;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CognitoUserContext cognitoUserContext;

    public UserService(UserRepository userRepository, CognitoUserContext cognitoUserContext) {
        this.userRepository = userRepository;
        this.cognitoUserContext = cognitoUserContext;
    }

    private User getCurrentUser() {
        UserId userId = cognitoUserContext.currentUserId();
        return userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalStateException("User not found: " + userId));
    }

    public void acceptGroupInvitation(GroupInvitationId invitationId) {
        User user = getCurrentUser();
        user.accept(invitationId);
        userRepository.save(user);
    }

    public void rejectGroupInvitation(GroupInvitationId invitationId) {
        User user = getCurrentUser();
        user.reject(invitationId);
        userRepository.save(user);
    }
}
