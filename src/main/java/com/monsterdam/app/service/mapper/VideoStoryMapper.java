package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.VideoStory;
import com.monsterdam.app.service.dto.UserLiteDTO;
import com.monsterdam.app.service.dto.VideoStoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VideoStory} and its DTO {@link VideoStoryDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VideoStoryMapper extends EntityMapper<VideoStoryDTO, VideoStory> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    VideoStoryDTO toDto(VideoStory s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
