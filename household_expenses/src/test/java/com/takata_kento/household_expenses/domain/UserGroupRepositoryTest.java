package com.takata_kento.household_expenses.domain;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.Day;
import com.takata_kento.household_expenses.domain.valueobject.GroupName;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import java.util.Optional;
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
class UserGroupRepositoryTest {

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
    private UserGroupRepository userGroupRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        // 関連テーブルをカスケード削除
        jdbcClient.sql("DROP TABLE IF EXISTS group_invitation CASCADE").update();
        jdbcClient.sql("DROP TABLE IF EXISTS users CASCADE").update();
        jdbcClient.sql("DROP TABLE IF EXISTS user_group CASCADE").update();

        // user_groupテーブル作成
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

        // usersテーブル作成（外部キー制約のため）
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

        // 作成者ユーザーを挿入
        jdbcClient
            .sql(
                """
                INSERT INTO
                    users (id, username, password_hash, enabled, version)
                VALUES
                    (:id, :username, :password_hash, :enabled, :version)
                """
            )
            .param("id", 1L)
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
            .param("id", 1L)
            .param("group_name", "Test Group")
            .param("month_start_day", 10)
            .param("created_by_user_id", 1L)
            .param("version", 1)
            .update();
    }

    @Test
    void testSave() {
        // Given
        long createdByUserId = 1L;
        String groupName = "Test Group";
        int monthStartDay = 15;

        UserGroup userGroup = UserGroup.create(
            new GroupName(groupName),
            new Day(monthStartDay),
            new UserId(createdByUserId)
        );

        // When
        UserGroup savedUserGroup = userGroupRepository.save(userGroup);

        // Then
        // DBから直接確認
        String groupNameFromDb = jdbcClient
            .sql("SELECT group_name FROM user_group WHERE id = ?")
            .param(savedUserGroup.id().value())
            .query(String.class)
            .single();
        assertThat(groupNameFromDb).isEqualTo(groupName);

        Integer monthStartDayFromDb = jdbcClient
            .sql("SELECT month_start_day FROM user_group WHERE id = ?")
            .param(savedUserGroup.id().value())
            .query(Integer.class)
            .single();
        assertThat(monthStartDayFromDb).isEqualTo(monthStartDay);

        Long createdByUserIdFromDb = jdbcClient
            .sql("SELECT created_by_user_id FROM user_group WHERE id = ?")
            .param(savedUserGroup.id().value())
            .query(Long.class)
            .single();
        assertThat(createdByUserIdFromDb).isEqualTo(createdByUserId);
    }

    @Test
    void testFindById() {
        // Given
        long expectedId = 1L;
        String expectedGroupName = "Test Group";
        int expectedMonthStartDay = 10;
        long expectedCreatedByUserId = 1L;
        UserGroupId userGroupId = new UserGroupId(expectedId);

        // When
        Optional<UserGroup> actual = userGroupRepository.findById(userGroupId);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(new UserGroupId(expectedId));
        assertThat(actual.get().name()).isEqualTo(new GroupName(expectedGroupName));
        assertThat(actual.get().monthStartDay()).isEqualTo(new Day(expectedMonthStartDay));
        assertThat(actual.get().createdByUserId()).isEqualTo(new UserId(expectedCreatedByUserId));
    }

    @Test
    void testFindByGroupName() {
        // Given
        long expectedId = 1L;
        String expectedGroupName = "Test Group";
        GroupName groupName = new GroupName(expectedGroupName);

        // When
        Optional<UserGroup> actual = userGroupRepository.findByGroupName(groupName);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(new UserGroupId(expectedId));
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
        long userGroupId = 1L;
        String updatedGroupName = "Updated Group";
        int updatedMonthStartDay = 28;

        // When
        UserGroup userGroup = userGroupRepository.findById(new UserGroupId(userGroupId)).orElseThrow();
        userGroup.updateGroupName(new GroupName(updatedGroupName));
        userGroup.updateMonthStartDay(new Day(updatedMonthStartDay));

        userGroupRepository.save(userGroup);

        // Then
        // DBから直接確認
        String groupNameFromDb = jdbcClient
            .sql("SELECT group_name FROM user_group WHERE id = ?")
            .param(userGroupId)
            .query(String.class)
            .single();
        assertThat(groupNameFromDb).isEqualTo(updatedGroupName);

        Integer monthStartDayFromDb = jdbcClient
            .sql("SELECT month_start_day FROM user_group WHERE id = ?")
            .param(userGroupId)
            .query(Integer.class)
            .single();
        assertThat(monthStartDayFromDb).isEqualTo(updatedMonthStartDay);

        Boolean updatedAtIsNotNull = jdbcClient
            .sql("SELECT updated_at IS NOT NULL FROM user_group WHERE id = ?")
            .param(userGroupId)
            .query(Boolean.class)
            .single();
        assertThat(updatedAtIsNotNull).isTrue();
    }
}
