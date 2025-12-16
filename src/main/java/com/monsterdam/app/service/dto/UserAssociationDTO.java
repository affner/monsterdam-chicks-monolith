package com.monsterdam.app.service.dto;

import com.monsterdam.app.domain.enumeration.AssociationStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.UserAssociation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAssociationDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant requestedDate;

    private AssociationStatus status;

    @NotNull
    private String associationToken;

    @NotNull
    private Instant expiryDate;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Instant deletedDate;

    @NotNull
    private UserLiteDTO owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Instant requestedDate) {
        this.requestedDate = requestedDate;
    }

    public AssociationStatus getStatus() {
        return status;
    }

    public void setStatus(AssociationStatus status) {
        this.status = status;
    }

    public String getAssociationToken() {
        return associationToken;
    }

    public void setAssociationToken(String associationToken) {
        this.associationToken = associationToken;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public UserLiteDTO getOwner() {
        return owner;
    }

    public void setOwner(UserLiteDTO owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAssociationDTO)) {
            return false;
        }

        UserAssociationDTO userAssociationDTO = (UserAssociationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAssociationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAssociationDTO{" +
            "id=" + getId() +
            ", requestedDate='" + getRequestedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", associationToken='" + getAssociationToken() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            ", owner=" + getOwner() +
            "}";
    }
}
