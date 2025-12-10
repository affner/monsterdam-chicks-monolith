package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.MoneyEarning;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.MoneyEarningDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MoneyEarning} and its DTO {@link MoneyEarningDTO}.
 */
@Mapper(componentModel = "spring")
public interface MoneyEarningMapper extends EntityMapper<MoneyEarningDTO, MoneyEarning> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    MoneyEarningDTO toDto(MoneyEarning s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
