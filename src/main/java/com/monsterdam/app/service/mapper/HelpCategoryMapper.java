package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.HelpCategory;
import com.monsterdam.app.service.dto.HelpCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpCategory} and its DTO {@link HelpCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpCategoryMapper extends EntityMapper<HelpCategoryDTO, HelpCategory> {}
