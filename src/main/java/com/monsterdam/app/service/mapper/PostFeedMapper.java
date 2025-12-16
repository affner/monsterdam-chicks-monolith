package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.PostFeedDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostFeed} and its DTO {@link PostFeedDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostFeedMapper extends EntityMapper<PostFeedDTO, PostFeed> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    PostFeedDTO toDto(PostFeed s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
