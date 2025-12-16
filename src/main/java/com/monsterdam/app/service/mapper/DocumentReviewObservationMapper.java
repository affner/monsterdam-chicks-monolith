package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.DocumentReviewObservation;
import com.monsterdam.app.domain.IdentityDocumentReview;
import com.monsterdam.app.service.dto.DocumentReviewObservationDTO;
import com.monsterdam.app.service.dto.IdentityDocumentReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentReviewObservation} and its DTO {@link DocumentReviewObservationDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentReviewObservationMapper extends EntityMapper<DocumentReviewObservationDTO, DocumentReviewObservation> {
    @Mapping(target = "review", source = "review", qualifiedByName = "identityDocumentReviewId")
    DocumentReviewObservationDTO toDto(DocumentReviewObservation s);

    @Named("identityDocumentReviewId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdentityDocumentReviewDTO toDtoIdentityDocumentReviewId(IdentityDocumentReview identityDocumentReview);
}
