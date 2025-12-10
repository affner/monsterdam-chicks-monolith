package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.MoneyGift;
import com.monsterdam.app.domain.Payment;
import com.monsterdam.app.service.dto.MoneyGiftDTO;
import com.monsterdam.app.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MoneyGift} and its DTO {@link MoneyGiftDTO}.
 */
@Mapper(componentModel = "spring")
public interface MoneyGiftMapper extends EntityMapper<MoneyGiftDTO, MoneyGift> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    MoneyGiftDTO toDto(MoneyGift s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);
}
