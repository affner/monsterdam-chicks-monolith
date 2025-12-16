package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.SpecialAward;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.SpecialAwardDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialAward} and its DTO {@link SpecialAwardDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecialAwardMapper extends EntityMapper<SpecialAwardDTO, SpecialAward> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    @Mapping(target = "viewer", source = "viewer", qualifiedByName = "userLiteId")
    SpecialAwardDTO toDto(SpecialAward s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
