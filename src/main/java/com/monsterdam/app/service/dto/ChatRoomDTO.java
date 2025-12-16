package com.monsterdam.app.service.dto;

import com.monsterdam.app.domain.enumeration.ChatRoomType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.monsterdam.app.domain.ChatRoom} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatRoomDTO implements Serializable {

    private Long id;

    @NotNull
    private ChatRoomType roomType;

    @Size(max = 120)
    private String title;

    private String lastAction;

    private Instant lastConnectionDate;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Instant deletedDate;

    private Set<UserLiteDTO> participants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatRoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(ChatRoomType roomType) {
        this.roomType = roomType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public Instant getLastConnectionDate() {
        return lastConnectionDate;
    }

    public void setLastConnectionDate(Instant lastConnectionDate) {
        this.lastConnectionDate = lastConnectionDate;
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

    public Set<UserLiteDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<UserLiteDTO> participants) {
        this.participants = participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatRoomDTO)) {
            return false;
        }

        ChatRoomDTO chatRoomDTO = (ChatRoomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chatRoomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatRoomDTO{" +
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
            ", participants=" + getParticipants() +
            "}";
    }
}
