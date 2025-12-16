package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.IdentityDocumentReview;
import com.monsterdam.app.service.dto.IdentityDocumentReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IdentityDocumentReview} and its DTO {@link IdentityDocumentReviewDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IdentityDocumentReviewMapper extends EntityMapper<IdentityDocumentReviewDTO, IdentityDocumentReview> {}
