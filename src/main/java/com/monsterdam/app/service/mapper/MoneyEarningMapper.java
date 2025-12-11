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
    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    MoneyEarning toEntity(MoneyEarningDTO dto);

    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget MoneyEarning entity, MoneyEarningDTO dto);

    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    MoneyEarningDTO toDto(MoneyEarning s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
