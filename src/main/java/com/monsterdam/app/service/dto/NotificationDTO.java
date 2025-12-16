package com.monsterdam.app.service.dto;

import com.monsterdam.app.domain.enumeration.NotificationKind;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.Notification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDTO implements Serializable {

    private Long id;

    private Instant readDate;

    @NotNull
    private NotificationKind notificationKind;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private PostCommentDTO comment;

    private PostFeedDTO post;

    private DirectMessageDTO message;

    @NotNull
    private UserLiteDTO recipient;

    private UserLiteDTO actor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReadDate() {
        return readDate;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
    }

    public NotificationKind getNotificationKind() {
        return notificationKind;
    }

    public void setNotificationKind(NotificationKind notificationKind) {
        this.notificationKind = notificationKind;
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

    public PostCommentDTO getComment() {
        return comment;
    }

    public void setComment(PostCommentDTO comment) {
        this.comment = comment;
    }

    public PostFeedDTO getPost() {
        return post;
    }

    public void setPost(PostFeedDTO post) {
        this.post = post;
    }

    public DirectMessageDTO getMessage() {
        return message;
    }

    public void setMessage(DirectMessageDTO message) {
        this.message = message;
    }

    public UserLiteDTO getRecipient() {
        return recipient;
    }

    public void setRecipient(UserLiteDTO recipient) {
        this.recipient = recipient;
    }

    public UserLiteDTO getActor() {
        return actor;
    }

    public void setActor(UserLiteDTO actor) {
        this.actor = actor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", readDate='" + getReadDate() + "'" +
            ", notificationKind='" + getNotificationKind() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", comment=" + getComment() +
            ", post=" + getPost() +
            ", message=" + getMessage() +
            ", recipient=" + getRecipient() +
            ", actor=" + getActor() +
            "}";
    }
}
