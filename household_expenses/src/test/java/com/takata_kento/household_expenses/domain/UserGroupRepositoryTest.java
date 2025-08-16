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
    }

    @Test
    void testSave() {
        // Given
        long createdByUserId = 1L;
        String groupName = "Test Group";
        int monthStartDay = 15;

        // 作成者ユーザーを挿入
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, enabled, version) VALUES (?, ?, ?, ?, ?)")
            .params(createdByUserId, "creator", "dummy_hash", true, 1)
            .update();

        UserGroup userGroup = UserGroup.create(
            new GroupName(groupName),
            new Day(monthStartDay),
            new UserId(createdByUserId)
        );

        // When
        UserGroup savedUserGroup = userGroupRepository.save(userGroup);

        // Then
        assertThat(savedUserGroup.id()).isNotNull();
        assertThat(savedUserGroup.groupName()).isEqualTo(new GroupName(groupName));
        assertThat(savedUserGroup.monthStartDay()).isEqualTo(new Day(monthStartDay));
        assertThat(savedUserGroup.createdByUserId()).isEqualTo(new UserId(createdByUserId));
        assertThat(savedUserGroup.createdAt()).isNotNull();
        assertThat(savedUserGroup.version()).isNotNull();

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
        String expectedGroupName = "Find Test Group";
        int expectedMonthStartDay = 10;
        long expectedCreatedByUserId = 2L;

        // 作成者ユーザーを挿入
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, enabled, version) VALUES (?, ?, ?, ?, ?)")
            .params(expectedCreatedByUserId, "creator2", "dummy_hash", true, 1)
            .update();

        jdbcClient
            .sql(
                "INSERT INTO user_group (id, group_name, month_start_day, created_by_user_id, version) VALUES (?, ?, ?, ?, ?)"
            )
            .params(expectedId, expectedGroupName, expectedMonthStartDay, expectedCreatedByUserId, 1)
            .update();

        UserGroupId userGroupId = new UserGroupId(expectedId);

        // When
        Optional<UserGroup> actual = userGroupRepository.findById(userGroupId);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(new UserGroupId(expectedId));
        assertThat(actual.get().groupName()).isEqualTo(new GroupName(expectedGroupName));
        assertThat(actual.get().monthStartDay()).isEqualTo(new Day(expectedMonthStartDay));
        assertThat(actual.get().createdByUserId()).isEqualTo(new UserId(expectedCreatedByUserId));
    }

    @Test
    void testFindByGroupName() {
        // Given
        long expectedId = 3L;
        String expectedGroupName = "Find By Name Group";
        int expectedMonthStartDay = 20;
        long expectedCreatedByUserId = 3L;

        // 作成者ユーザーを挿入
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, enabled, version) VALUES (?, ?, ?, ?, ?)")
            .params(expectedCreatedByUserId, "creator3", "dummy_hash", true, 1)
            .update();

        jdbcClient
            .sql(
                "INSERT INTO user_group (id, group_name, month_start_day, created_by_user_id, version) VALUES (?, ?, ?, ?, ?)"
            )
            .params(expectedId, expectedGroupName, expectedMonthStartDay, expectedCreatedByUserId, 1)
            .update();

        GroupName groupName = new GroupName(expectedGroupName);

        // When
        Optional<UserGroup> actual = userGroupRepository.findByGroupName(groupName);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(new UserGroupId(expectedId));
        assertThat(actual.get().groupName()).isEqualTo(new GroupName(expectedGroupName));
    }

    @Test
    void testExistsByGroupName() {
        // Given
        long expectedId = 4L;
        String expectedGroupName = "Exists Test Group";
        int expectedMonthStartDay = 25;
        long expectedCreatedByUserId = 4L;

        // 作成者ユーザーを挿入
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, enabled, version) VALUES (?, ?, ?, ?, ?)")
            .params(expectedCreatedByUserId, "creator4", "dummy_hash", true, 1)
            .update();

        jdbcClient
            .sql(
                "INSERT INTO user_group (id, group_name, month_start_day, created_by_user_id, version) VALUES (?, ?, ?, ?, ?)"
            )
            .params(expectedId, expectedGroupName, expectedMonthStartDay, expectedCreatedByUserId, 1)
            .update();

        GroupName groupName = new GroupName(expectedGroupName);

        // When
        boolean actual = userGroupRepository.existsByGroupName(groupName);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void testUpdate() {
        // Given
        long userGroupId = 10L;
        long createdByUserId = 7L;
        String originalGroupName = "Original Group";
        String updatedGroupName = "Updated Group";
        int originalMonthStartDay = 1;
        int updatedMonthStartDay = 28;

        // 作成者ユーザーを挿入
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, enabled, version) VALUES (?, ?, ?, ?, ?)")
            .params(createdByUserId, "creator7", "dummy_hash", true, 1)
            .update();

        jdbcClient
            .sql(
                "INSERT INTO user_group (id, group_name, month_start_day, created_by_user_id, version) VALUES (?, ?, ?, ?, ?)"
            )
            .params(userGroupId, originalGroupName, originalMonthStartDay, createdByUserId, 1)
            .update();

        // When
        Optional<UserGroup> userGroupOptional = userGroupRepository.findById(new UserGroupId(userGroupId));
        assertThat(userGroupOptional).isPresent();

        UserGroup userGroup = userGroupOptional.get();
        userGroup.updateGroupName(new GroupName(updatedGroupName));
        userGroup.updateMonthStartDay(new Day(updatedMonthStartDay));

        userGroupRepository.save(userGroup);

        // Then
        // リポジトリから再取得して確認
        Optional<UserGroup> actualUserGroup = userGroupRepository.findById(new UserGroupId(userGroupId));
        assertThat(actualUserGroup).isPresent();
        assertThat(actualUserGroup.get().groupName()).isEqualTo(new GroupName(updatedGroupName));
        assertThat(actualUserGroup.get().monthStartDay()).isEqualTo(new Day(updatedMonthStartDay));
        assertThat(actualUserGroup.get().updatedAt()).isNotNull();

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
