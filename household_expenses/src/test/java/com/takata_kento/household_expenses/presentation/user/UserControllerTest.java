package com.takata_kento.household_expenses.presentation.user;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.takata_kento.household_expenses.application.user.UserService;
import com.takata_kento.household_expenses.config.WithMockCognitoUser;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.presentation.common.GlobalExceptionHandler;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final String INVITATION_ID = "00000000-0000-0000-0000-0000000000e1";

    @Test
    @WithMockCognitoUser
    void testAcceptGroupInvitation() throws Exception {
        // When / Then
        mockMvc.perform(post("/api/users/invitations/" + INVITATION_ID + "/accept")).andExpect(status().isNoContent());
        verify(userService).acceptGroupInvitation(
            CURRENT_USER_ID,
            new GroupInvitationId(UUID.fromString(INVITATION_ID))
        );
    }

    @Test
    @WithMockCognitoUser
    void testRejectGroupInvitation() throws Exception {
        // When / Then
        mockMvc.perform(post("/api/users/invitations/" + INVITATION_ID + "/reject")).andExpect(status().isNoContent());
        verify(userService).rejectGroupInvitation(
            CURRENT_USER_ID,
            new GroupInvitationId(UUID.fromString(INVITATION_ID))
        );
    }

    @Test
    @WithMockCognitoUser
    void testAcceptGroupInvitationWhenNotFoundReturnsBadRequest() throws Exception {
        // Given
        doThrow(new IllegalArgumentException("Invitation not found"))
            .when(userService)
            .acceptGroupInvitation(CURRENT_USER_ID, new GroupInvitationId(UUID.fromString(INVITATION_ID)));

        // When / Then
        mockMvc
            .perform(post("/api/users/invitations/" + INVITATION_ID + "/accept"))
            .andExpect(status().isBadRequest());
    }
}
