package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.HashTag;
import com.monsterdam.app.service.dto.HashTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HashTag} and its DTO {@link HashTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface HashTagMapper extends EntityMapper<HashTagDTO, HashTag> {}
