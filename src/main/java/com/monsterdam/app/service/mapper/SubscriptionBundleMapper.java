package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.SubscriptionBundle;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.SubscriptionBundleDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionBundle} and its DTO {@link SubscriptionBundleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionBundleMapper extends EntityMapper<SubscriptionBundleDTO, SubscriptionBundle> {
    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    SubscriptionBundle toEntity(SubscriptionBundleDTO dto);

    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget SubscriptionBundle entity, SubscriptionBundleDTO dto);

    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    SubscriptionBundleDTO toDto(SubscriptionBundle s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
