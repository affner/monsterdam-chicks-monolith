package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.domain.Payment;
import com.monsterdam.app.domain.PurchasedContent;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.ContentPackageDTO;
import com.monsterdam.app.service.dto.PaymentDTO;
import com.monsterdam.app.service.dto.PurchasedContentDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchasedContent} and its DTO {@link PurchasedContentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PurchasedContentMapper extends EntityMapper<PurchasedContentDTO, PurchasedContent> {
    @Mapping(target = "contentPackage", source = "contentPackage", qualifiedByName = "contentPackageId")
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    @Mapping(target = "viewer", source = "viewer", qualifiedByName = "userLiteId")
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    PurchasedContentDTO toDto(PurchasedContent s);

    @Named("contentPackageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentPackageDTO toDtoContentPackageId(ContentPackage contentPackage);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
