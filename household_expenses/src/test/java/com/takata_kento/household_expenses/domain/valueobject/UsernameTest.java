package com.takata_kento.household_expenses.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class UsernameTest {
    
    @Test
    void testCreate() {
        // Given
        String value = "testuser";
        
        // When
        Username actual = new Username(value);
        
        // Then
        assertThat(actual.value()).isEqualTo("testuser");
    }
    
    @Test
    void testCreateWithNull() {
        // Given
        String value = null;
        
        // When & Then
        assertThatThrownBy(() -> new Username(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username cannot be null");
    }
    
    @Test
    void testCreateWithEmpty() {
        // Given
        String value = "";
        
        // When & Then
        assertThatThrownBy(() -> new Username(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username cannot be empty");
    }
    
    @Test
    void testCreateWithTooLong() {
        // Given
        String value = "a".repeat(51);
        
        // When & Then
        assertThatThrownBy(() -> new Username(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username cannot be longer than 50 characters");
    }
    
    @Test
    void testCreateWithMaxLength() {
        // Given
        String value = "a".repeat(50);
        
        // When
        Username actual = new Username(value);
        
        // Then
        assertThat(actual.value()).isEqualTo(value);
    }
    
    @Test
    void testEquals() {
        // Given
        Username username1 = new Username("testuser");
        Username username2 = new Username("testuser");
        Username username3 = new Username("otheruser");
        
        // When & Then
        assertThat(username1).isEqualTo(username2);
        assertThat(username1).isNotEqualTo(username3);
    }
    
    @Test
    void testHashCode() {
        // Given
        Username username1 = new Username("testuser");
        Username username2 = new Username("testuser");
        
        // When & Then
        assertThat(username1.hashCode()).isEqualTo(username2.hashCode());
    }
    
    @Test
    void testToString() {
        // Given
        Username username = new Username("testuser");
        
        // When
        String actual = username.toString();
        
        // Then
        assertThat(actual).contains("testuser");
    }
}
