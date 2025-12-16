package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PollOption;
import com.monsterdam.app.domain.PostPoll;
import com.monsterdam.app.service.dto.PollOptionDTO;
import com.monsterdam.app.service.dto.PostPollDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PollOption} and its DTO {@link PollOptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PollOptionMapper extends EntityMapper<PollOptionDTO, PollOption> {
    @Mapping(target = "poll", source = "poll", qualifiedByName = "postPollId")
    PollOptionDTO toDto(PollOption s);

    @Named("postPollId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostPollDTO toDtoPostPollId(PostPoll postPoll);
}
