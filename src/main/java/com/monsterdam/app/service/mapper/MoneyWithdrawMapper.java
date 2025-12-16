package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.MoneyWithdraw;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.MoneyWithdrawDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MoneyWithdraw} and its DTO {@link MoneyWithdrawDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MoneyWithdrawMapper extends EntityMapper<MoneyWithdrawDTO, MoneyWithdraw> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    MoneyWithdrawDTO toDto(MoneyWithdraw s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
