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
