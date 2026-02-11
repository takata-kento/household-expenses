package com.takata_kento.household_expenses.domain.user;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.dto.GroupInvitationInfo;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@DataJdbcTest
@Testcontainers
@Sql("/schema.sql")
class UserRepositoryTest {

    private static final UserId TEST_USER_ID_1 = new UserId(UUID.randomUUID());
    private static final UserId TEST_USER_ID_2 = new UserId(UUID.randomUUID());
    private static final UserGroupId TEST_USER_GROUP_ID = new UserGroupId(UUID.randomUUID());

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        // テストデータを挿入
        jdbcClient
            .sql(
                """
                INSERT INTO
                    user_group (id, group_name, month_start_day, version)
                VALUES
                    (:id, :group_name, :month_start_day, :version)
                """
            )
            .param("id", TEST_USER_GROUP_ID.toString())
            .param("group_name", "Test Group")
            .param("month_start_day", 1)
            .param("version", 0)
            .update();

        jdbcClient
            .sql(
                """
                INSERT INTO
                    users (id, username, password_hash, user_group_id, enabled, version)
                VALUES
                    (:id, :username, :password_hash, :user_group_id, :enabled, :version)
                """
            )
            .param("id", TEST_USER_ID_1.toString())
            .param("username", "testuser")
            .param("password_hash", "dummy_hash")
            .param("user_group_id", TEST_USER_GROUP_ID.toString())
            .param("enabled", true)
            .param("version", 0)
            .update();

