package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monsterdam.app.domain.enumeration.ChatRoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A ChatRoom.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "chat_room")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private ChatRoomType roomType;

    @Size(max = 120)
    @Column(name = "title", length = 120)
    private String title;

    @Column(name = "last_action")
    private String lastAction;

    @Column(name = "last_connection_date")
    private Instant lastConnectionDate;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_chat_room__participants",
        joinColumns = @JoinColumn(name = "chat_room_id"),
        inverseJoinColumns = @JoinColumn(name = "participants_id")
    )
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
    private Set<UserLite> participants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChatRoom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatRoomType getRoomType() {
        return this.roomType;
    }

    public ChatRoom roomType(ChatRoomType roomType) {
        this.setRoomType(roomType);
        return this;
    }

    public void setRoomType(ChatRoomType roomType) {
        this.roomType = roomType;
    }

    public String getTitle() {
        return this.title;
    }

    public ChatRoom title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastAction() {
        return this.lastAction;
    }

    public ChatRoom lastAction(String lastAction) {
        this.setLastAction(lastAction);
        return this;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public Instant getLastConnectionDate() {
        return this.lastConnectionDate;
    }

    public ChatRoom lastConnectionDate(Instant lastConnectionDate) {
        this.setLastConnectionDate(lastConnectionDate);
        return this;
    }

    public void setLastConnectionDate(Instant lastConnectionDate) {
        this.lastConnectionDate = lastConnectionDate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ChatRoom createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ChatRoom lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ChatRoom createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public ChatRoom lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public ChatRoom deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Set<UserLite> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<UserLite> userLites) {
        this.participants = userLites;
    }

    public ChatRoom participants(Set<UserLite> userLites) {
        this.setParticipants(userLites);
        return this;
    }

    public ChatRoom addParticipants(UserLite userLite) {
        this.participants.add(userLite);
        return this;
    }

    public ChatRoom removeParticipants(UserLite userLite) {
        this.participants.remove(userLite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatRoom)) {
            return false;
        }
        return getId() != null && getId().equals(((ChatRoom) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatRoom{" +
            "id=" + getId() +
            ", roomType='" + getRoomType() + "'" +
            ", title='" + getTitle() + "'" +
            ", lastAction='" + getLastAction() + "'" +
            ", lastConnectionDate='" + getLastConnectionDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            "}";
    }
}
