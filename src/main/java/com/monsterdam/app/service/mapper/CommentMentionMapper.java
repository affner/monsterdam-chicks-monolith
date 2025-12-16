package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.CommentMention;
import com.monsterdam.app.domain.PostComment;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.CommentMentionDTO;
import com.monsterdam.app.service.dto.PostCommentDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommentMention} and its DTO {@link CommentMentionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentMentionMapper extends EntityMapper<CommentMentionDTO, CommentMention> {
    @Mapping(target = "originPostComment", source = "originPostComment", qualifiedByName = "postCommentId")
    @Mapping(target = "mentionedUser", source = "mentionedUser", qualifiedByName = "userLiteId")
    CommentMentionDTO toDto(CommentMention s);

    @Named("postCommentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostCommentDTO toDtoPostCommentId(PostComment postComment);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
