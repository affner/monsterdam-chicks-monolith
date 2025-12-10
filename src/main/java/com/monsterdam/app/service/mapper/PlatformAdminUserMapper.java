package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PlatformAdminUser;
import com.monsterdam.app.service.dto.PlatformAdminUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlatformAdminUser} and its DTO {@link PlatformAdminUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlatformAdminUserMapper extends EntityMapper<PlatformAdminUserDTO, PlatformAdminUser> {}
