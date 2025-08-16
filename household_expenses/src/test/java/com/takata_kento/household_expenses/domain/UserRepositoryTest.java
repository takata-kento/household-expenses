package com.takata_kento.household_expenses.domain;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.dto.GroupInvitationInfo;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@Testcontainers
class UserRepositoryTest {

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        // 関連テーブルをカスケード削除
        jdbcClient.sql("DROP TABLE IF EXISTS group_invitation CASCADE").update();
        jdbcClient.sql("DROP TABLE IF EXISTS users CASCADE").update();
        jdbcClient.sql("DROP TABLE IF EXISTS user_group CASCADE").update();

        // user_groupテーブル作成（外部キー制約なし）
        jdbcClient
            .sql(
                """
                CREATE TABLE user_group (
                    id BIGSERIAL PRIMARY KEY,
                    group_name VARCHAR(255) NOT NULL,
                    month_start_day INTEGER NOT NULL DEFAULT 1 CHECK (month_start_day >= 1 AND month_start_day <= 31),
                    created_by_user_id BIGINT,
                    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP WITH TIME ZONE,
                    version INTEGER DEFAULT 0
                )
                """
            )
            .update();

        // usersテーブル作成
        jdbcClient
            .sql(
                """
                CREATE TABLE "users" (
                    id BIGSERIAL PRIMARY KEY,
                    username VARCHAR(255) UNIQUE NOT NULL,
                    password_hash VARCHAR(255) NOT NULL,
                    user_group_id BIGINT REFERENCES user_group(id) ON DELETE SET NULL,
                    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP WITH TIME ZONE,
                    enabled boolean,
                    version INTEGER DEFAULT 0
                )
                """
            )
            .update();

        // user_groupのcreated_by_user_idに外部キー制約を追加
        jdbcClient
            .sql(
                "ALTER TABLE user_group ADD FOREIGN KEY (created_by_user_id) REFERENCES \"users\"(id) ON DELETE SET NULL"
            )
            .update();

