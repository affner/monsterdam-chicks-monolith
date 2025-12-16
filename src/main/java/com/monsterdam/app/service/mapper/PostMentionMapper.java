package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.domain.PostMention;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.PostFeedDTO;
import com.monsterdam.app.service.dto.PostMentionDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostMention} and its DTO {@link PostMentionDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMentionMapper extends EntityMapper<PostMentionDTO, PostMention> {
    @Mapping(target = "originPost", source = "originPost", qualifiedByName = "postFeedId")
    @Mapping(target = "mentionedUser", source = "mentionedUser", qualifiedByName = "userLiteId")
    PostMentionDTO toDto(PostMention s);

    @Named("postFeedId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostFeedDTO toDtoPostFeedId(PostFeed postFeed);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
