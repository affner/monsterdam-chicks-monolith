package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.AssistanceTicket;
import com.monsterdam.app.domain.IdentityDocumentReview;
import com.monsterdam.app.domain.PlatformAdminUser;
import com.monsterdam.app.domain.UserReport;
import com.monsterdam.app.service.dto.AssistanceTicketDTO;
import com.monsterdam.app.service.dto.IdentityDocumentReviewDTO;
import com.monsterdam.app.service.dto.PlatformAdminUserDTO;
import com.monsterdam.app.service.dto.UserReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssistanceTicket} and its DTO {@link AssistanceTicketDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssistanceTicketMapper extends EntityMapper<AssistanceTicketDTO, AssistanceTicket> {
    @Mapping(target = "assignedAdmin", source = "assignedAdmin", qualifiedByName = "platformAdminUserId")
    @Mapping(target = "report", source = "report", qualifiedByName = "userReportId")
    @Mapping(target = "documentsReview", source = "documentsReview", qualifiedByName = "identityDocumentReviewId")
    AssistanceTicketDTO toDto(AssistanceTicket s);

    @Named("platformAdminUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlatformAdminUserDTO toDtoPlatformAdminUserId(PlatformAdminUser platformAdminUser);

    @Named("userReportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserReportDTO toDtoUserReportId(UserReport userReport);

    @Named("identityDocumentReviewId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IdentityDocumentReviewDTO toDtoIdentityDocumentReviewId(IdentityDocumentReview identityDocumentReview);
}
