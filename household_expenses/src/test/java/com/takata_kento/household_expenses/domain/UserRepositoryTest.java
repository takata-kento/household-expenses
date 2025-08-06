package com.takata_kento.household_expenses.domain;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
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
        jdbcClient.sql("DROP TABLE IF EXISTS users").update();
        jdbcClient
            .sql(
                """
                CREATE TABLE users (
                    id BIGSERIAL PRIMARY KEY,
                    username VARCHAR(255) UNIQUE NOT NULL,
                    password_hash VARCHAR(255) NOT NULL,
                    user_group_id BIGINT,
                    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP WITH TIME ZONE,
                    enabled boolean
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

        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, user_group_id, enabled) VALUES (?, ?, ?, ?, ?)")
            .params(expectedId, expectedUsername, "dummy_hash", expectedUserGroupId, true)
            .update();

        // When
        Optional<User> actual = userRepository.findById(userId);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(new UserId(expectedId));
        assertThat(actual.get().username()).isEqualTo(new Username(expectedUsername));
        assertThat(actual.get().userGroupId()).isEqualTo(Optional.of(new UserGroupId(expectedUserGroupId)));
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
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, user_group_id, enabled) VALUES (?, ?, ?, ?, ?)")
            .params(expectedId, expectedUsername, "dummy_hash", expectedUserGroupId, true)
            .update();

        // When
        Optional<User> actual = userRepository.findByUsername(username);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(new UserId(expectedId));
        assertThat(actual.get().username()).isEqualTo(new Username(expectedUsername));
        assertThat(actual.get().userGroupId()).isEqualTo(Optional.of(new UserGroupId(expectedUserGroupId)));
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
        jdbcClient
            .sql("INSERT INTO users (id, username, password_hash, user_group_id, enabled) VALUES (?, ?, ?, ?, ?)")
            .params(expectedId, expectedUsername, "dummy_hash", expectedUserGroupId, true)
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
            .sql("INSERT INTO users (id, username, password_hash, user_group_id, enabled) VALUES (?, ?, ?, ?, ?)")
            .params(expectedId, expectedUsername, "dummy_hash", expectedUserGroupId, true)
            .update();

        // When
        Optional<User> actual = userRepository.findById(userId);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().id()).isEqualTo(new UserId(expectedId));
        assertThat(actual.get().username()).isEqualTo(new Username(expectedUsername));
        assertThat(actual.get().userGroupId()).isEmpty();
    }
}
