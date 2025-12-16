package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PollOption;
import com.monsterdam.app.domain.PollVote;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.PollOptionDTO;
import com.monsterdam.app.service.dto.PollVoteDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PollVote} and its DTO {@link PollVoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PollVoteMapper extends EntityMapper<PollVoteDTO, PollVote> {
    @Mapping(target = "pollOption", source = "pollOption", qualifiedByName = "pollOptionId")
    @Mapping(target = "voter", source = "voter", qualifiedByName = "userLiteId")
    PollVoteDTO toDto(PollVote s);

    @Named("pollOptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PollOptionDTO toDtoPollOptionId(PollOption pollOption);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
