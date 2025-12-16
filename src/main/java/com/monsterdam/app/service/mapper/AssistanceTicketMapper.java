package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.AssistanceTicket;
import com.monsterdam.app.domain.IdentityDocumentReview;
import com.monsterdam.app.domain.PlatformAdminUser;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.AssistanceTicketDTO;
import com.monsterdam.app.service.dto.IdentityDocumentReviewDTO;
import com.monsterdam.app.service.dto.PlatformAdminUserDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssistanceTicket} and its DTO {@link AssistanceTicketDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssistanceTicketMapper extends EntityMapper<AssistanceTicketDTO, AssistanceTicket> {
    @Mapping(target = "assignedAdmin", source = "assignedAdmin", qualifiedByName = "platformAdminUserId")
    @Mapping(target = "documentsReview", source = "documentsReview", qualifiedByName = "identityDocumentReviewId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLiteId")
    AssistanceTicketDTO toDto(AssistanceTicket s);

    @Named("platformAdminUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlatformAdminUserDTO toDtoPlatformAdminUserId(PlatformAdminUser platformAdminUser);

    @Named("identityDocumentReviewId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdentityDocumentReviewDTO toDtoIdentityDocumentReviewId(IdentityDocumentReview identityDocumentReview);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
