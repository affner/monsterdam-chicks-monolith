package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.SubscriptionBundle;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.SubscriptionBundleDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionBundle} and its DTO {@link SubscriptionBundleDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionBundleMapper extends EntityMapper<SubscriptionBundleDTO, SubscriptionBundle> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    SubscriptionBundleDTO toDto(SubscriptionBundle s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
