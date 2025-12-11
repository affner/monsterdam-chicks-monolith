package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.UserSettings;
import com.monsterdam.app.service.dto.UserSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSettings} and its DTO {@link UserSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSettingsMapper extends EntityMapper<UserSettingsDTO, UserSettings> {
    @Override
    @Mapping(target = "user", ignore = true)
    UserSettings toEntity(UserSettingsDTO dto);

    @Override
    @Mapping(target = "user", ignore = true)
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget UserSettings entity, UserSettingsDTO dto);
}
