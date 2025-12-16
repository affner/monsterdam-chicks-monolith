package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PaymentProvider;
import com.monsterdam.app.service.dto.PaymentProviderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentProvider} and its DTO {@link PaymentProviderDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentProviderMapper extends EntityMapper<PaymentProviderDTO, PaymentProvider> {}
