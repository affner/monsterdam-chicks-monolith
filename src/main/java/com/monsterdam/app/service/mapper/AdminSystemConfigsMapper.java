package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.AdminSystemConfigs;
import com.monsterdam.app.service.dto.AdminSystemConfigsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminSystemConfigs} and its DTO {@link AdminSystemConfigsDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminSystemConfigsMapper extends EntityMapper<AdminSystemConfigsDTO, AdminSystemConfigs> {}
