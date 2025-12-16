package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Payment;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.ViewerWallet;
import com.monsterdam.app.service.dto.PaymentDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import com.monsterdam.app.service.dto.ViewerWalletDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ViewerWallet} and its DTO {@link ViewerWalletDTO}.
 */
@Mapper(componentModel = "spring")
public interface ViewerWalletMapper extends EntityMapper<ViewerWalletDTO, ViewerWallet> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    @Mapping(target = "viewer", source = "viewer", qualifiedByName = "userLiteId")
    ViewerWalletDTO toDto(ViewerWallet s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
