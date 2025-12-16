package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.domain.SingleVideo;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.ContentPackageDTO;
import com.monsterdam.app.service.dto.SingleVideoDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleVideo} and its DTO {@link SingleVideoDTO}.
 */
@Mapper(componentModel = "spring")
public interface SingleVideoMapper extends EntityMapper<SingleVideoDTO, SingleVideo> {
    @Mapping(target = "contentPackage", source = "contentPackage", qualifiedByName = "contentPackageId")
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    SingleVideoDTO toDto(SingleVideo s);

    @Named("contentPackageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentPackageDTO toDtoContentPackageId(ContentPackage contentPackage);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
