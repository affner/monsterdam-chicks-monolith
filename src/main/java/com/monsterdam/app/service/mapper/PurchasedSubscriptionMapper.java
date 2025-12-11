package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Payment;
import com.monsterdam.app.domain.PurchasedSubscription;
import com.monsterdam.app.domain.SubscriptionPlanOffer;
import com.monsterdam.app.service.dto.PaymentDTO;
import com.monsterdam.app.service.dto.PurchasedSubscriptionDTO;
import com.monsterdam.app.service.dto.SubscriptionPlanOfferDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchasedSubscription} and its DTO {@link PurchasedSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchasedSubscriptionMapper extends EntityMapper<PurchasedSubscriptionDTO, PurchasedSubscription> {
    @Override
    @Mapping(target = "subscriptionPlanOffer.creator.profile.user", ignore = true)
    @Mapping(target = "subscriptionPlanOffer.creator.settings.user", ignore = true)
    PurchasedSubscription toEntity(PurchasedSubscriptionDTO dto);

    @Override
    @Mapping(target = "subscriptionPlanOffer.creator.profile.user", ignore = true)
    @Mapping(target = "subscriptionPlanOffer.creator.settings.user", ignore = true)
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget PurchasedSubscription entity, PurchasedSubscriptionDTO dto);

    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    @Mapping(target = "subscriptionPlanOffer", source = "subscriptionPlanOffer", qualifiedByName = "subscriptionPlanOfferId")
    PurchasedSubscriptionDTO toDto(PurchasedSubscription s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);

    @Named("subscriptionPlanOfferId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriptionPlanOfferDTO toDtoSubscriptionPlanOfferId(SubscriptionPlanOffer subscriptionPlanOffer);
}