        // group_invitationテーブル作成
        jdbcClient
            .sql(
                """
                CREATE TABLE group_invitation (
                    id BIGSERIAL PRIMARY KEY,
                    user_group_id BIGINT NOT NULL REFERENCES user_group(id) ON DELETE CASCADE,
                    invited_user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
                    invited_by_user_id BIGINT NOT NULL REFERENCES "users"(id) ON DELETE CASCADE,
                    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED')),
                    invited_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                    responded_at TIMESTAMP WITH TIME ZONE,
                    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP WITH TIME ZONE,
                    UNIQUE (user_group_id, invited_user_id)
                )
                """
            )
            .update();
    }

    @Test
    void testFindById() {
        // Given
        long expectedId = 1L;
        String expectedUsername = "testuser";
        Long expectedUserGroupId = 100L;
        UserId userId = new UserId(expectedId);

        // user_groupデータを挿入
        jdbcClient
            .sql("INSERT INTO user_group (id, group_name, month_start_day, version) VALUES (?, ?, ?, ?)")
            .params(expectedUserGroupId, "Test Group", 1, 0)
            .update();

        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(expectedId, expectedUsername, "dummy_hash", expectedUserGroupId, true, 0)
            .update();

        // When
        Optional<User> actual = userRepository.findById(userId);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(new UserId(expectedId));
        assertThat(actual.get().name()).isEqualTo(new Username(expectedUsername));
        assertThat(actual.get().isBelongsToGroup()).isTrue();
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        long nonExistentId = 999L;
        UserId userId = new UserId(nonExistentId);

        // When
        Optional<User> actual = userRepository.findById(userId);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void testFindByUsername() {
        // Given
        long expectedId = 2L;
        String expectedUsername = "finduser";
        Long expectedUserGroupId = 200L;
        Username username = new Username(expectedUsername);

        // user_groupデータを挿入
        jdbcClient
            .sql("INSERT INTO user_group (id, group_name, month_start_day, version) VALUES (?, ?, ?, ?)")
            .params(expectedUserGroupId, "Test Group 2", 1, 0)
            .update();

        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(expectedId, expectedUsername, "dummy_hash", expectedUserGroupId, true, 0)
            .update();

        // When
        Optional<User> actual = userRepository.findByUsername(username);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(new UserId(expectedId));
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
        long expectedId = 3L;
        String expectedUsername = "existsuser";
        Long expectedUserGroupId = 300L;
        Username username = new Username(expectedUsername);

        // user_groupデータを挿入
        jdbcClient
            .sql("INSERT INTO user_group (id, group_name, month_start_day, version) VALUES (?, ?, ?, ?)")
            .params(expectedUserGroupId, "Test Group 3", 1, 0)
            .update();

        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(expectedId, expectedUsername, "dummy_hash", expectedUserGroupId, true, 0)
            .update();

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
        long expectedId = 4L;
        String expectedUsername = "nullgroupuser";
        Long expectedUserGroupId = null;
        UserId userId = new UserId(expectedId);
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(expectedId, expectedUsername, "dummy_hash", expectedUserGroupId, true, 0)
            .update();

        // When
        Optional<User> actual = userRepository.findById(userId);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(new UserId(expectedId));
        assertThat(actual.get().name()).isEqualTo(new Username(expectedUsername));
        assertThat(actual.get().isBelongsToGroup()).isFalse();
    }

    @Test
    void testFindByIdWithReceivedInvitations() {
        // Given
        long expectedId = 5L;
        String expectedUsername = "inviteduser";
        Long expectedUserGroupId = null;
        UserId userId = new UserId(expectedId);

        // 招待者とグループの設定
        long inviterUserId = 6L;
        long userGroupId = 1L;
        long invitationId = 1L;

        // user_groupデータを挿入
        jdbcClient
            .sql("INSERT INTO user_group (id, group_name, month_start_day, version) VALUES (?, ?, ?, ?)")
            .params(userGroupId, "Test Group", 1, 0)
            .update();

        // 招待者ユーザーを挿入
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(inviterUserId, "inviter", "dummy_hash", userGroupId, true, 0)
            .update();

        // 招待を受けるユーザーを挿入
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(expectedId, expectedUsername, "dummy_hash", expectedUserGroupId, true, 0)
            .update();

        // 招待データを挿入
        jdbcClient
            .sql(
                "INSERT INTO group_invitation (id, user_group_id, invited_user_id, invited_by_user_id, status) VALUES (?, ?, ?, ?, ?)"
            )
            .params(invitationId, userGroupId, expectedId, inviterUserId, "PENDING")
            .update();

        // When
        Optional<User> actual = userRepository.findById(userId);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(new UserId(expectedId));
        assertThat(actual.get().name()).isEqualTo(new Username(expectedUsername));
        assertThat(actual.get().isBelongsToGroup()).isFalse();

        Set<GroupInvitationInfo> receivedInvitations = actual.get().receivedInvitations();
        assertThat(receivedInvitations).hasSize(1);

        GroupInvitationInfo invitation = receivedInvitations.iterator().next();
        assertThat(invitation.groupInvitationId()).isEqualTo(new GroupInvitationId(invitationId));
        assertThat(invitation.userGroupId()).isEqualTo(new UserGroupId(userGroupId));
        assertThat(invitation.invitedByUserId()).isEqualTo(new UserId(inviterUserId));
    }

    @Test
    void testFindByIdWithNoInvitations() {
        // Given
        long expectedId = 7L;
        String expectedUsername = "noinvitationsuser";
        Long expectedUserGroupId = null;
        UserId userId = new UserId(expectedId);

        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(expectedId, expectedUsername, "dummy_hash", expectedUserGroupId, true, 0)
            .update();

        // When
        Optional<User> actual = userRepository.findById(userId);

        // Then
        assertThat(actual).isPresent();
        Set<GroupInvitationInfo> receivedInvitations = actual.get().receivedInvitations();
        assertThat(receivedInvitations).isEmpty();
    }

    @Test
    void testLeaveGroupAndSave() {
        // Given
        long expectedId = 8L;
        String expectedUsername = "leavegroupuser";
        Long expectedUserGroupId = 400L;
        UserId userId = new UserId(expectedId);

        // user_groupデータを挿入
        jdbcClient
            .sql("INSERT INTO user_group (id, group_name, month_start_day, version) VALUES (?, ?, ?, ?)")
            .params(expectedUserGroupId, "Test Group 4", 1, 1)
            .update();

        // グループに所属しているユーザーを挿入
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(expectedId, expectedUsername, "dummy_hash", expectedUserGroupId, true, 1)
            .update();

        // When
        Optional<User> userOptional = userRepository.findById(userId);
        assertThat(userOptional).isPresent();

        User user = userOptional.get();
        assertThat(user.isBelongsToGroup()).isTrue();

        user.leaveGroup();
        userRepository.save(user);

        // Then
        Optional<User> actualUser = userRepository.findById(userId);
        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().isBelongsToGroup()).isFalse();

        // DBから直接確認
        Integer userGroupIdFromDb = jdbcClient
            .sql("SELECT user_group_id FROM users WHERE id = ?")
            .param(expectedId)
            .query(Integer.class)
            .optional()
            .orElse(null);
        assertThat(userGroupIdFromDb).isNull();
    }

    @Test
    void testInviteUserAndSave() {
        // Given
        long inviterUserId = 9L;
        long inviteeUserId = 10L;
        long userGroupId = 500L;
        String inviterUsername = "inviter";
        String inviteeUsername = "invitee";

        // user_groupデータを挿入
        jdbcClient
            .sql("INSERT INTO user_group (id, group_name, month_start_day, version) VALUES (?, ?, ?, ?)")
            .params(userGroupId, "Test Group 5", 1, 1)
            .update();

        // 招待者（グループに所属）を挿入
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(inviterUserId, inviterUsername, "dummy_hash", userGroupId, true, 1)
            .update();

        // 招待を受ける側（グループに未所属）を挿入
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(inviteeUserId, inviteeUsername, "dummy_hash", null, true, 1)
            .update();

        // When
        Optional<User> inviterOptional = userRepository.findById(new UserId(inviterUserId));
        Optional<User> inviteeOptional = userRepository.findById(new UserId(inviteeUserId));

        assertThat(inviterOptional).isPresent();
        assertThat(inviteeOptional).isPresent();

        User inviter = inviterOptional.get();
        User invitee = inviteeOptional.get();

        // 招待前の状態確認
        assertThat(inviter.isBelongsToGroup()).isTrue();
        assertThat(invitee.isBelongsToGroup()).isFalse();
        assertThat(invitee.receivedInvitations()).isEmpty();

        // 招待実行
        GroupInvitationId invitationId = inviter.invite(invitee);

        // 招待されたユーザーの状態を永続化
        userRepository.save(invitee);

        // Then
        // 永続化後の招待されたユーザーの状態確認
        Optional<User> savedInviteeOptional = userRepository.findById(new UserId(inviteeUserId));
        assertThat(savedInviteeOptional).isPresent();

        User savedInvitee = savedInviteeOptional.get();
        assertThat(savedInvitee.isBelongsToGroup()).isFalse(); // まだグループに所属していない

        Set<GroupInvitationInfo> receivedInvitations = savedInvitee.receivedInvitations();
        assertThat(receivedInvitations).hasSize(1);

        GroupInvitationInfo invitation = receivedInvitations.iterator().next();
        assertThat(invitation.userGroupId()).isEqualTo(new UserGroupId(userGroupId));
        assertThat(invitation.invitedByUserId()).isEqualTo(new UserId(inviterUserId));

        // DBから直接確認
        Integer invitationCount = jdbcClient
            .sql(
                "SELECT COUNT(*) FROM group_invitation WHERE id = ? AND invited_user_id = ? AND invited_by_user_id = ? AND user_group_id = ?"
            )
            .params(invitationId.value(), inviteeUserId, inviterUserId, userGroupId)
            .query(Integer.class)
            .single();
        assertThat(invitationCount).isEqualTo(1);

        String status = jdbcClient
            .sql(
                "SELECT status FROM group_invitation WHERE id = ? AND invited_user_id = ? AND invited_by_user_id = ? AND user_group_id = ?"
            )
            .params(invitationId.value(), inviteeUserId, inviterUserId, userGroupId)
            .query(String.class)
            .single();
        assertThat(status).isEqualTo("PENDING");
    }

    @Test
    void testAcceptInvitationAndSave() {
        // Given
        long invitedUserId = 11L;
        long inviterUserId = 12L;
        long userGroupId = 600L;
        long invitationId = 1L;
        String invitedUsername = "inviteduser";
        String inviterUsername = "inviteruser";

        // user_groupデータを挿入
        jdbcClient
            .sql("INSERT INTO user_group (id, group_name, month_start_day, version) VALUES (?, ?, ?, ?)")
            .params(userGroupId, "Test Group 6", 1, 1)
            .update();

        // 招待者（グループに所属）を挿入
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(inviterUserId, inviterUsername, "dummy_hash", userGroupId, true, 1)
            .update();

        // 招待を受けるユーザー（グループに未所属）を挿入
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(invitedUserId, invitedUsername, "dummy_hash", null, true, 1)
            .update();

        // 招待データを挿入
        jdbcClient
            .sql(
                "INSERT INTO group_invitation (id, user_group_id, invited_user_id, invited_by_user_id, status) VALUES (?, ?, ?, ?, ?)"
            )
            .params(invitationId, userGroupId, invitedUserId, inviterUserId, "PENDING")
            .update();

        // When
        Optional<User> invitedUserOptional = userRepository.findById(new UserId(invitedUserId));
        assertThat(invitedUserOptional).isPresent();

        User invitedUser = invitedUserOptional.get();

        // 招待承認前の状態確認
        assertThat(invitedUser.isBelongsToGroup()).isFalse();
        Set<GroupInvitationInfo> receivedInvitations = invitedUser.receivedInvitations();
        assertThat(receivedInvitations).hasSize(1);

        GroupInvitationInfo invitation = receivedInvitations.iterator().next();
        assertThat(invitation.groupInvitationId()).isEqualTo(new GroupInvitationId(invitationId));

        // 招待を承認
        invitedUser.accept(new GroupInvitationId(invitationId));

        // 承認したユーザーの状態を永続化
        userRepository.save(invitedUser);

        // Then
        // 永続化後のユーザーの状態確認
        Optional<User> savedInvitedUserOptional = userRepository.findById(new UserId(invitedUserId));
        assertThat(savedInvitedUserOptional).isPresent();

        User savedInvitedUser = savedInvitedUserOptional.get();
        assertThat(savedInvitedUser.isBelongsToGroup()).isTrue(); // グループに所属
        assertThat(savedInvitedUser.userGroupId()).isEqualTo(Optional.of(new UserGroupId(userGroupId)));

        // DBから直接確認
        Long userGroupIdFromDb = jdbcClient
            .sql("SELECT user_group_id FROM users WHERE id = ?")
            .param(invitedUserId)
            .query(Long.class)
            .single();
        assertThat(userGroupIdFromDb).isEqualTo(userGroupId);

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
        long invitedUserId = 13L;
        long inviterUserId = 14L;
        long userGroupId = 700L;
        long invitationId = 2L;
        String invitedUsername = "rejectuser";
        String inviterUsername = "inviteragain";

        // user_groupデータを挿入
        jdbcClient
            .sql("INSERT INTO user_group (id, group_name, month_start_day, version) VALUES (?, ?, ?, ?)")
            .params(userGroupId, "Test Group 7", 1, 1)
            .update();

        // 招待者（グループに所属）を挿入
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(inviterUserId, inviterUsername, "dummy_hash", userGroupId, true, 1)
            .update();

        // 招待を受けるユーザー（グループに未所属）を挿入
        jdbcClient
            .sql(
                "INSERT INTO users (id, username, password_hash, user_group_id, enabled, version) VALUES (?, ?, ?, ?, ?, ?)"
            )
            .params(invitedUserId, invitedUsername, "dummy_hash", null, true, 1)
            .update();

        // 招待データを挿入
        jdbcClient
            .sql(
                "INSERT INTO group_invitation (id, user_group_id, invited_user_id, invited_by_user_id, status) VALUES (?, ?, ?, ?, ?)"
            )
            .params(invitationId, userGroupId, invitedUserId, inviterUserId, "PENDING")
            .update();

        // When
        Optional<User> invitedUserOptional = userRepository.findById(new UserId(invitedUserId));
        assertThat(invitedUserOptional).isPresent();

        User invitedUser = invitedUserOptional.get();

        // 招待拒否前の状態確認
        assertThat(invitedUser.isBelongsToGroup()).isFalse();
        Set<GroupInvitationInfo> receivedInvitations = invitedUser.receivedInvitations();
        assertThat(receivedInvitations).hasSize(1);

        GroupInvitationInfo invitation = receivedInvitations.iterator().next();
        assertThat(invitation.groupInvitationId()).isEqualTo(new GroupInvitationId(invitationId));

        // 招待を拒否
        invitedUser.reject(new GroupInvitationId(invitationId));

        // 拒否したユーザーの状態を永続化
        userRepository.save(invitedUser);

        // Then
        // 永続化後のユーザーの状態確認
        Optional<User> savedInvitedUserOptional = userRepository.findById(new UserId(invitedUserId));
        assertThat(savedInvitedUserOptional).isPresent();

        User savedInvitedUser = savedInvitedUserOptional.get();
        assertThat(savedInvitedUser.isBelongsToGroup()).isFalse(); // グループに所属していない
        assertThat(savedInvitedUser.userGroupId()).isEqualTo(Optional.empty());

        // DBから直接確認
        Long userGroupIdFromDb = jdbcClient
            .sql("SELECT user_group_id FROM users WHERE id = ?")
            .param(invitedUserId)
            .query(Long.class)
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
