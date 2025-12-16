package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A PostComment.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "post_comment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @Column(name = "like_count")
    private Integer likeCount;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "creator", "polls" }, allowSetters = true)
    private PostFeed post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "post", "responseTo", "commenter" }, allowSetters = true)
    private PostComment responseTo;

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
    private UserLite commenter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostComment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentContent() {
        return this.commentContent;
    }

    public PostComment commentContent(String commentContent) {
        this.setCommentContent(commentContent);
        return this;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Integer getLikeCount() {
        return this.likeCount;
    }

    public PostComment likeCount(Integer likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PostComment createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PostComment lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PostComment createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PostComment lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public PostComment deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public PostFeed getPost() {
        return this.post;
    }

    public void setPost(PostFeed postFeed) {
        this.post = postFeed;
    }

    public PostComment post(PostFeed postFeed) {
        this.setPost(postFeed);
        return this;
    }

    public PostComment getResponseTo() {
        return this.responseTo;
    }

    public void setResponseTo(PostComment postComment) {
        this.responseTo = postComment;
    }

    public PostComment responseTo(PostComment postComment) {
        this.setResponseTo(postComment);
        return this;
    }

    public UserLite getCommenter() {
        return this.commenter;
    }

    public void setCommenter(UserLite userLite) {
        this.commenter = userLite;
    }

    public PostComment commenter(UserLite userLite) {
        this.setCommenter(userLite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostComment)) {
            return false;
        }
        return getId() != null && getId().equals(((PostComment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostComment{" +
            "id=" + getId() +
            ", commentContent='" + getCommentContent() + "'" +
            ", likeCount=" + getLikeCount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            "}";
    }
}
