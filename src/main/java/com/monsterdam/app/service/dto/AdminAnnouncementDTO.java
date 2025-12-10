package com.monsterdam.app.service.dto;

import com.monsterdam.app.domain.enumeration.AdminAnnouncementType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.AdminAnnouncement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdminAnnouncementDTO implements Serializable {

    private Long id;

    @NotNull
    private AdminAnnouncementType announcementType;

    @NotNull
    @Size(max = 100)
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    @NotNull
    private Long announcerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdminAnnouncementType getAnnouncementType() {
        return announcementType;
    }

    public void setAnnouncementType(AdminAnnouncementType announcementType) {
        this.announcementType = announcementType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Long getAnnouncerId() {
        return announcerId;
    }

    public void setAnnouncerId(Long announcerId) {
        this.announcerId = announcerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminAnnouncementDTO)) {
            return false;
        }

        AdminAnnouncementDTO adminAnnouncementDTO = (AdminAnnouncementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adminAnnouncementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminAnnouncementDTO{" +
            "id=" + getId() +
            ", announcementType='" + getAnnouncementType() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", announcerId=" + getAnnouncerId() +
            "}";
    }
}
