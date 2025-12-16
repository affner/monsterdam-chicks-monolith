package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Payment;
import com.monsterdam.app.domain.PaymentMethod;
import com.monsterdam.app.domain.PaymentProvider;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.PaymentDTO;
import com.monsterdam.app.service.dto.PaymentMethodDTO;
import com.monsterdam.app.service.dto.PaymentProviderDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "method", source = "method", qualifiedByName = "paymentMethodId")
    @Mapping(target = "provider", source = "provider", qualifiedByName = "paymentProviderId")
    @Mapping(target = "viewer", source = "viewer", qualifiedByName = "userLiteId")
    PaymentDTO toDto(Payment s);

    @Named("paymentMethodId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentMethodDTO toDtoPaymentMethodId(PaymentMethod paymentMethod);

    @Named("paymentProviderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentProviderDTO toDtoPaymentProviderId(PaymentProvider paymentProvider);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
