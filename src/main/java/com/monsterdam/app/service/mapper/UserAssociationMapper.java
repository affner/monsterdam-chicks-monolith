package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.UserAssociation;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.UserAssociationDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAssociation} and its DTO {@link UserAssociationDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAssociationMapper extends EntityMapper<UserAssociationDTO, UserAssociation> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userLiteId")
    UserAssociationDTO toDto(UserAssociation s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
