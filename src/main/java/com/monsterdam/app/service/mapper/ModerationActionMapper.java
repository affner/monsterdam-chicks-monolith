package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.ModerationAction;
import com.monsterdam.app.service.dto.ModerationActionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModerationAction} and its DTO {@link ModerationActionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModerationActionMapper extends EntityMapper<ModerationActionDTO, ModerationAction> {}
