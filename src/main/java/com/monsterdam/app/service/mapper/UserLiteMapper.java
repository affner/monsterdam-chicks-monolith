package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.ChatRoom;
import com.monsterdam.app.domain.Country;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.UserProfile;
import com.monsterdam.app.domain.UserSettings;
import com.monsterdam.app.service.dto.ChatRoomDTO;
import com.monsterdam.app.service.dto.CountryDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import com.monsterdam.app.service.dto.UserProfileDTO;
import com.monsterdam.app.service.dto.UserSettingsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserLite} and its DTO {@link UserLiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserLiteMapper extends EntityMapper<UserLiteDTO, UserLite> {
    @Mapping(target = "profile", source = "profile", qualifiedByName = "userProfileId")
    @Mapping(target = "settings", source = "settings", qualifiedByName = "userSettingsId")
    @Mapping(target = "countryOfBirth", source = "countryOfBirth", qualifiedByName = "countryId")
    @Mapping(target = "chatRooms", source = "chatRooms", qualifiedByName = "chatRoomIdSet")
    UserLiteDTO toDto(UserLite s);

    @Mapping(target = "chatRooms", ignore = true)
    @Mapping(target = "removeChatRooms", ignore = true)
    UserLite toEntity(UserLiteDTO userLiteDTO);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("userSettingsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserSettingsDTO toDtoUserSettingsId(UserSettings userSettings);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);

    @Named("chatRoomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChatRoomDTO toDtoChatRoomId(ChatRoom chatRoom);

    @Named("chatRoomIdSet")
    default Set<ChatRoomDTO> toDtoChatRoomIdSet(Set<ChatRoom> chatRoom) {
        return chatRoom.stream().map(this::toDtoChatRoomId).collect(Collectors.toSet());
    }
}
