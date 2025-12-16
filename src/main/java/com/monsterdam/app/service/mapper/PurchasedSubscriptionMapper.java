package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Payment;
import com.monsterdam.app.domain.PurchasedSubscription;
import com.monsterdam.app.domain.SubscriptionPlanOffer;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.PaymentDTO;
import com.monsterdam.app.service.dto.PurchasedSubscriptionDTO;
import com.monsterdam.app.service.dto.SubscriptionPlanOfferDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchasedSubscription} and its DTO {@link PurchasedSubscriptionDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchasedSubscriptionMapper extends EntityMapper<PurchasedSubscriptionDTO, PurchasedSubscription> {
    @Mapping(target = "viewer", source = "viewer", qualifiedByName = "userLiteId")
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    @Mapping(target = "subscriptionPlanOffer", source = "subscriptionPlanOffer", qualifiedByName = "subscriptionPlanOfferId")
    PurchasedSubscriptionDTO toDto(PurchasedSubscription s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);

    @Named("subscriptionPlanOfferId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriptionPlanOfferDTO toDtoSubscriptionPlanOfferId(SubscriptionPlanOffer subscriptionPlanOffer);
}
