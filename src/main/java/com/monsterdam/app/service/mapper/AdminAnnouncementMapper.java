package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.AdminAnnouncement;
import com.monsterdam.app.service.dto.AdminAnnouncementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminAnnouncement} and its DTO {@link AdminAnnouncementDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminAnnouncementMapper extends EntityMapper<AdminAnnouncementDTO, AdminAnnouncement> {}
