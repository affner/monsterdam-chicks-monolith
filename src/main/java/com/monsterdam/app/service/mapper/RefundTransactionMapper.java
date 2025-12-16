package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Payment;
import com.monsterdam.app.domain.RefundTransaction;
import com.monsterdam.app.service.dto.PaymentDTO;
import com.monsterdam.app.service.dto.RefundTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RefundTransaction} and its DTO {@link RefundTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface RefundTransactionMapper extends EntityMapper<RefundTransactionDTO, RefundTransaction> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    RefundTransactionDTO toDto(RefundTransaction s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);
}
