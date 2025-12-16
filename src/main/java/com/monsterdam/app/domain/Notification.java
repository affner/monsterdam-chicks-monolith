package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monsterdam.app.domain.enumeration.NotificationKind;
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
 * A Notification.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "post", "responseTo", "commenter" }, allowSetters = true)
    private PostComment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "creator", "polls" }, allowSetters = true)
    private PostFeed post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "responseTo", "chatRoom", "sender" }, allowSetters = true)
    private DirectMessage message;

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
    private UserLite recipient;

    @ManyToOne(fetch = FetchType.LAZY)
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
    private UserLite actor;

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

    public UserLite getRecipient() {
        return this.recipient;
    }

    public void setRecipient(UserLite userLite) {
        this.recipient = userLite;
    }

    public Notification recipient(UserLite userLite) {
        this.setRecipient(userLite);
        return this;
    }

    public UserLite getActor() {
        return this.actor;
    }

    public void setActor(UserLite userLite) {
        this.actor = userLite;
    }

    public Notification actor(UserLite userLite) {
        this.setActor(userLite);
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
            "}";
    }
}
