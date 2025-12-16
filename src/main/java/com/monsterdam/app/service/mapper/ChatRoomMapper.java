package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.ChatRoom;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.ChatRoomDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatRoom} and its DTO {@link ChatRoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatRoomMapper extends EntityMapper<ChatRoomDTO, ChatRoom> {
    @Mapping(target = "participants", source = "participants", qualifiedByName = "userLiteIdSet")
    ChatRoomDTO toDto(ChatRoom s);

    @Mapping(target = "removeParticipants", ignore = true)
    ChatRoom toEntity(ChatRoomDTO chatRoomDTO);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);

    @Named("userLiteIdSet")
    default Set<UserLiteDTO> toDtoUserLiteIdSet(Set<UserLite> userLite) {
        return userLite.stream().map(this::toDtoUserLiteId).collect(Collectors.toSet());
    }
}
