package com.monsterdam.app.service.mapper.bff;

import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.UserProfile;
import com.monsterdam.app.service.dto.bff.ContentSetDTO;
import com.monsterdam.app.service.dto.bff.CreatorProfileDTO;
import com.monsterdam.app.service.dto.bff.MediaItemDTO;
import com.monsterdam.app.service.dto.bff.MenuDTO;
import com.monsterdam.app.service.dto.bff.SingleSetDTO;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ViewerBffMapper {

    public MenuDTO toMenu(List<String> roles, List<String> sections) {
        return new MenuDTO().roles(roles).sections(sections);
    }

    public SingleSetDTO toSingleSet(
        ContentPackage contentPackage,
        String title,
        String description,
        UserLite creator,
        Double rating,
        String coverUrl,
        boolean unlocked,
        boolean canUnlock,
        String currencyFallback
    ) {
        SingleSetDTO dto = new SingleSetDTO();
        dto.setId(contentPackage.getId());
        dto.setTitle(title);
        dto.setDescriptionShort(description);
        dto.setCreatorId(creator != null ? creator.getId() : null);
        dto.setCreatorName(resolveCreatorName(creator));
        dto.setCoverUrl(coverUrl);
        dto.setIsFree(Boolean.FALSE.equals(contentPackage.getIsPaidContent()) || contentPackage.getAmount() == null);
        dto.setPrice(contentPackage.getAmount());
        dto.setCurrency(currencyFallback);
        dto.setRating(rating);
        dto.setCreatedDate(contentPackage.getCreatedDate());
        dto.setUnlocked(unlocked);
        dto.setCanUnlock(canUnlock);
        return dto;
    }

    public ContentSetDTO toContentSet(
        ContentPackage contentPackage,
        String title,
        String description,
        boolean unlocked,
        boolean canUnlock,
        String downloadUrl,
        List<MediaItemDTO> items,
        String currencyFallback
    ) {
        ContentSetDTO dto = new ContentSetDTO();
        dto.setId(contentPackage.getId());
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setUnlocked(unlocked);
        dto.setCanUnlock(canUnlock);
        dto.setPrice(contentPackage.getAmount());
        dto.setCurrency(currencyFallback);
        dto.setDownloadUrl(downloadUrl);
        dto.setItems(items);
        return dto;
    }

    public CreatorProfileDTO toCreatorProfile(UserLite creator, UserProfile profile, List<String> links, List<SingleSetDTO> sets) {
        CreatorProfileDTO dto = new CreatorProfileDTO();
        dto.setCreatorId(creator.getId());
        dto.setDisplayName(resolveCreatorName(creator));
        dto.setBioShort(profile != null ? profile.getBiography() : null);
        dto.setAvatarUrl(
            profile != null && StringUtils.hasText(profile.getProfilePhotoS3Key())
                ? profile.getProfilePhotoS3Key()
                : creator.getThumbnailS3Key()
        );
        dto.setCoverUrl(profile != null ? profile.getCoverPhotoS3Key() : null);
        dto.setLinks(links);
        dto.setSets(sets);
        return dto;
    }

    private String resolveCreatorName(UserLite creator) {
        if (creator == null) {
            return null;
        }
        if (StringUtils.hasText(creator.getFullName())) {
            return creator.getFullName();
        }
        return creator.getNickName();
    }
}
