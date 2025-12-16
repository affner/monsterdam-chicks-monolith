package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.BookMark;
import com.monsterdam.app.domain.DirectMessage;
import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.BookMarkDTO;
import com.monsterdam.app.service.dto.DirectMessageDTO;
import com.monsterdam.app.service.dto.PostFeedDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookMark} and its DTO {@link BookMarkDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMarkMapper extends EntityMapper<BookMarkDTO, BookMark> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLiteId")
    @Mapping(target = "post", source = "post", qualifiedByName = "postFeedId")
    @Mapping(target = "message", source = "message", qualifiedByName = "directMessageId")
    BookMarkDTO toDto(BookMark s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);

    @Named("postFeedId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PostFeedDTO toDtoPostFeedId(PostFeed postFeed);

    @Named("directMessageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DirectMessageDTO toDtoDirectMessageId(DirectMessage directMessage);
}
