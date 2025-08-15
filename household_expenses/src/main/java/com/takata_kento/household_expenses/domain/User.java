package com.takata_kento.household_expenses.domain;

import com.takata_kento.household_expenses.domain.dto.GroupInvitationInfo;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public class User {

    @Id
    private final UserId id;

    @Column("username")
    private final Username name;

    @Column("user_group_id")
    private Optional<UserGroupId> userGroupId;

    @MappedCollection(idColumn = "invited_user_id")
    private Set<GroupInvitation> receivedInvitations;

    @Version
    private Integer version;

    public User(
        UserId id,
        Username username,
        Optional<UserGroupId> userGroup,
        Set<GroupInvitation> receivedInvitations,
        Integer version
    ) {
        this.id = id;
        this.name = username;
        this.userGroupId = userGroup != null ? userGroup : Optional.empty();
        this.receivedInvitations = receivedInvitations;
        this.version = version;
    }

    public UserId id() {
        return id;
    }

    public Username name() {
        return name;
    }

    public Optional<UserGroupId> userGroupId() {
        return userGroupId;
    }

    public boolean isBelongsToGroup() {
        return userGroupId.isPresent();
    }

    public Set<GroupInvitationInfo> receivedInvitations() {
        if (receivedInvitations == null) {
            return Set.of();
        }
        return receivedInvitations.stream().map(GroupInvitationInfo::from).collect(Collectors.toUnmodifiableSet());
    }

    public Set<GroupInvitationInfo> getPendingInvitations() {
        if (receivedInvitations == null) {
            return Set.of();
        }
        return receivedInvitations
            .stream()
            .filter(GroupInvitation::isPending)
            .map(GroupInvitationInfo::from)
            .collect(Collectors.toUnmodifiableSet());
    }

    public void leaveGroup() {
        this.userGroupId = Optional.empty();
    }

    public boolean canCreateGroup() {
        return !this.isBelongsToGroup();
    }

    public boolean canLeaveGroup() {
        return this.isBelongsToGroup();
    }

    public boolean canInvite(UserId inviteeUserId) {
        return this.isBelongsToGroup() && !this.id.equals(inviteeUserId);
    }

    public void invite(User invitee) {
        if (!this.canInvite(invitee.id())) {
            throw new IllegalStateException("Cannot invite user");
        }

        GroupInvitation invitation = GroupInvitation.create(this.userGroupId.orElseThrow(), invitee.id(), this.id);

        invitee.addInvitation(invitation);
    }

    void addInvitation(GroupInvitation invitation) {
        if (this.receivedInvitations == null) {
            this.receivedInvitations = new HashSet<>();
        }
        this.receivedInvitations.add(invitation);
    }

    public void accept(GroupInvitationId invitationId) {
        if (receivedInvitations == null) {
            throw new IllegalArgumentException("No invitations to accept");
        }

        GroupInvitation invitation = receivedInvitations
            .stream()
            .filter(inv -> inv.id().equals(invitationId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        invitation.accept();
        this.userGroupId = Optional.of(invitation.userGroupId());
    }

    public void reject(GroupInvitationId invitationId) {
        if (receivedInvitations == null) {
            throw new IllegalArgumentException("No invitations to reject");
        }

        GroupInvitation invitation = receivedInvitations
            .stream()
            .filter(inv -> inv.id().equals(invitationId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        invitation.reject();
    }

    public Integer version() {
        return version;
    }
}
