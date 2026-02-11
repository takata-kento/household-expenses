package com.takata_kento.household_expenses.domain.usergroup;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.Day;
import com.takata_kento.household_expenses.domain.valueobject.GroupName;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.util.Optional;
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
class UserGroupRepositoryTest {

    private static final UserId TEST_USER_ID = new UserId(UUID.randomUUID());
    private static final UserGroupId TEST_USER_GROUP_ID = new UserGroupId(UUID.randomUUID());

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        // テストデータを挿入
        jdbcClient
            .sql(
                """
                INSERT INTO
                    users (id, username, password_hash, enabled, version)
                VALUES
                    (:id, :username, :password_hash, :enabled, :version)
                """
            )
            .param("id", TEST_USER_ID.toString())
            .param("username", "creator")
            .param("password_hash", "dummy_hash")
            .param("enabled", true)
            .param("version", 1)
            .update();

        jdbcClient
            .sql(
                """
                INSERT INTO
                    user_group (id, group_name, month_start_day, created_by_user_id, version)
                VALUES
                    (:id, :group_name, :month_start_day, :created_by_user_id, :version)
                """
            )
            .param("id", TEST_USER_GROUP_ID.toString())
            .param("group_name", "Test Group")
            .param("month_start_day", 10)
            .param("created_by_user_id", TEST_USER_ID.toString())
            .param("version", 1)
            .update();
    }

    @Test
    void testSave() {
        // Given
        String createdByUserId = TEST_USER_ID.toString();
        String groupName = "Test Group";
        int monthStartDay = 15;

        UserGroup userGroup = UserGroup.create(new GroupName(groupName), new Day(monthStartDay), TEST_USER_ID);

        // When
        UserGroup savedUserGroup = userGroupRepository.save(userGroup);

        // Then
        // DBから直接確認
        String groupNameFromDb = jdbcClient
            .sql("SELECT group_name FROM user_group WHERE id = ?")
            .param(savedUserGroup.id().toString())
            .query(String.class)
            .single();
        assertThat(groupNameFromDb).isEqualTo(groupName);

        Integer monthStartDayFromDb = jdbcClient
            .sql("SELECT month_start_day FROM user_group WHERE id = ?")
            .param(savedUserGroup.id().toString())
            .query(Integer.class)
            .single();
        assertThat(monthStartDayFromDb).isEqualTo(monthStartDay);

        String createdByUserIdFromDb = jdbcClient
            .sql("SELECT created_by_user_id FROM user_group WHERE id = ?")
            .param(savedUserGroup.id().toString())
            .query(String.class)
            .single();
        assertThat(createdByUserIdFromDb).isEqualTo(createdByUserId);
    }

    @Test
    void testFindById() {
        // Given
        String expectedGroupName = "Test Group";
        int expectedMonthStartDay = 10;

        // When
        Optional<UserGroup> actual = userGroupRepository.findById(TEST_USER_GROUP_ID);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(TEST_USER_GROUP_ID);
        assertThat(actual.get().name()).isEqualTo(new GroupName(expectedGroupName));
        assertThat(actual.get().monthStartDay()).isEqualTo(new Day(expectedMonthStartDay));
        assertThat(actual.get().createdByUserId()).isEqualTo(TEST_USER_ID);
    }

    @Test
    void testFindByGroupName() {
        // Given
        String expectedGroupName = "Test Group";
        GroupName groupName = new GroupName(expectedGroupName);

        // When
        Optional<UserGroup> actual = userGroupRepository.findByGroupName(groupName);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(TEST_USER_GROUP_ID);
        assertThat(actual.get().name()).isEqualTo(new GroupName(expectedGroupName));
    }

    @Test
    void testExistsByGroupName() {
        // Given
        String expectedGroupName = "Test Group";
        GroupName groupName = new GroupName(expectedGroupName);

        // When
        boolean actual = userGroupRepository.existsByGroupName(groupName);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void testUpdate() {
        // Given
        String updatedGroupName = "Updated Group";
        int updatedMonthStartDay = 28;

        // When
        UserGroup userGroup = userGroupRepository.findById(TEST_USER_GROUP_ID).orElseThrow();
        userGroup.updateGroupName(new GroupName(updatedGroupName));
        userGroup.updateMonthStartDay(new Day(updatedMonthStartDay));

        userGroupRepository.save(userGroup);

        // Then
        // DBから直接確認
        String groupNameFromDb = jdbcClient
            .sql("SELECT group_name FROM user_group WHERE id = ?")
            .param(TEST_USER_GROUP_ID.toString())
            .query(String.class)
            .single();
        assertThat(groupNameFromDb).isEqualTo(updatedGroupName);

        Integer monthStartDayFromDb = jdbcClient
            .sql("SELECT month_start_day FROM user_group WHERE id = ?")
            .param(TEST_USER_GROUP_ID.toString())
            .query(Integer.class)
            .single();
        assertThat(monthStartDayFromDb).isEqualTo(updatedMonthStartDay);

        Boolean updatedAtIsNotNull = jdbcClient
            .sql("SELECT updated_at IS NOT NULL FROM user_group WHERE id = ?")
            .param(TEST_USER_GROUP_ID.toString())
            .query(Boolean.class)
            .single();
        assertThat(updatedAtIsNotNull).isTrue();
    }
}
