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
    @Mapping(target = "stateOfResidence", source = "stateOfResidence", qualifiedByName = "stateId")
    UserProfileDTO toDto(UserProfile s);

    @Named("stateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StateDTO toDtoStateId(State state);
}
