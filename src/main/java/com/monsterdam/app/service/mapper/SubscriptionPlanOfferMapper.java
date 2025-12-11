package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.SubscriptionPlanOffer;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.SubscriptionPlanOfferDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionPlanOffer} and its DTO {@link SubscriptionPlanOfferDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionPlanOfferMapper extends EntityMapper<SubscriptionPlanOfferDTO, SubscriptionPlanOffer> {
    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    SubscriptionPlanOffer toEntity(SubscriptionPlanOfferDTO dto);

    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget SubscriptionPlanOffer entity, SubscriptionPlanOfferDTO dto);

    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    SubscriptionPlanOfferDTO toDto(SubscriptionPlanOffer s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
