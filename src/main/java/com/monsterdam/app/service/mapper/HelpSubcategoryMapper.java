package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.HelpCategory;
import com.monsterdam.app.domain.HelpSubcategory;
import com.monsterdam.app.service.dto.HelpCategoryDTO;
import com.monsterdam.app.service.dto.HelpSubcategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpSubcategory} and its DTO {@link HelpSubcategoryDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HelpSubcategoryMapper extends EntityMapper<HelpSubcategoryDTO, HelpSubcategory> {
    @Mapping(target = "category", source = "category", qualifiedByName = "helpCategoryId")
    HelpSubcategoryDTO toDto(HelpSubcategory s);

    @Named("helpCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HelpCategoryDTO toDtoHelpCategoryId(HelpCategory helpCategory);
}
