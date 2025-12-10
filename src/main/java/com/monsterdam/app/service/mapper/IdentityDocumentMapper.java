package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.IdentityDocument;
import com.monsterdam.app.domain.IdentityDocumentReview;
import com.monsterdam.app.service.dto.IdentityDocumentDTO;
import com.monsterdam.app.service.dto.IdentityDocumentReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IdentityDocument} and its DTO {@link IdentityDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface IdentityDocumentMapper extends EntityMapper<IdentityDocumentDTO, IdentityDocument> {
    @Mapping(target = "review", source = "review", qualifiedByName = "identityDocumentReviewId")
    IdentityDocumentDTO toDto(IdentityDocument s);

    @Named("identityDocumentReviewId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdentityDocumentReviewDTO toDtoIdentityDocumentReviewId(IdentityDocumentReview identityDocumentReview);
}
