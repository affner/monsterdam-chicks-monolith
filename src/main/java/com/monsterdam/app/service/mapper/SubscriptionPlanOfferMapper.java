package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.SubscriptionPlanOffer;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.SubscriptionPlanOfferDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionPlanOffer} and its DTO {@link SubscriptionPlanOfferDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionPlanOfferMapper extends EntityMapper<SubscriptionPlanOfferDTO, SubscriptionPlanOffer> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    SubscriptionPlanOfferDTO toDto(SubscriptionPlanOffer s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
