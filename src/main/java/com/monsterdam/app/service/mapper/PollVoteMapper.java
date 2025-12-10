package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PollOption;
import com.monsterdam.app.domain.PollVote;
import com.monsterdam.app.service.dto.PollOptionDTO;
import com.monsterdam.app.service.dto.PollVoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PollVote} and its DTO {@link PollVoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PollVoteMapper extends EntityMapper<PollVoteDTO, PollVote> {
    @Mapping(target = "pollOption", source = "pollOption", qualifiedByName = "pollOptionId")
    PollVoteDTO toDto(PollVote s);

    @Named("pollOptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PollOptionDTO toDtoPollOptionId(PollOption pollOption);
}
