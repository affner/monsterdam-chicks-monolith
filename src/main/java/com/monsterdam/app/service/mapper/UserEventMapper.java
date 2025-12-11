package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.UserEvent;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.UserEventDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserEvent} and its DTO {@link UserEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserEventMapper extends EntityMapper<UserEventDTO, UserEvent> {
    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    UserEvent toEntity(UserEventDTO dto);

    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget UserEvent entity, UserEventDTO dto);

    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    UserEventDTO toDto(UserEvent s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
