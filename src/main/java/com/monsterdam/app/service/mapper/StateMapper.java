package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.State;
import com.monsterdam.app.service.dto.StateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link State} and its DTO {@link StateDTO}.
 */
@Mapper(componentModel = "spring")
public interface StateMapper extends EntityMapper<StateDTO, State> {}
