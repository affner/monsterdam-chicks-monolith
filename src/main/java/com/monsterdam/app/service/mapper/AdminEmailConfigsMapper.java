package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.AdminEmailConfigs;
import com.monsterdam.app.service.dto.AdminEmailConfigsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminEmailConfigs} and its DTO {@link AdminEmailConfigsDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminEmailConfigsMapper extends EntityMapper<AdminEmailConfigsDTO, AdminEmailConfigs> {}
