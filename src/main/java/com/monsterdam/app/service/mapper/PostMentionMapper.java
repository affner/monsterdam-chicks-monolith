package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.domain.PostMention;
import com.monsterdam.app.service.dto.PostFeedDTO;
import com.monsterdam.app.service.dto.PostMentionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostMention} and its DTO {@link PostMentionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostMentionMapper extends EntityMapper<PostMentionDTO, PostMention> {
    @Mapping(target = "originPost", source = "originPost", qualifiedByName = "postFeedId")
    PostMentionDTO toDto(PostMention s);

    @Named("postFeedId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostFeedDTO toDtoPostFeedId(PostFeed postFeed);
}
