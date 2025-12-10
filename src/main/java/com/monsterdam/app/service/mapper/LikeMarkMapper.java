package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.LikeMark;
import com.monsterdam.app.service.dto.LikeMarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeMark} and its DTO {@link LikeMarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikeMarkMapper extends EntityMapper<LikeMarkDTO, LikeMark> {}
