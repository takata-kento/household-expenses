package com.takata_kento.household_expenses.domain;

import static org.assertj.core.api.Assertions.*;

import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testUserCreation() {
        // Given
        UserId userId = new UserId(1L);
        Username username = new Username("testuser");
        UserGroupId userGroupId = new UserGroupId(100L);

        // When
        User actual = new User(userId, username, userGroupId);

        // Then
        assertThat(actual.id()).isEqualTo(userId);
        assertThat(actual.username()).isEqualTo(username);
        assertThat(actual.userGroupId()).isEqualTo(Optional.of(userGroupId));
    }

    @Test
    void testUserCreationWithNullUserGroupId() {
        // Given
        UserId userId = new UserId(1L);
        Username username = new Username("testuser");
        UserGroupId userGroupId = null;

        // When
        User actual = new User(userId, username, userGroupId);

        // Then
        assertThat(actual.id()).isEqualTo(userId);
        assertThat(actual.username()).isEqualTo(username);
        assertThat(actual.userGroupId()).isEqualTo(Optional.empty());
    }

    @Test
    void testId() {
        // Given
        UserId userId = new UserId(42L);
        Username username = new Username("testuser");
        UserGroupId userGroupId = new UserGroupId(100L);
        User user = new User(userId, username, userGroupId);

        // When
        UserId actual = user.id();

        // Then
        assertThat(actual).isEqualTo(userId);
    }

    @Test
    void testUsername() {
        // Given
        UserId userId = new UserId(1L);
        Username username = new Username("myusername");
        UserGroupId userGroupId = new UserGroupId(100L);
        User user = new User(userId, username, userGroupId);

        // When
        Username actual = user.username();

        // Then
        assertThat(actual).isEqualTo(username);
    }

    @Test
    void testUserGroupIdPresent() {
        // Given
        UserId userId = new UserId(1L);
        Username username = new Username("testuser");
        UserGroupId userGroupId = new UserGroupId(999L);
        User user = new User(userId, username, userGroupId);

        // When
        Optional<UserGroupId> actual = user.userGroupId();

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(userGroupId);
    }

    @Test
    void testUserGroupIdEmpty() {
        // Given
        UserId userId = new UserId(1L);
        Username username = new Username("testuser");
        UserGroupId userGroupId = null;
        User user = new User(userId, username, userGroupId);

        // When
        Optional<UserGroupId> actual = user.userGroupId();

        // Then
        assertThat(actual).isEmpty();
    }
}
