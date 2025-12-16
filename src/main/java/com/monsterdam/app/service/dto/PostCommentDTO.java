package com.monsterdam.app.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.PostComment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostCommentDTO implements Serializable {

    private Long id;

    @Lob
    private String commentContent;

    private Integer likeCount;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Instant deletedDate;

    private PostFeedDTO post;

    private PostCommentDTO responseTo;

    @NotNull
    private UserLiteDTO commenter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
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

    public PostFeedDTO getPost() {
        return post;
    }

    public void setPost(PostFeedDTO post) {
        this.post = post;
    }

    public PostCommentDTO getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(PostCommentDTO responseTo) {
        this.responseTo = responseTo;
    }

    public UserLiteDTO getCommenter() {
        return commenter;
    }

    public void setCommenter(UserLiteDTO commenter) {
        this.commenter = commenter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostCommentDTO)) {
            return false;
        }

        PostCommentDTO postCommentDTO = (PostCommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, postCommentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostCommentDTO{" +
            "id=" + getId() +
            ", commentContent='" + getCommentContent() + "'" +
            ", likeCount=" + getLikeCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            ", post=" + getPost() +
            ", responseTo=" + getResponseTo() +
            ", commenter=" + getCommenter() +
            "}";
    }
}
