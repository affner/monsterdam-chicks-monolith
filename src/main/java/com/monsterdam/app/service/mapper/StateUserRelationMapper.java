package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.State;
import com.monsterdam.app.domain.StateUserRelation;
import com.monsterdam.app.domain.UserProfile;
import com.monsterdam.app.service.dto.StateDTO;
import com.monsterdam.app.service.dto.StateUserRelationDTO;
import com.monsterdam.app.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StateUserRelation} and its DTO {@link StateUserRelationDTO}.
 */
@Mapper(componentModel = "spring")
public interface StateUserRelationMapper extends EntityMapper<StateUserRelationDTO, StateUserRelation> {
    @Mapping(target = "state", source = "state", qualifiedByName = "stateId")
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    StateUserRelationDTO toDto(StateUserRelation s);

    @Named("stateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StateDTO toDtoStateId(State state);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
