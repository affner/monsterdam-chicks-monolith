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
 * A DirectMessage.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "direct_message")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DirectMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "message_content", nullable = false)
    private String messageContent;

    @Column(name = "read_date")
    private Instant readDate;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "is_hidden")
    private Boolean isHidden;

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

    @Column(name = "replied_story_id")
    private Long repliedStoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "responseTo", "chatRoom", "sender" }, allowSetters = true)
    private DirectMessage responseTo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "participants" }, allowSetters = true)
    private ChatRoom chatRoom;

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
    private UserLite sender;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DirectMessage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public DirectMessage messageContent(String messageContent) {
        this.setMessageContent(messageContent);
        return this;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Instant getReadDate() {
        return this.readDate;
    }

    public DirectMessage readDate(Instant readDate) {
        this.setReadDate(readDate);
        return this;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
    }

    public Integer getLikeCount() {
        return this.likeCount;
    }

    public DirectMessage likeCount(Integer likeCount) {
        this.setLikeCount(likeCount);
        return this;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getIsHidden() {
        return this.isHidden;
    }

    public DirectMessage isHidden(Boolean isHidden) {
        this.setIsHidden(isHidden);
        return this;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public DirectMessage createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public DirectMessage lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public DirectMessage createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public DirectMessage lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public DirectMessage deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Long getRepliedStoryId() {
        return this.repliedStoryId;
    }

    public DirectMessage repliedStoryId(Long repliedStoryId) {
        this.setRepliedStoryId(repliedStoryId);
        return this;
    }

    public void setRepliedStoryId(Long repliedStoryId) {
        this.repliedStoryId = repliedStoryId;
    }

    public DirectMessage getResponseTo() {
        return this.responseTo;
    }

    public void setResponseTo(DirectMessage directMessage) {
        this.responseTo = directMessage;
    }

    public DirectMessage responseTo(DirectMessage directMessage) {
        this.setResponseTo(directMessage);
        return this;
    }

    public ChatRoom getChatRoom() {
        return this.chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public DirectMessage chatRoom(ChatRoom chatRoom) {
        this.setChatRoom(chatRoom);
        return this;
    }

    public UserLite getSender() {
        return this.sender;
    }

    public void setSender(UserLite userLite) {
        this.sender = userLite;
    }

    public DirectMessage sender(UserLite userLite) {
        this.setSender(userLite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DirectMessage)) {
            return false;
        }
        return getId() != null && getId().equals(((DirectMessage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DirectMessage{" +
            "id=" + getId() +
            ", messageContent='" + getMessageContent() + "'" +
            ", readDate='" + getReadDate() + "'" +
            ", likeCount=" + getLikeCount() +
            ", isHidden='" + getIsHidden() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            ", repliedStoryId=" + getRepliedStoryId() +
            "}";
    }
}
