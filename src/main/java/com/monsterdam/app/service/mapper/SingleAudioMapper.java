package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.domain.SingleAudio;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.ContentPackageDTO;
import com.monsterdam.app.service.dto.SingleAudioDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SingleAudio} and its DTO {@link SingleAudioDTO}.
 */
@Mapper(componentModel = "spring")
public interface SingleAudioMapper extends EntityMapper<SingleAudioDTO, SingleAudio> {
    @Mapping(target = "contentPackage", source = "contentPackage", qualifiedByName = "contentPackageId")
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    SingleAudioDTO toDto(SingleAudio s);

    @Named("contentPackageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentPackageDTO toDtoContentPackageId(ContentPackage contentPackage);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
