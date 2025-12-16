package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.UserEvent;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.UserEventDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserEvent} and its DTO {@link UserEventDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEventMapper extends EntityMapper<UserEventDTO, UserEvent> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    UserEventDTO toDto(UserEvent s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
