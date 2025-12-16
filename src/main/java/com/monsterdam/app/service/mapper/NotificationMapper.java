package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.DirectMessage;
import com.monsterdam.app.domain.Notification;
import com.monsterdam.app.domain.PostComment;
import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.DirectMessageDTO;
import com.monsterdam.app.service.dto.NotificationDTO;
import com.monsterdam.app.service.dto.PostCommentDTO;
import com.monsterdam.app.service.dto.PostFeedDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "comment", source = "comment", qualifiedByName = "postCommentId")
    @Mapping(target = "post", source = "post", qualifiedByName = "postFeedId")
    @Mapping(target = "message", source = "message", qualifiedByName = "directMessageId")
    @Mapping(target = "recipient", source = "recipient", qualifiedByName = "userLiteId")
    @Mapping(target = "actor", source = "actor", qualifiedByName = "userLiteId")
    NotificationDTO toDto(Notification s);

    @Named("postCommentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostCommentDTO toDtoPostCommentId(PostComment postComment);

    @Named("postFeedId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostFeedDTO toDtoPostFeedId(PostFeed postFeed);

    @Named("directMessageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DirectMessageDTO toDtoDirectMessageId(DirectMessage directMessage);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
