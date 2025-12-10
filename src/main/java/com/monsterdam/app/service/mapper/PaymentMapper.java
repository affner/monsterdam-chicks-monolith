package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Payment;
import com.monsterdam.app.domain.PaymentMethod;
import com.monsterdam.app.domain.PaymentProvider;
import com.monsterdam.app.service.dto.PaymentDTO;
import com.monsterdam.app.service.dto.PaymentMethodDTO;
import com.monsterdam.app.service.dto.PaymentProviderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "method", source = "method", qualifiedByName = "paymentMethodId")
    @Mapping(target = "provider", source = "provider", qualifiedByName = "paymentProviderId")
    PaymentDTO toDto(Payment s);

    @Named("paymentMethodId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentMethodDTO toDtoPaymentMethodId(PaymentMethod paymentMethod);

    @Named("paymentProviderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentProviderDTO toDtoPaymentProviderId(PaymentProvider paymentProvider);
}
