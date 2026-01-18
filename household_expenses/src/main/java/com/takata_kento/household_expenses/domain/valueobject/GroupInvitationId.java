package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record GroupInvitationId(UUID value) implements UUIDValueObject {
    public GroupInvitationId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
