package com.takata_kento.household_expenses.domain.valueobject;

import java.util.UUID;

public record UserGroupId(UUID value) implements UUIDValueObject {
    public UserGroupId {
        ValidateUtil.validUUID(value, getClass());
    }

    public String toString() {
        return value.toString();
    }
}
