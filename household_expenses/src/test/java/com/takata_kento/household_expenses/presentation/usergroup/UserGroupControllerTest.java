package com.takata_kento.household_expenses.presentation.usergroup;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.takata_kento.household_expenses.application.usergroup.UserGroupService;
import com.takata_kento.household_expenses.config.WithMockCognitoUser;
import com.takata_kento.household_expenses.domain.user.User;
import com.takata_kento.household_expenses.domain.usergroup.UserGroup;
import com.takata_kento.household_expenses.domain.valueobject.Day;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.GroupName;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import com.takata_kento.household_expenses.presentation.common.GlobalExceptionHandler;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserGroupController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UserGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserGroupService userGroupService;

    private static final UserId CURRENT_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    private static final UserId MEMBER_USER_ID = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
    private static final UserGroupId USER_GROUP_ID = new UserGroupId(
        UUID.fromString("00000000-0000-0000-0000-0000000000ff")
    );

    @Test
    @WithMockCognitoUser
    void testCreateGroup() throws Exception {
        // Given
        UserGroup userGroup = UserGroup.create(new GroupName("高田家"), new Day(1), CURRENT_USER_ID);
        when(userGroupService.createGroup(CURRENT_USER_ID, new GroupName("高田家"))).thenReturn(userGroup);
        String body =
            """
            { "groupName": "高田家" }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/user-groups").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.groupName").value("高田家"))
            .andExpect(jsonPath("$.monthStartDay").value(1))
            .andExpect(jsonPath("$.createdByUserId").value(CURRENT_USER_ID.toString()));
    }

    @Test
    @WithMockCognitoUser
    void testInviteUser() throws Exception {
        // Given
        GroupInvitationId invitationId = new GroupInvitationId(UUID.fromString("00000000-0000-0000-0000-0000000000e1"));
        when(userGroupService.inviteUser(CURRENT_USER_ID, new Username("partner"))).thenReturn(invitationId);
        String body =
            """
            { "invitedUsername": "partner" }
            """;

        // When / Then
        mockMvc
            .perform(post("/api/user-groups/invitations").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.invitationId").value(invitationId.toString()));
    }

    @Test
    @WithMockCognitoUser
    void testLeaveGroup() throws Exception {
        // When / Then
        mockMvc.perform(delete("/api/user-groups/membership")).andExpect(status().isNoContent());
        verify(userGroupService).leaveGroup(CURRENT_USER_ID);
    }

    @Test
    @WithMockCognitoUser
    void testGetGroupMembers() throws Exception {
        // Given
        User member1 = new User(
            CURRENT_USER_ID,
            new Username("takata"),
            Optional.of(USER_GROUP_ID),
            Set.of(),
            null
        );
        User member2 = new User(
            MEMBER_USER_ID,
            new Username("partner"),
            Optional.of(USER_GROUP_ID),
            Set.of(),
            null
        );
        when(userGroupService.getGroupMembers(CURRENT_USER_ID)).thenReturn(java.util.List.of(member1, member2));

        // When / Then
        mockMvc
            .perform(get("/api/user-groups/members"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].username").value("takata"))
            .andExpect(jsonPath("$[1].userGroupId").value(USER_GROUP_ID.toString()));
    }

    @Test
    @WithMockCognitoUser
    void testUpdateGroupName() throws Exception {
        // Given
        UserGroup userGroup = UserGroup.create(new GroupName("新グループ名"), new Day(1), CURRENT_USER_ID);
        when(userGroupService.updateGroupName(CURRENT_USER_ID, new GroupName("新グループ名"))).thenReturn(userGroup);
        String body =
            """
            { "groupName": "新グループ名" }
            """;

        // When / Then
        mockMvc
            .perform(patch("/api/user-groups/name").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.groupName").value("新グループ名"));
    }

    @Test
    @WithMockCognitoUser
    void testUpdateMonthStartDay() throws Exception {
        // Given
        UserGroup userGroup = UserGroup.create(new GroupName("高田家"), new Day(25), CURRENT_USER_ID);
        when(userGroupService.updateMonthStartDay(CURRENT_USER_ID, new Day(25))).thenReturn(userGroup);
        String body =
            """
            { "monthStartDay": 25 }
            """;

        // When / Then
        mockMvc
            .perform(patch("/api/user-groups/month-start-day").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.monthStartDay").value(25));
    }

    @Test
    @WithMockCognitoUser
    void testUpdateMonthStartDayWithInvalidValueReturnsBadRequest() throws Exception {
        // Given
        String body =
            """
            { "monthStartDay": 32 }
            """;

        // When / Then
        mockMvc
            .perform(patch("/api/user-groups/month-start-day").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isBadRequest());
    }
}
