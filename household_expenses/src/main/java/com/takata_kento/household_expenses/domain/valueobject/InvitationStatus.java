package com.takata_kento.household_expenses.domain.valueobject;

public enum InvitationStatus {
    PENDING,
    ACCEPTED,
    REJECTED;

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isAccepted() {
        return this == ACCEPTED;
    }

    public boolean isRejected() {
        return this == REJECTED;
    }
}
