package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.PersonalSocialLinks;
import com.monsterdam.app.domain.SocialNetwork;
import com.monsterdam.app.domain.UserProfile;
import com.monsterdam.app.service.dto.PersonalSocialLinksDTO;
import com.monsterdam.app.service.dto.SocialNetworkDTO;
import com.monsterdam.app.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PersonalSocialLinks} and its DTO {@link PersonalSocialLinksDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonalSocialLinksMapper extends EntityMapper<PersonalSocialLinksDTO, PersonalSocialLinks> {
    @Mapping(target = "socialNetwork", source = "socialNetwork", qualifiedByName = "socialNetworkId")
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    PersonalSocialLinksDTO toDto(PersonalSocialLinks s);

    @Named("socialNetworkId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SocialNetworkDTO toDtoSocialNetworkId(SocialNetwork socialNetwork);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
