package com.takata_kento.household_expenses.domain.valueobject;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InvitationStatusTest {

    @Test
    void testPendingStatus() {
        // Given
        InvitationStatus status = InvitationStatus.PENDING;

        // When & Then
        assertThat(status.isPending()).isTrue();
        assertThat(status.isAccepted()).isFalse();
        assertThat(status.isRejected()).isFalse();
    }

    @Test
    void testAcceptedStatus() {
        // Given
        InvitationStatus status = InvitationStatus.ACCEPTED;

        // When & Then
        assertThat(status.isPending()).isFalse();
        assertThat(status.isAccepted()).isTrue();
        assertThat(status.isRejected()).isFalse();
    }

    @Test
    void testRejectedStatus() {
        // Given
        InvitationStatus status = InvitationStatus.REJECTED;

        // When & Then
        assertThat(status.isPending()).isFalse();
        assertThat(status.isAccepted()).isFalse();
        assertThat(status.isRejected()).isTrue();
    }

    @Test
    void testValueOf() {
        // Given & When & Then
        assertThat(InvitationStatus.valueOf("PENDING")).isEqualTo(InvitationStatus.PENDING);
        assertThat(InvitationStatus.valueOf("ACCEPTED")).isEqualTo(InvitationStatus.ACCEPTED);
        assertThat(InvitationStatus.valueOf("REJECTED")).isEqualTo(InvitationStatus.REJECTED);
    }

    @Test
    void testValueOfInvalidValue() {
        // Given
        String invalidValue = "INVALID";

        // When & Then
        assertThatThrownBy(() -> InvitationStatus.valueOf(invalidValue)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testToString() {
        // Given & When & Then
        assertThat(InvitationStatus.PENDING.toString()).isEqualTo("PENDING");
        assertThat(InvitationStatus.ACCEPTED.toString()).isEqualTo("ACCEPTED");
        assertThat(InvitationStatus.REJECTED.toString()).isEqualTo("REJECTED");
    }

    @Test
    void testValues() {
        // Given & When
        InvitationStatus[] values = InvitationStatus.values();

        // Then
        assertThat(values).hasSize(3);
        assertThat(values).contains(InvitationStatus.PENDING, InvitationStatus.ACCEPTED, InvitationStatus.REJECTED);
    }
}
