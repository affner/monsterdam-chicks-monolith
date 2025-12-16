package com.monsterdam.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.CommentMention} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentMentionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Instant deletedDate;

    private PostCommentDTO originPostComment;

    @NotNull
    private UserLiteDTO mentionedUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PostCommentDTO getOriginPostComment() {
        return originPostComment;
    }

    public void setOriginPostComment(PostCommentDTO originPostComment) {
        this.originPostComment = originPostComment;
    }

    public UserLiteDTO getMentionedUser() {
        return mentionedUser;
    }

    public void setMentionedUser(UserLiteDTO mentionedUser) {
        this.mentionedUser = mentionedUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentMentionDTO)) {
            return false;
        }

        CommentMentionDTO commentMentionDTO = (CommentMentionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentMentionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentMentionDTO{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            ", originPostComment=" + getOriginPostComment() +
            ", mentionedUser=" + getMentionedUser() +
            "}";
    }
}