        jdbcClient
            .sql(
                """
                INSERT INTO
                    users (id, username, password_hash, enabled, version)
                VALUES
                    (:id, :username, :password_hash, :enabled, :version)
                """
            )
            .param("id", TEST_USER_ID_2.toString())
            .param("username", "testuser2")
            .param("password_hash", "dummy_hash")
            .param("enabled", true)
            .param("version", 0)
            .update();
    }

    @Test
    void testFindById() {
        // Given
        String expectedUsername = "testuser";

        // When
        Optional<User> actual = userRepository.findById(TEST_USER_ID_1);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(TEST_USER_ID_1);
        assertThat(actual.get().name()).isEqualTo(new Username(expectedUsername));
        assertThat(actual.get().isBelongsToGroup()).isTrue();
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        UserId userId = new UserId(nonExistentId);

        // When
        Optional<User> actual = userRepository.findById(userId);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void testFindByUsername() {
        // Given
        String expectedUsername = "testuser";
        Username username = new Username(expectedUsername);

        // When
        Optional<User> actual = userRepository.findByUsername(username);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(TEST_USER_ID_1);
        assertThat(actual.get().name()).isEqualTo(new Username(expectedUsername));
        assertThat(actual.get().isBelongsToGroup()).isTrue();
    }

    @Test
    void testFindByUsernameNotFound() {
        // Given
        String nonExistentUsername = "nonexistent";
        Username username = new Username(nonExistentUsername);

        // When
        Optional<User> actual = userRepository.findByUsername(username);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void testExistsByUsername() {
        // Given
        String expectedUsername = "testuser";
        Username username = new Username(expectedUsername);

        // When
        boolean actual = userRepository.existsByUsername(username);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void testExistsByUsernameNotFound() {
        // Given
        String nonExistentUsername = "doesnotexist";
        Username username = new Username(nonExistentUsername);

        // When
        boolean actual = userRepository.existsByUsername(username);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void testFindByIdWithNullUserGroupId() {
        // Given
        String expectedUsername = "testuser2";

        // When
        Optional<User> actual = userRepository.findById(TEST_USER_ID_2);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(TEST_USER_ID_2);
        assertThat(actual.get().name()).isEqualTo(new Username(expectedUsername));
        assertThat(actual.get().isBelongsToGroup()).isFalse();
    }

    @Test
    void testFindByIdWithReceivedInvitations() {
        // Given
        String expectedUsername = "testuser2";

        // 招待者とグループの設定
        String invitationId = UUID.randomUUID().toString();

        // 招待データを挿入
        jdbcClient
            .sql(
                "INSERT INTO group_invitation (id, user_group_id, invited_user_id, invited_by_user_id, status) VALUES (?, ?, ?, ?, ?)"
            )
            .params(invitationId, TEST_USER_GROUP_ID.toString(), TEST_USER_ID_2.toString(), TEST_USER_ID_1.toString(), "PENDING")
            .update();

        // When
        Optional<User> actual = userRepository.findById(TEST_USER_ID_2);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(TEST_USER_ID_2);
        assertThat(actual.get().name()).isEqualTo(new Username(expectedUsername));
        assertThat(actual.get().isBelongsToGroup()).isFalse();

        Set<GroupInvitationInfo> receivedInvitations = actual.get().receivedInvitations();
        assertThat(receivedInvitations).hasSize(1);

        GroupInvitationInfo invitation = receivedInvitations.iterator().next();
        assertThat(invitation.groupInvitationId()).isEqualTo(new GroupInvitationId(UUID.fromString(invitationId)));
        assertThat(invitation.userGroupId()).isEqualTo(TEST_USER_GROUP_ID);
        assertThat(invitation.invitedByUserId()).isEqualTo(TEST_USER_ID_1);
    }

    @Test
    void testFindByIdWithNoInvitations() {
        // When
        Optional<User> actual = userRepository.findById(TEST_USER_ID_2);

        // Then
        assertThat(actual).isPresent();
        Set<GroupInvitationInfo> receivedInvitations = actual.get().receivedInvitations();
        assertThat(receivedInvitations).isEmpty();
    }

    @Test
    void testLeaveGroupAndSave() {
        // When
        User user = userRepository.findById(TEST_USER_ID_1).orElseThrow();
        assertThat(user.isBelongsToGroup()).isTrue();

        user.leaveGroup();
        userRepository.save(user);

        // Then
        // DBから直接確認
        String userGroupIdFromDb = jdbcClient
            .sql("SELECT user_group_id FROM users WHERE id = ?")
            .param(TEST_USER_ID_1.toString())
            .query(String.class)
            .optional()
            .orElse(null);
        assertThat(userGroupIdFromDb).isNull();
    }

    @Test
    void testInviteUserAndSave() {
        // When
        User inviter = userRepository.findById(TEST_USER_ID_1).orElseThrow();
        User invitee = userRepository.findById(TEST_USER_ID_2).orElseThrow();

        // 招待前の状態確認
        assertThat(inviter.isBelongsToGroup()).isTrue();
        assertThat(invitee.isBelongsToGroup()).isFalse();
        assertThat(invitee.receivedInvitations()).isEmpty();

        // 招待実行
        GroupInvitationId invitationId = inviter.invite(invitee);

        // 招待されたユーザーの状態を永続化
        userRepository.save(invitee);

        // Then
        // DBから直接確認
        String userGroupIdFromDb = jdbcClient
            .sql("SELECT user_group_id FROM users WHERE id = ?")
            .param(TEST_USER_ID_2.toString())
            .query(String.class)
            .optional()
            .orElse(null);
        assertThat(userGroupIdFromDb).isNull();

        Integer invitationCount = jdbcClient
            .sql(
                "SELECT COUNT(*) FROM group_invitation WHERE id = ? AND invited_user_id = ? AND invited_by_user_id = ? AND user_group_id = ?"
            )
            .params(invitationId.toString(), TEST_USER_ID_2.toString(), TEST_USER_ID_1.toString(), TEST_USER_GROUP_ID.toString())
            .query(Integer.class)
            .single();
        assertThat(invitationCount).isEqualTo(1);

        String status = jdbcClient
            .sql(
                "SELECT status FROM group_invitation WHERE id = ? AND invited_user_id = ? AND invited_by_user_id = ? AND user_group_id = ?"
            )
            .params(invitationId.toString(), TEST_USER_ID_2.toString(), TEST_USER_ID_1.toString(), TEST_USER_GROUP_ID.toString())
            .query(String.class)
            .single();
        assertThat(status).isEqualTo("PENDING");
    }

    @Test
    void testAcceptInvitationAndSave() {
        // Given
        String invitationId = UUID.randomUUID().toString();

        // 招待データを挿入
        jdbcClient
            .sql(
                "INSERT INTO group_invitation (id, user_group_id, invited_user_id, invited_by_user_id, status) VALUES (?, ?, ?, ?, ?)"
            )
            .params(invitationId, TEST_USER_GROUP_ID.toString(), TEST_USER_ID_2.toString(), TEST_USER_ID_1.toString(), "PENDING")
            .update();

        // When
        User invitedUser = userRepository.findById(TEST_USER_ID_2).orElseThrow();

        // 招待承認前の状態確認
        assertThat(invitedUser.isBelongsToGroup()).isFalse();

        // 招待を承認
        invitedUser.accept(new GroupInvitationId(UUID.fromString(invitationId)));

        // 承認したユーザーの状態を永続化
        userRepository.save(invitedUser);

        // Then
        // DBから直接確認
        String userGroupIdFromDb = jdbcClient
            .sql("SELECT user_group_id FROM users WHERE id = ?")
            .param(TEST_USER_ID_2.toString())
            .query(String.class)
            .single();
        assertThat(userGroupIdFromDb).isEqualTo(TEST_USER_GROUP_ID.toString());

        String statusFromDb = jdbcClient
            .sql("SELECT status FROM group_invitation WHERE id = ?")
            .param(invitationId)
            .query(String.class)
            .single();
        assertThat(statusFromDb).isEqualTo("ACCEPTED");

        Boolean respondedAtIsNotNull = jdbcClient
            .sql("SELECT responded_at IS NOT NULL FROM group_invitation WHERE id = ?")
            .param(invitationId)
            .query(Boolean.class)
            .single();
        assertThat(respondedAtIsNotNull).isTrue();
    }

    @Test
    void testRejectInvitationAndSave() {
        // Given
        String invitationId = UUID.randomUUID().toString();

        // 招待データを挿入
        jdbcClient
            .sql(
                "INSERT INTO group_invitation (id, user_group_id, invited_user_id, invited_by_user_id, status) VALUES (?, ?, ?, ?, ?)"
            )
            .params(invitationId, TEST_USER_GROUP_ID.toString(), TEST_USER_ID_2.toString(), TEST_USER_ID_1.toString(), "PENDING")
            .update();

        // When
        User invitedUser = userRepository.findById(TEST_USER_ID_2).orElseThrow();

        // 招待拒否前の状態確認
        assertThat(invitedUser.isBelongsToGroup()).isFalse();

        // 招待を拒否
        invitedUser.reject(new GroupInvitationId(UUID.fromString(invitationId)));

        // 拒否したユーザーの状態を永続化
        userRepository.save(invitedUser);

        // Then
        // DBから直接確認
        String userGroupIdFromDb = jdbcClient
            .sql("SELECT user_group_id FROM users WHERE id = ?")
            .param(TEST_USER_ID_2.toString())
            .query(String.class)
            .optional()
            .orElse(null);
        assertThat(userGroupIdFromDb).isNull();

        String statusFromDb = jdbcClient
            .sql("SELECT status FROM group_invitation WHERE id = ?")
            .param(invitationId)
            .query(String.class)
            .single();
        assertThat(statusFromDb).isEqualTo("REJECTED");

        Boolean respondedAtIsNotNull = jdbcClient
            .sql("SELECT responded_at IS NOT NULL FROM group_invitation WHERE id = ?")
            .param(invitationId)
            .query(Boolean.class)
            .single();
        assertThat(respondedAtIsNotNull).isTrue();
    }
}
