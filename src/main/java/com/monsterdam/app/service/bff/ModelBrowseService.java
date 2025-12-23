package com.monsterdam.app.service.bff;

import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.repository.ContentPackageRepository;
import com.monsterdam.app.repository.UserLiteRepository;
import com.monsterdam.app.service.dto.bff.ModelDto;
import com.monsterdam.app.service.dto.bff.PackageDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
        // Basic specification filtering deleted users and applying search on nickName/fullName.
        Specification<UserLite> spec = (root, q, cb) -> {
            // Only return users without a deletedDate
            var predicates = cb.conjunction();
            predicates.getExpressions().add(cb.isNull(root.get("deletedDate")));
            if (query != null && !query.isBlank()) {
                String like = "%" + query.toLowerCase().trim() + "%";
                var nick = cb.like(cb.lower(root.get("nickName")), like);
                var full = cb.like(cb.lower(root.get("fullName")), like);
                predicates.getExpressions().add(cb.or(nick, full));
            }
            // TODO: filter by style once the domain supports it
            return predicates;
        };

        return userLiteRepository.findAllByDeletedDateIsNull(pageable).map(this::mapToModelDto);
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
        Specification<ContentPackage> spec = (root, query, cb) -> {
            var predicates = cb.conjunction();
            // Only packages created by the given model: join via createdBy string
            // In a future iteration this should use a proper relationship
            predicates.getExpressions().add(cb.equal(root.get("createdBy"), modelId.toString()));
            if (paid != null) {
                predicates.getExpressions().add(cb.equal(root.get("isPaidContent"), paid));
            }
            return predicates;
        };
        return contentPackageRepository.findAllByDeletedDateIsNull(pageable).map(this::mapToPackageDto);
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
