package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.TrialLink;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.TrialLinkDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TrialLink} and its DTO {@link TrialLinkDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrialLinkMapper extends EntityMapper<TrialLinkDTO, TrialLink> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    TrialLinkDTO toDto(TrialLink s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
