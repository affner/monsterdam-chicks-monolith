package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Feedback;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.FeedbackDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Feedback} and its DTO {@link FeedbackDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedbackMapper extends EntityMapper<FeedbackDTO, Feedback> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    FeedbackDTO toDto(Feedback s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
