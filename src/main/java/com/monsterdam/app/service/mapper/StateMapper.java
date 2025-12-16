package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Country;
import com.monsterdam.app.domain.State;
import com.monsterdam.app.service.dto.CountryDTO;
import com.monsterdam.app.service.dto.StateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link State} and its DTO {@link StateDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StateMapper extends EntityMapper<StateDTO, State> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryId")
    StateDTO toDto(State s);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);
}
