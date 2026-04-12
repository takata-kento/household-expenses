package com.takata_kento.household_expenses.application.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.takata_kento.household_expenses.config.CognitoUserContext;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.user.UserRepository;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @AutoClose
    private MockedStatic<CognitoUserContext> cognitoUserContext;

    @InjectMocks
    private UserService userService;

    private static final UserId USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final UserId INVITER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

    @BeforeEach
    void setUp() {
        cognitoUserContext = Mockito.mockStatic(CognitoUserContext.class);
    }

    @Test
    void testAcceptGroupInvitation() {
        // Given
        UserGroupId userGroupId = new UserGroupId(UUID.randomUUID());
        User inviter = new User(INVITER_ID, new Username("inviter"), Optional.of(userGroupId), null, null);
        User user = new User(USER_ID, new Username("testuser"), Optional.empty(), null, null);
        GroupInvitationId invitationId = inviter.invite(user);

        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(USER_ID);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // When
        userService.acceptGroupInvitation(invitationId);

        // Then
        assertThat(user.isBelongsToGroup()).isTrue();
        assertThat(user.userGroupId()).isEqualTo(Optional.of(userGroupId));
        verify(userRepository).save(user);
    }

    @Test
    void testRejectGroupInvitation() {
        // Given
        UserGroupId userGroupId = new UserGroupId(UUID.randomUUID());
        User inviter = new User(INVITER_ID, new Username("inviter"), Optional.of(userGroupId), null, null);
        User user = new User(USER_ID, new Username("testuser"), Optional.empty(), null, null);
        GroupInvitationId invitationId = inviter.invite(user);

        cognitoUserContext.when(CognitoUserContext::currentUserId).thenReturn(USER_ID);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // When
        userService.rejectGroupInvitation(invitationId);

        // Then
        assertThat(user.isBelongsToGroup()).isFalse();
        assertThat(user.userGroupId()).isEqualTo(Optional.empty());
        verify(userRepository).save(user);
    }
}
