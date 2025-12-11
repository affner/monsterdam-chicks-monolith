package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.SpecialAward;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.SpecialAwardDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialAward} and its DTO {@link SpecialAwardDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialAwardMapper extends EntityMapper<SpecialAwardDTO, SpecialAward> {
    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    @Mapping(target = "viewer.profile.user", ignore = true)
    @Mapping(target = "viewer.settings.user", ignore = true)
    SpecialAward toEntity(SpecialAwardDTO dto);

    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    @Mapping(target = "viewer.profile.user", ignore = true)
    @Mapping(target = "viewer.settings.user", ignore = true)
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget SpecialAward entity, SpecialAwardDTO dto);

    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    @Mapping(target = "viewer", source = "viewer", qualifiedByName = "userLiteId")
    SpecialAwardDTO toDto(SpecialAward s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
