package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Country;
import com.monsterdam.app.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {}
