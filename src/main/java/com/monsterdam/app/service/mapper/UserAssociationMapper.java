package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.UserAssociation;
import com.monsterdam.app.service.dto.UserAssociationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAssociation} and its DTO {@link UserAssociationDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAssociationMapper extends EntityMapper<UserAssociationDTO, UserAssociation> {}
