package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Country;
import com.monsterdam.app.domain.TaxInfo;
import com.monsterdam.app.service.dto.CountryDTO;
import com.monsterdam.app.service.dto.TaxInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaxInfo} and its DTO {@link TaxInfoDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaxInfoMapper extends EntityMapper<TaxInfoDTO, TaxInfo> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryId")
    TaxInfoDTO toDto(TaxInfo s);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);
}
