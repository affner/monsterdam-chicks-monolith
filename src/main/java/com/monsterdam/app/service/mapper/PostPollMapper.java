package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.domain.PostPoll;
import com.monsterdam.app.service.dto.PostFeedDTO;
import com.monsterdam.app.service.dto.PostPollDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostPoll} and its DTO {@link PostPollDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostPollMapper extends EntityMapper<PostPollDTO, PostPoll> {
    @Mapping(target = "post", source = "post", qualifiedByName = "postFeedId")
    PostPollDTO toDto(PostPoll s);

    @Named("postFeedId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostFeedDTO toDtoPostFeedId(PostFeed postFeed);
}
