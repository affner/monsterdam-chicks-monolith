package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.ChatRoom;
import com.monsterdam.app.service.dto.ChatRoomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatRoom} and its DTO {@link ChatRoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatRoomMapper extends EntityMapper<ChatRoomDTO, ChatRoom> {}
