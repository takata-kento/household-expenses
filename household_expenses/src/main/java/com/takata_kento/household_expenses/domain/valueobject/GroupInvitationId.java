package com.takata_kento.household_expenses.domain.valueobject;

public record GroupInvitationId(long value) {
    public GroupInvitationId {
        if (value <= 0) {
            throw new IllegalArgumentException("GroupInvitationId must be positive");
        }
    }
}
