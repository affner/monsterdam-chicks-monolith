package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monsterdam.app.domain.enumeration.NotificationKind;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "read_date")
    private Instant readDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_kind", nullable = false)
    private NotificationKind notificationKind;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "commented_user_id")
    private Long commentedUserId;

    @Column(name = "messaged_user_id")
    private Long messagedUserId;

    @Column(name = "mentioner_id_in_post")
    private Long mentionerIdInPost;

    @Column(name = "mentioner_id_in_comment")
    private Long mentionerIdInComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "post", "responseTo" }, allowSetters = true)
    private PostComment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private PostFeed post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "responseTo", "chatRoom" }, allowSetters = true)
    private DirectMessage message;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReadDate() {
        return this.readDate;
    }

    public Notification readDate(Instant readDate) {
        this.setReadDate(readDate);
        return this;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
    }

    public NotificationKind getNotificationKind() {
        return this.notificationKind;
    }

    public Notification notificationKind(NotificationKind notificationKind) {
        this.setNotificationKind(notificationKind);
        return this;
    }

    public void setNotificationKind(NotificationKind notificationKind) {
        this.notificationKind = notificationKind;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Notification createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Notification lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Notification createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Notification lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public Notification isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getCommentedUserId() {
        return this.commentedUserId;
    }

    public Notification commentedUserId(Long commentedUserId) {
        this.setCommentedUserId(commentedUserId);
        return this;
    }

    public void setCommentedUserId(Long commentedUserId) {
        this.commentedUserId = commentedUserId;
    }

    public Long getMessagedUserId() {
        return this.messagedUserId;
    }

    public Notification messagedUserId(Long messagedUserId) {
        this.setMessagedUserId(messagedUserId);
        return this;
    }

    public void setMessagedUserId(Long messagedUserId) {
        this.messagedUserId = messagedUserId;
    }

    public Long getMentionerIdInPost() {
        return this.mentionerIdInPost;
    }

    public Notification mentionerIdInPost(Long mentionerIdInPost) {
        this.setMentionerIdInPost(mentionerIdInPost);
        return this;
    }

    public void setMentionerIdInPost(Long mentionerIdInPost) {
        this.mentionerIdInPost = mentionerIdInPost;
    }

    public Long getMentionerIdInComment() {
        return this.mentionerIdInComment;
    }

    public Notification mentionerIdInComment(Long mentionerIdInComment) {
        this.setMentionerIdInComment(mentionerIdInComment);
        return this;
    }

    public void setMentionerIdInComment(Long mentionerIdInComment) {
        this.mentionerIdInComment = mentionerIdInComment;
    }

    public PostComment getComment() {
        return this.comment;
    }

    public void setComment(PostComment postComment) {
        this.comment = postComment;
    }

    public Notification comment(PostComment postComment) {
        this.setComment(postComment);
        return this;
    }

    public PostFeed getPost() {
        return this.post;
    }

    public void setPost(PostFeed postFeed) {
        this.post = postFeed;
    }

    public Notification post(PostFeed postFeed) {
        this.setPost(postFeed);
        return this;
    }

    public DirectMessage getMessage() {
        return this.message;
    }

    public void setMessage(DirectMessage directMessage) {
        this.message = directMessage;
    }

    public Notification message(DirectMessage directMessage) {
        this.setMessage(directMessage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", readDate='" + getReadDate() + "'" +
            ", notificationKind='" + getNotificationKind() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", commentedUserId=" + getCommentedUserId() +
            ", messagedUserId=" + getMessagedUserId() +
            ", mentionerIdInPost=" + getMentionerIdInPost() +
            ", mentionerIdInComment=" + getMentionerIdInComment() +
            "}";
    }
}
