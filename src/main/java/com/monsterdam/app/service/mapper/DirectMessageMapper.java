package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.ChatRoom;
import com.monsterdam.app.domain.DirectMessage;
import com.monsterdam.app.service.dto.ChatRoomDTO;
import com.monsterdam.app.service.dto.DirectMessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DirectMessage} and its DTO {@link DirectMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface DirectMessageMapper extends EntityMapper<DirectMessageDTO, DirectMessage> {
    @Mapping(target = "responseTo", source = "responseTo", qualifiedByName = "directMessageId")
    @Mapping(target = "chatRoom", source = "chatRoom", qualifiedByName = "chatRoomId")
    DirectMessageDTO toDto(DirectMessage s);

    @Named("directMessageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DirectMessageDTO toDtoDirectMessageId(DirectMessage directMessage);

    @Named("chatRoomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChatRoomDTO toDtoChatRoomId(ChatRoom chatRoom);
}
