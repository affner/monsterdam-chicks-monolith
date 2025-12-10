package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PostComment;
import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.service.dto.PostCommentDTO;
import com.monsterdam.app.service.dto.PostFeedDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostComment} and its DTO {@link PostCommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostCommentMapper extends EntityMapper<PostCommentDTO, PostComment> {
    @Mapping(target = "post", source = "post", qualifiedByName = "postFeedId")
    @Mapping(target = "responseTo", source = "responseTo", qualifiedByName = "postCommentId")
    PostCommentDTO toDto(PostComment s);

    @Named("postFeedId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostFeedDTO toDtoPostFeedId(PostFeed postFeed);

    @Named("postCommentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostCommentDTO toDtoPostCommentId(PostComment postComment);
}
