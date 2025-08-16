package com.takata_kento.household_expenses.domain;

import com.takata_kento.household_expenses.domain.valueobject.*;
import java.time.LocalDateTime;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("group_invitation")
public class GroupInvitation {

    @Column("id")
    private GroupInvitationId id;

    @Column("user_group_id")
    private final UserGroupId userGroupId;

    @Column("invited_user_id")
    private final UserId invitedUserId;

    @Column("invited_by_user_id")
    private final UserId invitedByUserId;

    @Column("status")
    private InvitationStatus status;

    @Column("invited_at")
    private final LocalDateTime invitedAt;

    @Column("responded_at")
    private LocalDateTime respondedAt;

    @Column("created_at")
    private final LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public GroupInvitation(
        GroupInvitationId id,
        UserGroupId userGroupId,
        UserId invitedUserId,
        UserId invitedByUserId,
        InvitationStatus status,
        LocalDateTime invitedAt,
        LocalDateTime respondedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userGroupId = userGroupId;
        this.invitedUserId = invitedUserId;
        this.invitedByUserId = invitedByUserId;
        this.status = status;
        this.invitedAt = invitedAt;
        this.respondedAt = respondedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GroupInvitation create(UserGroupId userGroupId, UserId invitedUserId, UserId invitedByUserId) {
        LocalDateTime now = LocalDateTime.now();
        Long invitationId = Long.parseLong(String.valueOf(userGroupId.value()) + String.valueOf(invitedUserId.value()));
        GroupInvitationId id = new GroupInvitationId(invitationId);
        return new GroupInvitation(
            id,
            userGroupId,
            invitedUserId,
            invitedByUserId,
            InvitationStatus.PENDING,
            now,
            null,
            now,
            now
        );
    }

    public GroupInvitationId id() {
        return id;
    }

    public UserGroupId userGroupId() {
        return userGroupId;
    }

    public UserId invitedUserId() {
        return invitedUserId;
    }

    public UserId invitedByUserId() {
        return invitedByUserId;
    }

    public InvitationStatus status() {
        return status;
    }

    public LocalDateTime invitedAt() {
        return invitedAt;
    }

    public LocalDateTime respondedAt() {
        return respondedAt;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public void accept() {
        this.status = InvitationStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = InvitationStatus.REJECTED;
        this.respondedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return status.isPending();
    }

    public boolean canRespond() {
        return isPending();
    }

    public boolean isFrom(UserGroupId userGroupId) {
        return this.userGroupId.equals(userGroupId);
    }
}
