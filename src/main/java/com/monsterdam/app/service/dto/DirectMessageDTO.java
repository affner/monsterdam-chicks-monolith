package com.monsterdam.app.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.DirectMessage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DirectMessageDTO implements Serializable {

    private Long id;

    @Lob
    private String messageContent;

    private Instant readDate;

    private Integer likeCount;

    private Boolean isHidden;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Instant deletedDate;

    private Long repliedStoryId;

    private DirectMessageDTO responseTo;

    @NotNull
    private ChatRoomDTO chatRoom;

    @NotNull
    private UserLiteDTO sender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Instant getReadDate() {
        return readDate;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
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

    public Long getRepliedStoryId() {
        return repliedStoryId;
    }

    public void setRepliedStoryId(Long repliedStoryId) {
        this.repliedStoryId = repliedStoryId;
    }

    public DirectMessageDTO getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(DirectMessageDTO responseTo) {
        this.responseTo = responseTo;
    }

    public ChatRoomDTO getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoomDTO chatRoom) {
        this.chatRoom = chatRoom;
    }

    public UserLiteDTO getSender() {
        return sender;
    }

    public void setSender(UserLiteDTO sender) {
        this.sender = sender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DirectMessageDTO)) {
            return false;
        }

        DirectMessageDTO directMessageDTO = (DirectMessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, directMessageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DirectMessageDTO{" +
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
            ", responseTo=" + getResponseTo() +
            ", chatRoom=" + getChatRoom() +
            ", sender=" + getSender() +
            "}";
    }
}
