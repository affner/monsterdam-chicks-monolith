package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monsterdam.app.domain.enumeration.LikeEntityKind;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A LikeMark.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "like_mark")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LikeMark implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private LikeEntityKind entityType;

    @NotNull
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    @Column(name = "multimedia_id")
    private Long multimediaId;

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "profile",
            "settings",
            "countryOfBirth",
            "purchasedSubscriptionsAsViewers",
            "purchasedSubscriptionsAsCreators",
            "pollVotes",
            "associationsAsOwners",
            "assistanceTickets",
            "payments",
            "comments",
            "posts",
            "singleAudios",
            "singleVideos",
            "singlePhotos",
            "videoStories",
            "directMessagesSents",
            "likeMarks",
            "notificationsReceiveds",
            "notificationsTriggereds",
            "postMentions",
            "commentMentions",
            "chatRooms",
        },
        allowSetters = true
    )
    private UserLite liker;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LikeMark id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LikeEntityKind getEntityType() {
        return this.entityType;
    }

    public LikeMark entityType(LikeEntityKind entityType) {
        this.setEntityType(entityType);
        return this;
    }

    public void setEntityType(LikeEntityKind entityType) {
        this.entityType = entityType;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public LikeMark createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public LikeMark lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public LikeMark createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public LikeMark lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public LikeMark deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Long getMultimediaId() {
        return this.multimediaId;
    }

    public LikeMark multimediaId(Long multimediaId) {
        this.setMultimediaId(multimediaId);
        return this;
    }

    public void setMultimediaId(Long multimediaId) {
        this.multimediaId = multimediaId;
    }

    public Long getMessageId() {
        return this.messageId;
    }

    public LikeMark messageId(Long messageId) {
        this.setMessageId(messageId);
        return this;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getPostId() {
        return this.postId;
    }

    public LikeMark postId(Long postId) {
        this.setPostId(postId);
        return this;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return this.commentId;
    }

    public LikeMark commentId(Long commentId) {
        this.setCommentId(commentId);
        return this;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public UserLite getLiker() {
        return this.liker;
    }

    public void setLiker(UserLite userLite) {
        this.liker = userLite;
    }

    public LikeMark liker(UserLite userLite) {
        this.setLiker(userLite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikeMark)) {
            return false;
        }
        return getId() != null && getId().equals(((LikeMark) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikeMark{" +
            "id=" + getId() +
            ", entityType='" + getEntityType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            ", multimediaId=" + getMultimediaId() +
            ", messageId=" + getMessageId() +
            ", postId=" + getPostId() +
            ", commentId=" + getCommentId() +
            "}";
    }
}
