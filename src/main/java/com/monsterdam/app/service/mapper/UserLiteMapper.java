package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Country;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.UserProfile;
import com.monsterdam.app.domain.UserSettings;
import com.monsterdam.app.service.dto.CountryDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import com.monsterdam.app.service.dto.UserProfileDTO;
import com.monsterdam.app.service.dto.UserSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserLite} and its DTO {@link UserLiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserLiteMapper extends EntityMapper<UserLiteDTO, UserLite> {
    @Mapping(target = "profile", source = "profile", qualifiedByName = "userProfileId")
    @Mapping(target = "settings", source = "settings", qualifiedByName = "userSettingsId")
    @Mapping(target = "countryOfBirth", source = "countryOfBirth", qualifiedByName = "countryId")
    UserLiteDTO toDto(UserLite s);

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
}
