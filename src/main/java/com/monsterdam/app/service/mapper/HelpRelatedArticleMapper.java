package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.HelpRelatedArticle;
import com.monsterdam.app.service.dto.HelpRelatedArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpRelatedArticle} and its DTO {@link HelpRelatedArticleDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HelpRelatedArticleMapper extends EntityMapper<HelpRelatedArticleDTO, HelpRelatedArticle> {}
