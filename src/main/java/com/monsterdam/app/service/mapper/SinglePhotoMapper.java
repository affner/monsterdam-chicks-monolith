package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.domain.SinglePhoto;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.ContentPackageDTO;
import com.monsterdam.app.service.dto.SinglePhotoDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SinglePhoto} and its DTO {@link SinglePhotoDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SinglePhotoMapper extends EntityMapper<SinglePhotoDTO, SinglePhoto> {
    @Mapping(target = "contentPackage", source = "contentPackage", qualifiedByName = "contentPackageId")
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    SinglePhotoDTO toDto(SinglePhoto s);

    @Named("contentPackageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentPackageDTO toDtoContentPackageId(ContentPackage contentPackage);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
