package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.LikeMark;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.LikeMarkDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LikeMark} and its DTO {@link LikeMarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikeMarkMapper extends EntityMapper<LikeMarkDTO, LikeMark> {
    @Mapping(target = "liker", source = "liker", qualifiedByName = "userLiteId")
    LikeMarkDTO toDto(LikeMark s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
