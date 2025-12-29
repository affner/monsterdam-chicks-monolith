package com.monsterdam.app.service.bff;

import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.domain.User;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.repository.ContentPackageRepository;
import com.monsterdam.app.repository.UserLiteRepository;
import com.monsterdam.app.security.AuthoritiesConstants;
import com.monsterdam.app.service.dto.bff.ModelDto;
import com.monsterdam.app.service.dto.bff.PackageDto;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.Locale;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service class implementing the browse logic for models and packages.  It
 * hides the details of the persistence layer and maps domain entities to
 * lightweight DTOs.  More advanced filtering (e.g. by style) can be
 * implemented here using Specifications or QueryDSL.
 */
@Service
public class ModelBrowseService {

    private final UserLiteRepository userLiteRepository;
    private final ContentPackageRepository contentPackageRepository;
    private final MediaTokenService mediaTokenService;

    public ModelBrowseService(
        UserLiteRepository userLiteRepository,
        ContentPackageRepository contentPackageRepository,
        MediaTokenService mediaTokenService
    ) {
        this.userLiteRepository = userLiteRepository;
        this.contentPackageRepository = contentPackageRepository;
        this.mediaTokenService = mediaTokenService;
    }

    /**
     * Search for models matching the given criteria.
     *
     * @param query the free‑text search string to match against nickName and fullName
     * @param style an optional style filter (not yet implemented)
     * @param pageable pagination information
     * @return a page of ModelDto objects
     */
    public Page<ModelDto> searchModels(String query, String style, Pageable pageable) {
        Specification<UserLite> spec = Specification.where(null);

        spec = spec.and(hasCreatorRole());

        if (StringUtils.hasText(query)) {
            String like = "%" + query.toLowerCase(Locale.ROOT).trim() + "%";
            spec = spec.and((root, q, cb) -> {
                var nick = cb.like(cb.lower(root.get("nickName")), like);
                var full = cb.like(cb.lower(root.get("fullName")), like);
                return cb.or(nick, full);
            });
        }

        if (StringUtils.hasText(style)) {
            String like = "%" + style.toLowerCase(Locale.ROOT).trim() + "%";
            spec = spec.and((root, q, cb) -> {
                var profile = root.join("profile", JoinType.LEFT);
                return cb.like(cb.lower(profile.get("biography")), like);
            });
        }

        return userLiteRepository.findAllByDeletedDateIsNull(spec, pageable).map(this::mapToModelDto);
    }

    public Page<ModelDto> listModels(Pageable pageable) {
        return searchModels(null, null, pageable);
    }

    /**
     * Fetch full details for a single model.
     *
     * @param id the model id
     * @param limit optional limit to the number of posts/packages included in the DTO
     * @return an optional containing the ModelDto
     */
    public Optional<ModelDto> getModelDetails(Long id, Integer limit) {
        return userLiteRepository
            .findByIdAndDeletedDateIsNull(id)
            .map(userLite -> {
                ModelDto dto = mapToModelDto(userLite);
                // Fetch limited packages (posts omitted for brevity)
                Pageable pageable = Pageable.ofSize(limit == null ? 5 : limit);
                Page<PackageDto> packages = listPackagesByModel(id, null, pageable);
                dto.setPackages(packages.getContent());
                // TODO: include recent posts when PostFeedService is available
                return dto;
            });
    }

    /**
     * List packages belonging to a particular model.
     *
     * @param modelId the id of the creator
     * @param paid optional filter to restrict to paid/free content
     * @param pageable pagination information
     * @return a page of PackageDto objects
     */
    public Page<PackageDto> listPackagesByModel(Long modelId, Boolean paid, Pageable pageable) {
        Specification<ContentPackage> spec = Specification.where(null);
        if (modelId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("createdBy"), modelId.toString()));
        }
        if (paid != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isPaidContent"), paid));
        }
        return contentPackageRepository.findAllByDeletedDateIsNull(spec, pageable).map(this::mapToPackageDto);
    }

    /**
     * Retrieve a single package with its media IDs.
     *
     * @param packageId the package id
     * @return an Optional of PackageDto
     */
    public Optional<PackageDto> getPackageDetails(Long packageId) {
        return contentPackageRepository.findByIdAndDeletedDateIsNull(packageId).map(this::mapToPackageDto);
    }

    private Specification<UserLite> hasCreatorRole() {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<User> userRoot = subquery.from(User.class);
            var authorityJoin = userRoot.join("authorities");
            subquery.select(userRoot.get("id"));
            subquery.where(cb.equal(userRoot.get("id"), root.get("id")), cb.equal(authorityJoin.get("name"), AuthoritiesConstants.CREATOR));
            return cb.exists(subquery);
        };
    }

    private ModelDto mapToModelDto(UserLite user) {
        ModelDto dto = new ModelDto();
        dto.setId(user.getId());
        dto.setNickName(user.getNickName());
        dto.setFullName(user.getFullName());
        if (user.getProfile() != null) {
            dto.setBiography(user.getProfile().getBiography());
            dto.setIsFree(Boolean.TRUE.equals(user.getProfile().getIsFree()));
            String profileKey = user.getProfile().getProfilePhotoS3Key();
            if (profileKey != null) {
                dto.setProfilePhotoUrl(mediaTokenService.getTokenForKey(profileKey));
            }
            String coverKey = user.getProfile().getCoverPhotoS3Key();
            if (coverKey != null) {
                dto.setCoverPhotoUrl(mediaTokenService.getTokenForKey(coverKey));
            }
        }
        String thumbKey = user.getThumbnailS3Key();
        if (thumbKey != null) {
            dto.setThumbnailUrl(mediaTokenService.getTokenForKey(thumbKey));
        }
        // TODO: populate followers count and other stats once available
        return dto;
    }

    private PackageDto mapToPackageDto(ContentPackage pkg) {
        PackageDto dto = new PackageDto();
        dto.setId(pkg.getId());
        dto.setAmount(pkg.getAmount());
        dto.setVideoCount(pkg.getVideoCount());
        dto.setImageCount(pkg.getImageCount());
        dto.setPaid(Boolean.TRUE.equals(pkg.getIsPaidContent()));
        // The createdBy field stores the model's id as a string; parse to long when needed
        if (pkg.getCreatedBy() != null) {
            try {
                dto.setModelId(Long.valueOf(pkg.getCreatedBy()));
            } catch (NumberFormatException ignored) {
                dto.setModelId(null);
            }
        }
        // Use the package id as a stand‑in for a thumbnail; front end will call /media/{id} to obtain the real URL
        dto.setThumbnailUrl(mediaTokenService.getTokenForMedia(pkg.getId()));
        return dto;
    }
}
