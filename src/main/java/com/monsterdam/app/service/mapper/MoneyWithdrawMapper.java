package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.MoneyWithdraw;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.MoneyWithdrawDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MoneyWithdraw} and its DTO {@link MoneyWithdrawDTO}.
 */
@Mapper(componentModel = "spring")
public interface MoneyWithdrawMapper extends EntityMapper<MoneyWithdrawDTO, MoneyWithdraw> {
    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    MoneyWithdraw toEntity(MoneyWithdrawDTO dto);

    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget MoneyWithdraw entity, MoneyWithdrawDTO dto);

    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    MoneyWithdrawDTO toDto(MoneyWithdraw s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
