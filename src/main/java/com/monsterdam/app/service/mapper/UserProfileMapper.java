package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.State;
import com.monsterdam.app.domain.UserProfile;
import com.monsterdam.app.service.dto.StateDTO;
import com.monsterdam.app.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Override
    @Mapping(target = "user", ignore = true)
    UserProfile toEntity(UserProfileDTO dto);

    @Override
    @Mapping(target = "user", ignore = true)
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget UserProfile entity, UserProfileDTO dto);

    @Mapping(target = "stateOfResidence", source = "stateOfResidence", qualifiedByName = "stateId")
    UserProfileDTO toDto(UserProfile s);

    @Named("stateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StateDTO toDtoStateId(State state);
}
