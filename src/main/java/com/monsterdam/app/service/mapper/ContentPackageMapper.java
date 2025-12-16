package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.service.dto.ContentPackageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContentPackage} and its DTO {@link ContentPackageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContentPackageMapper extends EntityMapper<ContentPackageDTO, ContentPackage> {}
