package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.HelpQuestion;
import com.monsterdam.app.domain.HelpSubcategory;
import com.monsterdam.app.service.dto.HelpQuestionDTO;
import com.monsterdam.app.service.dto.HelpSubcategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpQuestion} and its DTO {@link HelpQuestionDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HelpQuestionMapper extends EntityMapper<HelpQuestionDTO, HelpQuestion> {
    @Mapping(target = "subcategory", source = "subcategory", qualifiedByName = "helpSubcategoryId")
    HelpQuestionDTO toDto(HelpQuestion s);

    @Named("helpSubcategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HelpSubcategoryDTO toDtoHelpSubcategoryId(HelpSubcategory helpSubcategory);
}
