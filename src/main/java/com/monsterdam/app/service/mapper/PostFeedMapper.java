package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.service.dto.PostFeedDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostFeed} and its DTO {@link PostFeedDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostFeedMapper extends EntityMapper<PostFeedDTO, PostFeed> {}
