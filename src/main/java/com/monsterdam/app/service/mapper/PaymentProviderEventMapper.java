package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Payment;
import com.monsterdam.app.domain.PaymentProviderEvent;
import com.monsterdam.app.service.dto.PaymentDTO;
import com.monsterdam.app.service.dto.PaymentProviderEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentProviderEvent} and its DTO {@link PaymentProviderEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentProviderEventMapper extends EntityMapper<PaymentProviderEventDTO, PaymentProviderEvent> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    PaymentProviderEventDTO toDto(PaymentProviderEvent s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);
}
